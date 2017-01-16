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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.code_house.ebus.api.Command;
import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.api.CommandCodec;
import org.code_house.ebus.api.CommandCodecException;
import org.code_house.ebus.api.CommandCodecRegistry;
import org.code_house.ebus.api.Constants;
import org.code_house.ebus.common.DefaultCommand;
import org.code_house.ebus.device.EBusDevice;
import org.code_house.ebus.device.UnsupportedCommandException;
import org.code_house.ebus.netty.codec.struct.Confirmation;
import org.code_house.ebus.netty.codec.struct.MasterData;
import org.code_house.ebus.netty.codec.struct.MasterHeader;
import org.code_house.ebus.netty.codec.struct.Rejection;
import org.code_house.ebus.netty.codec.struct.SlaveData;
import org.code_house.ebus.netty.codec.struct.SlaveHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;

/**
 * Outbound handler which controls writing to bus.
 *
 * Because bus access is not random it requires arbitration. This means that before writing any telegram to bus we must
 * be sure that there is no other participant trying to do the same. For that reason we must write own master address
 * and read it back. If we read same byte as we wrote this means access to bus is granted.
 *
 * Note that this handler is not shareable as it tracks connection state.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public class OutboundEBusHandler extends ChannelDuplexHandler {

    private final Logger logger = LoggerFactory.getLogger(OutboundEBusHandler.class);

    private final ArrayDeque<ToSend> entries = new ArrayDeque<>(100);

    private final CommandCodecRegistry codecRegistry;
    private final EBusDevice device;

    private Confirmation acknowledge = null;
    private ChannelHandlerContext ctx;


    private MasterHeader masterHeader;
    private MasterData masterData;

    enum State {
        LISTEN,
        WRITE,
        WAIT
    }

    private boolean writeInProgress = false;
    private boolean accessGranted = false;

    public OutboundEBusHandler(CommandCodecRegistry codecRegistry, EBusDevice device) {
        this.codecRegistry = codecRegistry;
        this.device = device;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // receive
        if (msg instanceof MasterHeader) {
            this.masterHeader = (MasterHeader) msg;
        } else if (msg instanceof MasterData) {
            this.masterData = (MasterData) msg;
        }

        if (msg instanceof ByteBuf) {
            ByteBuf buffer = (ByteBuf) msg;
            if (writeInProgress && buffer.capacity() == 1) {
                byte aByte = buffer.getByte(0);
                if (device.getMaster().getAddress() == aByte) {
                    // we got access to bus, lets try to flush something
                    accessGranted = true;
                    flush(ctx);
                    return;
                }
            }
            writeInProgress = false;

            if (buffer.isReadable()) {
                byte lastByte = buffer.getByte(buffer.readerIndex());
                if (lastByte == Constants.SYN) {
                    // we received SYN symbol, try to write own address
                    writeInProgress = true;
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{device.getMaster().getAddress()})); // force writing master address to bus
                }
            }
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (masterHeader != null && masterData != null) {
            // passive get
            int destination = masterHeader.getDestination();
            if (device.getSlave().getAddress() == destination) {
                Command command = new DefaultCommand(masterHeader.getPrimary(), masterHeader.getSecondary());
                Optional<CommandCodec> mapper = codecRegistry.find(command);

                if (mapper.isPresent()){
                    try {
                        PropertyValue value = mapper.get().master().decode(masterData.getData());
                        if (value == null) {
                            ctx.write(new Rejection());
                        } else {
                            // ok, we received data and mapper was able to parse it, try to set value
                            PropertyValue returnValue = device.set(command, value);
                            if (returnValue == null) {
                                ctx.write(new Rejection());
                            }

                            byte[] reply = mapper.get().slave().encode(returnValue);
                            // data should not exceed 16 bytes, so we're safe here
                            ctx.write(new SlaveHeader((short) reply.length));
                            ctx.write(new SlaveData(reply, (byte) 0, (byte) 0));

                        }
                    } catch (UnsupportedCommandException | CommandCodecException e) {
                        logger.error("Could not answer slave command", e);
                    }
                } else {
                    logger.warn("Received slave command {}, but could not find mapping for it. Ignoring", command);
                    ctx.write(new Rejection());
                }
            }

            if (device.getMaster().getAddress() == destination) {
                Command command = new DefaultCommand(masterHeader.getPrimary(), masterHeader.getSecondary());
                Optional<CommandCodec> mapper = codecRegistry.find(command);
                if (mapper.isPresent()) {
                    try {
                        PropertyValue value = mapper.get().master().decode(masterData.getData());
                        if (value != null && device.receive(command, value)) {
                            ctx.write(new Confirmation()); // ok, we received data and mapper is able to parse it
                        } else {
                            ctx.write(new Rejection());
                        }
                    } catch (UnsupportedCommandException | CommandCodecException e) {
                        logger.error("Could not handle master command", e);
                    }
                } else {
                    logger.warn("Received master command {}, but could not find mapping for it. Ignoring", command);
                }
            }

            ctx.flush();
        }
        masterHeader = null;
        masterData = null;

        ctx.fireChannelReadComplete();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf && ((ByteBuf) msg).capacity() == 1) {
            ctx.write(msg);
        }

        entries.addLast(new ToSend(msg, promise));
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isWritable() && accessGranted) {
            ToSend toSend = entries.pollFirst();

            ctx.write(toSend.toSend, toSend.promise);
            ctx.write(Unpooled.buffer().writeByte(Constants.SYN));
            accessGranted = false;
            writeInProgress = false;
        }
        ctx.flush();
    }

    public void write(Object message) {
        entries.addLast(new ToSend(message, ctx.newPromise()));
    }

    private static final class ToSend {
        final Object toSend;
        final ChannelPromise promise;

        private ToSend(final Object toSend, final ChannelPromise promise) {
            this.toSend = toSend;
            this.promise = promise;
        }
    }

}

