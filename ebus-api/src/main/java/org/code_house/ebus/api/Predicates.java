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

/**
 * Predicates defined below are defining detection logic for master/slave/broadcast. These predicates are defined in same
 * way as they are described in specification. Currently IS_MASTER predicate allows 25 masters on the bus and their slaves
 * as well. Slave detection is quite simple - whatever is not master, must be a slave address.
 */
public interface Predicates {

    /**
     * Master detection logic.
     */
    Predicate<Byte> IS_MASTER = address -> masterIndex(priorityClass(address)) > 0 && masterIndex(subAddress(address)) > 0;

    /**
     * Slave detection logic.
     */
    Predicate<Byte> IS_SLAVE = com.google.common.base.Predicates.not(IS_MASTER);

    /**
     * Broadcast detection logic.
     */
    Predicate<Byte> IS_BROADCAST = address -> Constants.BROADCAST_ADDRESS == address;

}
