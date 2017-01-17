/*
 * (C) Copyright 2017 Code-House, Łukasz Dywicki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.code_house.ebus.netty.codec;

import com.google.common.base.Optional;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.code_house.ebus.client.api.BusListener;
import org.code_house.ebus.client.api.PropertyMap;
import org.code_house.ebus.api.Command;
import org.code_house.ebus.api.CommandCodec;
import org.code_house.ebus.api.CommandCodecException;
import org.code_house.ebus.api.CommandCodecRegistry;
import org.code_house.ebus.client.common.event.BroadcastEvent;
import org.code_house.ebus.client.common.event.DeviceGetEvent;
import org.code_house.ebus.client.common.event.DevicePushEvent;
import org.code_house.ebus.common.DefaultCommand;
import org.code_house.ebus.netty.codec.struct.Acknowledge;
import org.code_house.ebus.netty.codec.struct.MasterData;
import org.code_house.ebus.netty.codec.struct.MasterHeader;
import org.code_house.ebus.netty.codec.struct.SlaveData;
import org.code_house.ebus.netty.codec.struct.SlaveHeader;
import org.code_house.ebus.netty.codec.struct.Transmittable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.code_house.ebus.api.Predicates.IS_BROADCAST;
import static org.code_house.ebus.api.Predicates.IS_MASTER;

/**
 * Bridge between low level structures and events emmitted back to client.
 */
public class InboundEBusHandler extends SimpleChannelInboundHandler<Transmittable> {

    private final Logger logger = LoggerFactory.getLogger(InboundEBusHandler.class);

    private final CommandCodecRegistry codecRegistry;
    private PropertyMap propertyMap;
    private BusListener listener;

    enum TelegramType {
        UNKNOWN,
        BROADCAST,
        M2M,
        M2S
    }

    private TelegramType type = TelegramType.UNKNOWN;

    // all telegram parts
    private MasterHeader masterHeader;
    private MasterData masterData;
    private Acknowledge slaveAcknowledge;
    private SlaveHeader slaveHeader;
    private SlaveData slaveData;
    private Acknowledge masterAcknowledge;

    public InboundEBusHandler(CommandCodecRegistry codecRegistry, PropertyMap propertyMap, BusListener listener) {
        this.codecRegistry = codecRegistry;
        this.propertyMap = propertyMap;
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Transmittable transmittable) throws Exception {
        // lets iterate over possible telegram parts and see what we can push back to client.
        if (transmittable instanceof MasterHeader) {
            this.masterHeader = (MasterHeader) transmittable;
            this.type = getTelegramType(masterHeader.getDestination());
        } else if (transmittable instanceof MasterData) {
            this.masterData = (MasterData) transmittable;
            if (type == TelegramType.BROADCAST) {
                emmit();
            }
        } else if (transmittable instanceof Acknowledge && type == TelegramType.M2M) {
            // we expect no more data after that
            this.masterAcknowledge = (Acknowledge) transmittable;
            emmit();
        } else if (transmittable instanceof Acknowledge && type == TelegramType.M2S) {
            if (slaveAcknowledge == null) {
                this.slaveAcknowledge = (Acknowledge) transmittable;
            } else {
                // originating master ack
                this.masterAcknowledge = (Acknowledge) transmittable;
                emmit();
            }
        } else if (transmittable instanceof SlaveHeader) {
            this.slaveHeader = (SlaveHeader) transmittable;
        } else if (transmittable instanceof SlaveData) {
            this.slaveData = (SlaveData) transmittable;
        }
    }

    private TelegramType getTelegramType(byte destination) {
        if (IS_BROADCAST.apply(masterHeader.getDestination())) {
             return TelegramType.BROADCAST;
        }

        return IS_MASTER.apply(masterHeader.getDestination()) ? TelegramType.M2M : TelegramType.M2S;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        emmit();
    }

    private void emmit() {
        Command command = new DefaultCommand(masterHeader.getPrimary(), masterHeader.getSecondary());
        Optional<CommandCodec> codec = codecRegistry.find(command);

        if (!codec.isPresent()) {
            logger.warn("Could not find data mapping for command {}. Ignoring", command);
            reset();
            return;
        }

        try {
            switch (type) {
                case BROADCAST:
                    if (masterHeader != null && masterData != null) {
                        listener.onEvent(new BroadcastEvent(null, propertyMap.getPropertyName(command), codec.get().master().decode(masterData.getData())));
                    }
                case M2M:
                    if (masterHeader != null && masterData != null && masterAcknowledge != null && masterAcknowledge.isAccepted()) {
                        listener.onEvent(new DevicePushEvent(null, null, propertyMap.getPropertyName(command), codec.get().master().decode(masterData.getData())));
                    }
                case M2S:
                    if (masterHeader != null && masterData != null && masterAcknowledge != null && masterAcknowledge.isAccepted() &&
                        slaveHeader != null && slaveData != null && slaveAcknowledge != null && slaveAcknowledge.isAccepted()) {
                        listener.onEvent(new DeviceGetEvent(null, null,
                            propertyMap.getPropertyName(command),
                            codec.get().master().decode(masterData.getData()),
                            codec.get().slave().decode(slaveData.getData())) // error - slave reply is
                        );
                    }
            }
        } catch (CommandCodecException e) {
            logger.error("Could not parse raw data", e);
        } finally {
            reset();
        }
    }

    private void reset() {
        this.type = TelegramType.UNKNOWN;
        this.masterHeader = null;
        this.masterData = null;
        this.slaveAcknowledge = null;
        this.slaveHeader = null;
        this.slaveData = null;
        this.masterAcknowledge = null;
    }

}
