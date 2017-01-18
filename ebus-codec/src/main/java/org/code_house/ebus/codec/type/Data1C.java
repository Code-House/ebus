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

package org.code_house.ebus.codec.type;

import org.code_house.ebus.api.Converter;
import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.common.ScalarPropertyValue;

import java.nio.ByteBuffer;

/**
 * Data1c type defined in eBUS specification. Since it is 1 byte coded floating point value we represent it as java float.
 *
 * There is no more compact type which we could use. :)
 *
 * <pre>
 * 2.4.2 Secondary Data Types
 * +--------+------+-----+-----+------------+-------------------+
 * |  Name  | Type | Min | Max | Resolution | Replacement value |
 * +--------+------+-----+-----+------------+-------------------+
 * | Data1c | CHAR |   0 | 100 |        0.5 |               FFh |
 * +--------+------+-----+-----+------------+-------------------+
 *
 * Calculation:
 * y = dec(x) / 2
 * </pre>
 *
 * This type range is 0 to 100 with 0.5 resolution.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public class Data1C implements Converter<Float> {

    /**
     * Replacement value/special value.
     */
    public static final ByteBuffer REPLACEMENT_VALUE = ByteBuffer.allocate(1).put((byte) 0xFF);

    @Override
    public ByteBuffer encode(PropertyValue<Float> propertyValue) {
        short value = propertyValue.getValue().shortValue();
        if (value >= 0 && value <= 100) {
            return ByteBuffer.allocate(1).put((byte) (value * 2));
        }
        return REPLACEMENT_VALUE;
    }

    @Override
    public PropertyValue<Float> decode(ByteBuffer value) {
        if (REPLACEMENT_VALUE.equals(value)) {
            return PropertyValue.EMPTY;
        }

        return new ScalarPropertyValue<>(new Float(new Short(value.get())) / 2);
    }

}
