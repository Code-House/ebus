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

import java.nio.ByteBuffer;

/**
 * Allows to convert values from ebus to java and vice versa.
 *
 * @author Łukasz Dywicki &lt;luke@code-house.org&gt;
 */
public interface Converter<T> {

    /**
     * Convert value represented by java structure into telegram data payload.
     *
     * @param propertyValue Property to be converted.
     * @return Byte buffer representing property value.
     */
    ByteBuffer encode(PropertyValue<T> propertyValue);

    /**
     * Convert byte sequence from telegram into object form.
     *
     * @param value Byte representation of value.
     * @return Property value representing byte sequence.
     */
    PropertyValue<T> decode(ByteBuffer value);

}
