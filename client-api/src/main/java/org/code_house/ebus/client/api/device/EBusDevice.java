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

package org.code_house.ebus.client.api.device;

import org.code_house.ebus.client.api.Device;
import org.code_house.ebus.api.Command;
import org.code_house.ebus.api.PropertyValue;

/**
 * An extension of Device type which can be used to implement devices which can participate master communication, not just
 * represent an physical (or logical) entity.
 */
public interface EBusDevice extends Device {

    /**
     * Master - Slave command handling.
     *
     * @param property
     * @param value
     * @param <T>
     * @param <R>
     * @return
     * @throws UnsupportedCommandException
     */
    <T, R> PropertyValue<R> set(Command<T, R> property, PropertyValue<T> value)
        throws UnsupportedCommandException;

    /**
     * Master - Master command handling.
     *
     * @param name
     * @param value
     * @param <T>
     * @return
     * @throws UnsupportedCommandException
     */
    <T> boolean receive(Command<T, Boolean> name, PropertyValue<T> value)
        throws UnsupportedCommandException;

}
