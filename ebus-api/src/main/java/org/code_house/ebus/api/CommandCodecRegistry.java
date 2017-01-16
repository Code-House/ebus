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

import com.google.common.base.Optional;

/**
 * Registry responsible for mapping incoming and outgoing data from EBus.
 */
public interface CommandCodecRegistry {

    <T, R> Optional<CommandCodec<T, R>> find(Command<T, R> command);
    <T, R> void addCodec(Command<T, R> command, CommandCodec codec);
    <T, R> CommandCodec<T, R> removeCodec(Command<T, R> command);

//    Optional<CommandCodec> find(byte primaryCommand, final byte secondaryCommand);
//
//    void addCodec(byte primaryCommand, byte secondaryCommand, CommandCodec codec);
//    CommandCodec removeCodec(byte primaryCommand, byte secondaryCommand);

}
