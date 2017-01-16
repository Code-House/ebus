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

package org.code_house.ebus.common;

import com.google.common.base.Optional;
import org.code_house.ebus.api.Command;
import org.code_house.ebus.api.CommandCodecRegistry;
import org.code_house.ebus.api.CommandCodec;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class BasicCodecRegistry implements CommandCodecRegistry {

    private final Map<Command, CommandCodec> codecs = new HashMap<>();

    @Override
    public <T, R> Optional<CommandCodec<T, R>> find(Command<T, R> command) {
        if (codecs.containsKey(command)) {
            return Optional.of(codecs.get(command));
        }
        return Optional.absent();
    }

    @Override
    public <T, R> void addCodec(Command<T, R> command, CommandCodec codec) {
        codecs.put(command, codec);
    }

    @Override
    public <T, R> CommandCodec<T, R> removeCodec(Command<T, R> command) {
        return codecs.remove(command);
    }
}
