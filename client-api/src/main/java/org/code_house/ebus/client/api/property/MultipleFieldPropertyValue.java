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

package org.code_house.ebus.client.api.property;

import org.code_house.ebus.api.PropertyValue;

import java.util.Map;

/**
 * An special type of property value which is backed by Map, this allows to retrieve or update multiple fields within one
 * request.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 */
public interface MultipleFieldPropertyValue<K, V> extends PropertyValue<Map<K, V>> {

    /**
     * Returns an field which is embedded master property value.
     *
     * @param key Key of sub-property value.
     * @param type Expected type of value kept master
     * @param <T> Type of return value.
     * @return Value of property field.
     */
    <T> T get(String key, Class<T> type);

}
