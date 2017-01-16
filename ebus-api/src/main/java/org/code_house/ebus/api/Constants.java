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

package org.code_house.ebus.api;

/**
 * Constant values based on EBus specification.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public interface Constants {

    /**
     * The synchronisation symbol (SYN) byte.
     */
    byte SYN = (byte) 0xAA;

    /**
     * Escape sequence used to quote SYN value.
     */
    byte[] ESC = new byte[] {(byte) 0xA9, (byte) 0x01};

    /**
     * The acknowledge byte.
     */
    byte ACK = (byte) 0x00;

    /**
     * The negative acknowledge byte.
     */
    byte NACK = (byte) 0xFF;

    /**
     * Broadcast address.
     */
    byte BROADCAST_ADDRESS = (byte) 0xFE;

}
