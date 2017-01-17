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

package org.code_house.ebus.netty.codec.struct;

import org.code_house.ebus.netty.codec.Crc8;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Structure which represents message exchange over ebus.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public class MasterData implements Data {

    private final ByteBuffer data;
    private final byte initCrc;
    private final byte crc;

    public MasterData(ByteBuffer data, byte initCrc, byte crc) {
        this.data = data;
        this.initCrc = initCrc;
        this.crc = crc;
    }

    @Override
    public int getCrc() {
        return crc;
    }

    @Override
    public ByteBuffer getData() {
        return data;
    }

    @Override
    public boolean isValid() {
        return Crc8.crc8(data, initCrc) == crc;
    }

    @Override
    public String toString() {
        return "Master Data " + StandardCharsets.UTF_8.decode(data) + "(" + crc + ")";
    }
}
