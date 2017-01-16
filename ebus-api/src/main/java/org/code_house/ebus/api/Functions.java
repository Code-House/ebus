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

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Integer.bitCount;

/**
 * Set of utilities related to ebus itself.
 *
 * Even if address number from specification fits into short range master Java all bit operations return integers so for
 * clearness of code and less explicit casts we loose few extra bytes master memory.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public interface Functions {

    /**
     * Represents index of master using positive 1-5 integer.
     */
    Function<Byte, Integer> MASTER_INDEX = number -> {
        if (number == 0x0) {
            return 1;
        } else if (number == 0x1) {
            return 2;
        } else if (number == 0x3) {
            return 3;
        } else if (number == 0x07) {
            return 4;
        } else if (number == 0xF) {
            return 5;
        }
        return 0;
    };

    /**
     * Return bits 4-7 from address number (first 4 bits reading from left).
     */
    Function<Byte, Byte> LOW = address -> (byte) (address & 0x0F);

    /**
     * Return bits 0-3 from address number (first 4 bits reading from right).
     */
    Function<Byte, Byte> HIGH = address -> (byte) ((address >> 4) & 0x0F);

    Function<Byte, Byte> SUB_ADDRESS = LOW;

    Function<Byte, Byte> PRIORITY_CLASS = HIGH;

    /**
     * Priority function which allows to order elements on the bus. Since specification defines priority class and
     * sub addresses there is no single digit which represents priority. Both priority class and sub address numbers may
     * repeat for different combinations thus we must make it a single value.
     * This function return values from 0 to 24 which are also index number of master defined master specification.
     */
    Function<Byte, Integer> PRIORITY = address -> bitCount(PRIORITY_CLASS.apply(address)) * 5 + bitCount(SUB_ADDRESS.apply(address));

    Function<Byte, Byte> GET_SLAVE = address -> (byte) (address > 250 ? (address - 256) + 5 : address + 5);

//    Function<byte[], Integer> CRC = data -> {
//
//    };

}
