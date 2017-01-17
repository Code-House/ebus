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

import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Common stuff for working with eBUS telegrams and their fragments.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public abstract class EBusTelegramDecoder extends ByteToMessageDecoder {

    protected byte calculateCrc(byte... bytes) {
        return calculateCrc0((byte) 0, bytes);
    }

    private byte calculateCrc0(byte crc, byte ... bytes) {
        for (byte aByte : bytes) {
            crc = Crc8.crc8_tab(aByte, crc);
        }
        return crc;
    }

}
