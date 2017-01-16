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

package org.code_house.ebus;

import org.code_house.ebus.api.Command;

/**
 * Represents both human-readable and EBus understandable name of property.
 */
public interface PropertyName<T, R> {

    /**
     * Returns human readable property identifier.
     *
     * @return String property identifier.
     */
    String getName();

    /**
     * Returns EBus understandable property identifier.
     *
     * @return Two byte property identifier.
     */
    Command<T, R> getCommand();

}
