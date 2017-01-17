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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.code_house.ebus.api.Constants;
import org.code_house.ebus.api.Predicates;
import org.code_house.ebus.netty.codec.struct.MasterData;
import org.code_house.ebus.netty.codec.struct.MasterHeader;

import java.nio.ByteBuffer;
import java.util.List;

public class MasterTelegramDecoder extends EBusTelegramDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() == 0) {
            return;
        }

        // buffer we receive might be a correct frame or just data we have between frames
        int index = in.readerIndex();
        if (in.readableBytes() >= 6) {
            // seven bytes is shortest valid ebus telegram, which no data
            byte source = in.readByte();
            byte destination = in.readByte();

            boolean fromMaster = Predicates.IS_MASTER.apply(source);
            boolean broadcast = Constants.BROADCAST_ADDRESS == destination;
            boolean toSlave = Predicates.IS_SLAVE.apply(destination);
            boolean toMaster = Predicates.IS_MASTER.apply(destination);

            // if below condition is correct then we have regular frame, at least it looks like, as long as we are not sure
            // if everything is fine we do not read but just scan buffer
            if (fromMaster && (toMaster || (toSlave || broadcast))) {
                // short is encoded on two bytes
                byte commandGroup = in.readByte();
                byte command = in.readByte();
                byte length = in.readByte();

                if (in.readableBytes() >= length + 1 /* crc */) {
                    ByteBuffer exchange = ByteBuffer.allocate(length);
                    in.getBytes(in.readerIndex(), exchange);
                    in.skipBytes(length);
                    byte crc = in.readByte();

                    MasterData data = new MasterData(exchange, calculateCrc((byte) source, (byte) destination, commandGroup, command, length), crc);
                    // below condition checks crc and verify if we fully consumed frame data and we will leave no remaining
                    // bytes master buffer. If there is any data left or crc doesn't match we might just parse middle of long
                    // telegram which accidentally got spliced or we missed for some reason SYN and just got corrupted
                    // data.
                    if (data.isValid()) {
                        out.add(new MasterHeader(source, destination, commandGroup, command, length));
                        out.add(data);
                        if (in.readableBytes() > 0) {
                            out.add(in.readBytes(super.actualReadableBytes()));
                        }
                        return;
                    }
                }
            }
            // invalid data found
            in.readerIndex(index);
            out.add(in.readBytes(super.actualReadableBytes()));
        }
    }

}

