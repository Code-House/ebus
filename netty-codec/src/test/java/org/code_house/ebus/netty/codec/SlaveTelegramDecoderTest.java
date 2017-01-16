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

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.code_house.ebus.netty.codec.struct.Confirmation;
import org.code_house.ebus.netty.codec.struct.SlaveData;
import org.code_house.ebus.netty.codec.struct.SlaveHeader;
import org.junit.jupiter.api.Test;

class SlaveTelegramDecoderTest {
    @Test
    void decode() {
        EmbeddedChannel channel = new EmbeddedChannel(new SlaveTelegramDecoder());
        channel.writeInbound(Unpooled.wrappedBuffer(new byte[] {0x00, 0x01, 0x01, (byte) 0x9A}));

        Condition<SlaveHeader> header = new Condition<>(telegramHeader -> telegramHeader.getLength() == 1,"Header length is equal to 1");

        Condition<SlaveData> dataCrc = new Condition<>(telegramData -> telegramData.getCrc() == 0x9A,"Data crc is equal to 0x9A");

        // slave ack
        Assertions.assertThat((Object) channel.readInbound()).as("Acknowledge")
            .isNotNull()
            .isInstanceOf(Confirmation.class);

        // slave header
        Assertions.assertThat((Object) channel.readInbound()).as("Slave header")
            .isNotNull()
            .isInstanceOfSatisfying(SlaveHeader.class, slaveHeader -> header.matches(slaveHeader));

        // slave data
        Assertions.assertThat((SlaveData) channel.readInbound()).as("Slave reply")
            .isNotNull()
            .isInstanceOfSatisfying(SlaveData.class, data -> dataCrc.matches(data))
            .matches(data -> data.isValid());
    }

}