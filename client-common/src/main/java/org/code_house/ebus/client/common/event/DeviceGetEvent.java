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
package org.code_house.ebus.client.common.event;

import org.code_house.ebus.Device;
import org.code_house.ebus.PropertyName;
import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.event.GetEvent;

public class DeviceGetEvent extends PropertyEventBase implements GetEvent {

    private final Device destination;
    private final PropertyValue response;

    public DeviceGetEvent(Device source, Device destination, PropertyName name, PropertyValue value, PropertyValue response) {
        super(source, name, value);
        this.destination = destination;
        this.response = response;
    }

    @Override
    public Device getDestination() {
        return destination;
    }

    @Override
    public PropertyValue getResponseValue() {
        return response;
    }

}
