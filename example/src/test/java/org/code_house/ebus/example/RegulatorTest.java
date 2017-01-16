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

package org.code_house.ebus.example;

/**
 * Created by splatch on 13.01.2017.
 * master is 0x10, slave (regulator) is 0x68
 */
class RegulatorTest {

//    @Test
//    public void checkCommunication() {
//        Regulator regulator = new Regulator();
//        CodecRegistry codecRegistry = new BasicCodecRegistry();
//
//        EmbeddedChannel channel = new EmbeddedChannel(
//            new MasterTelegramDecoder(),
//            new SlaveTelegramDecoder(),
//            new AckTelegramDecoder(),
//            new EBusEncoder(),
//            new InboundEBusHandler(codecRegistry, null, regulator)
//        );
//
//        EbusRequest data = new GetTemperatureRequest();
//        /*
//        byte[] data = new byte[] {
//            0x10, // sender - master address
//            0x68, // slave - receiver address
//            0x01, 0x68,  // command - get temperature
//            0x00, // data length
//            (byte) 0xBD // CRC
//        };
//        channel.writeInbound(Unpooled.wrappedBuffer(data));
//        */
//
//        assertThat(channel.<Acknowledge>readOutbound()).as("Slave ack").isNotNull();
//        assertThat(channel.<TemperatureResponse>readOutbound()).as("Slave reply").isNotNull()
//            .hasFieldOrPropertyWithValue("temperature", 22);
//
//        data = new SetTemperatureRequest(24);
//        /*
//        data = new byte[] {
//            0x10, // sender - master address
//            0x68, // slave - receiver address
//            0x01, 0x69,  // command - set temperature
//            0x01, // data length
//            0x018, // temperature -> 24
//            (byte) 0x8B // CRC
//        };
//        channel.writeInbound(Unpooled.wrappedBuffer(data));
//        */
//        assertThat(channel.<Acknowledge>readOutbound()).as("Slave ack").isNotNull();
//        assertThat(channel.<TemperatureResponse>readOutbound()).as("Slave reply").isNotNull()
//            .hasFieldOrPropertyWithValue("temperature", 24);
//    }

}