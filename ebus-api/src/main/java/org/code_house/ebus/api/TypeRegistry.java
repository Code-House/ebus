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
 * Type registry allows to define dynamically type table used for decoding telegram parts.
 */
public interface TypeRegistry {

    /**
     * Lookup converter for given type name.
     *
     * @param name Name of type such data1c.
     * @param <X> Expected java type representation.
     * @return Type converter or null.
     */
    <X> Type<X> getType(String name);

}
