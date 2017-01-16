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
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.code_house.ebus.api.Constants;
import org.code_house.ebus.netty.codec.struct.Confirmation;
import org.code_house.ebus.netty.codec.struct.Rejection;
import org.code_house.ebus.netty.codec.struct.SlaveData;
import org.code_house.ebus.netty.codec.struct.SlaveHeader;

import java.util.List;

public class SlaveTelegramDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() == 0) {
            return;
        }

        int index = in.readerIndex();
        // no master, no slave, no broadcast -> slave reply or ACK
        if (in.readableBytes() >= 3) {
            // we have at least three bytes which should be slave ACK, slave reply len, slave crc.
            byte ack = in.readByte();
            if (ack == Constants.ACK) { // slave confirmed received data
                byte length = in.readByte();
                if (in.readableBytes() >= length + 1 /* crc */) {
                    byte[] exchange = new byte[length];
                    in.readBytes(exchange, 0, length);
                    byte crc = in.readByte();
                    SlaveData data = new SlaveData(exchange, calculateCrc(length), crc);
                    if (data.isValid()) {
                        // proper slave reply
                        out.add(new Confirmation());
                        out.add(new SlaveHeader(length));
                        out.add(data);
                    } else {
                        System.out.println("Slave CRC fail!!!");
                        in.discardReadBytes();
                    }
                }
            } else if (ack == Constants.NACK) {
                out.add(new Rejection());
            }
        }

        if (out.isEmpty()) {
            System.out.println("slave fail -> \n" + ByteBufUtil.prettyHexDump(in));
            in.readerIndex(index);
        }

        if (in.readableBytes() > 0) {
            out.add(in.readBytes(super.actualReadableBytes()));
        }
    }

    private byte calculateCrc(byte ... bytes) {
        return calculateCrc0((byte) 0, bytes);
    }

    private byte calculateCrc0(byte crc, byte ... bytes) {
        for (byte aByte : bytes) {
            crc = Crc8.crc8_tab(aByte, crc);
        }
        return crc;
    }


}

