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
package org.code_house.ebus.client.api.device;

import org.code_house.ebus.api.identification.DeviceInformation;

/**
 * Configuration options for device registration.
 */
public class EBusDeviceConfiguration {

    private final byte master;
    private final byte slave;
    private final DeviceInformation deviceInformation;

    public EBusDeviceConfiguration(byte master, byte slave, DeviceInformation deviceInformation) {
        this.master = master;
        this.slave = slave;
        this.deviceInformation = deviceInformation;
    }

    public byte getMaster() {
        return master;
    }

    public byte getSlave() {
        return slave;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

}
