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

/**
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public class MasterHeader implements Header {

    private final byte source;
    private final byte destination;
    private final short length;
    private final byte primary;
    private final byte secondary;

    public MasterHeader(byte source, byte destination, byte primary, byte secondary, short length) {
        this.source = source;
        this.destination = destination;
        this.primary = primary;
        this.secondary = secondary;
        this.length = length;
    }

    public int getSource() {
        return source;
    }

    public byte getDestination() {
        return destination;
    }

    public byte getPrimary() {
        return primary;
    }

    public byte getSecondary() {
        return secondary;
    }

    @Override
    public short getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Master " + source + "->" + destination + " [" + length + "]";
    }

}
