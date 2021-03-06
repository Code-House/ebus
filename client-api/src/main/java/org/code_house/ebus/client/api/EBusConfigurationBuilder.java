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
 * Definition of configuration in fluent form.
 *
 * Builder is mutable so methods can be called multiple times.
 */
public class EBusConfigurationBuilder {

    private TypeRegistry typeRegistry;
    private PropertyMap propertyMap;
    private CommandCodecRegistry commandCodecRegistry;
    private EBusDeviceConfiguration deviceConfiguration;

    public <X extends EBusConfigurationBuilder> X withTypeRegistry(TypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
        return (X) this;
    }

    <X extends EBusConfigurationBuilder> X withPropertyMap(PropertyMap propertyMap) {
        this.propertyMap = propertyMap;
        return (X) this;
    }

    <X extends EBusConfigurationBuilder> X withCommandCodecRegistry(CommandCodecRegistry commandCodecRegistry) {
        this.commandCodecRegistry = commandCodecRegistry;
        return (X) this;
    }

    <X extends EBusConfigurationBuilder> X active(EBusDeviceConfiguration deviceConfiguration) {
        this.deviceConfiguration = deviceConfiguration;
        return (X) this;
    }

    <X extends EBusConfiguration> X build() {
        return (X) new EBusConfiguration(typeRegistry, commandCodecRegistry, propertyMap, deviceConfiguration);
    }

}
