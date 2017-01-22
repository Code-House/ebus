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
package org.code_house.ebus.client.api;

import org.code_house.ebus.api.CommandCodecRegistry;
import org.code_house.ebus.api.TypeRegistry;
import org.code_house.ebus.client.api.device.EBusDeviceConfiguration;

/**
 * Configuration holder keeps information about registrars used during interactions with bus.
 */
public class EBusConfiguration {

    private final TypeRegistry typeRegistry;
    private final CommandCodecRegistry commandCodecRegistry;
    private final PropertyMap propertyMap;
    private final EBusDeviceConfiguration deviceConfiguration;

    public EBusConfiguration(TypeRegistry typeRegistry, CommandCodecRegistry commandCodecRegistry, PropertyMap propertyMap, EBusDeviceConfiguration deviceConfiguration) {
        this.typeRegistry = typeRegistry;
        this.commandCodecRegistry = commandCodecRegistry;
        this.propertyMap = propertyMap;
        this.deviceConfiguration = deviceConfiguration;
    }

    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    public CommandCodecRegistry getCommandCodecRegistry() {
        return commandCodecRegistry;
    }

    public PropertyMap getPropertyMap() {
        return propertyMap;
    }

    public EBusDeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

}
