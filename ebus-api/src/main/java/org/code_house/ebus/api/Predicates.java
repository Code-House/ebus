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

import com.google.common.base.Predicate;

import static org.code_house.ebus.api.Functions.*;

public interface Predicates {

    Predicate<Byte> IS_MASTER = address -> MASTER_INDEX.apply(HIGH.apply(address)) > 0 && MASTER_INDEX.apply(LOW.apply(address)) > 0;

    Predicate<Byte> IS_SLAVE = com.google.common.base.Predicates.not(IS_MASTER);

    Predicate<Byte> IS_BROADCAST = address -> Constants.BROADCAST_ADDRESS == address;


}
