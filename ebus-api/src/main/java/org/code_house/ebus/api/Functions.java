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

import static java.lang.Integer.bitCount;

/**
 * Set of utilities related to ebus itself.
 *
 * Even if address number from specification fits into short range master Java all bit operations return integers so for
 * clearness of code and less explicit casts we loose few extra bytes master memory.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public final class Functions {

    /**
     * Calculates index of master using positive 1-5 integer.
     * This is handy in arbitration process.
     *
     * @param address Master address
     * @return Index of master - if its first, second or ... fifth.
     */
    final static Integer masterIndex(byte address) {
        if (address == 0x0) {
            return 1;
        } else if (address == 0x1) {
            return 2;
        } else if (address == 0x3) {
            return 3;
        } else if (address == 0x07) {
            return 4;
        } else if (address == 0xF) {
            return 5;
        }
        return 0;
    }

    /**
     * Return bits 4-7 from address number (first 4 bits reading from left).
     *
     * @param address Address encoded as byte (0-254).
     * @return First 4 bits from left of given byte.
     */
    private final static byte lowBytes(byte address) {
        return (byte) (address & 0x0F);
    }

    /**
     * Return bits 0-3 from address number (first 4 bits reading from right).
     *
     * @param address Address encoded as byte (0-254).
     * @return First 4 bits from right of given byte.
     */
    private final static byte highBytes(byte address) {
        return (byte) ((address >> 4) & 0x0F);
    }

    /**
     * Calculates per specification sub address of given address.
     *
     * @param address Address encoded as byte (0-254).
     * @return Sub address of given address.
     */
    public static byte subAddress(byte address) {
        return lowBytes(address);
    }

    /**
     * Calculates per specification priority class of an address.
     *
     * @param address Address encoded as byte (0-254).
     * @return Priority class of given address.
     */
    public static byte priorityClass(byte address) {
        return highBytes(address);
    }

    /**
     * Priority function which allows to order elements on the bus. Since specification defines priority class and
     * sub addresses there is no single digit which represents priority. Both priority class and sub address numbers may
     * repeat for different combinations thus we must make it a single value.
     * This function return values from 0 to 24 which are also index number of master defined master specification.
     *
     * @param address Master (most likely) address.
     */
    public static Integer priority(byte address) {
        return bitCount(priorityClass(address)) * 5 + bitCount(subAddress(address));
    }

    /**
     * Calculate slave address of passed master address. Be aware that this utility does not check is passed address is
     * valid master.
     *
     * @param address Master address.
     * @return Slave address.
     */
    public static byte slave(byte address) {
        return (byte) (address > 250 ? (address - 256) + 5 : address + 5);
    }

}
