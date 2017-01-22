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

import org.code_house.ebus.api.Functions;
import org.code_house.ebus.api.identification.DeviceInformation;
import org.code_house.ebus.client.api.EBusConfigurationException;

/**
 * Configuration builder for device registrations.
 */
public class EBusDeviceConfigurationBuilder {

    private Byte master = (byte) 0xFF;
    private Byte slave;

    private DeviceInformation deviceInformation = null;

    public EBusDeviceConfigurationBuilder withMasterAddress(Byte masterAddress) {
        this.master = masterAddress;
        return this;
    }

    public EBusDeviceConfigurationBuilder withSlaveAddress(Byte slaveAddress) {
        this.slave = slaveAddress;
        return this;
    }

    public EBusDeviceConfigurationBuilder withDeviceInformation(DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
        return this;
    }

    public EBusDeviceConfiguration build() throws EBusConfigurationException {
        if (master == null && slave == null) {
            throw new EBusConfigurationException("Both master and slave addresses are not set. If you don't want or don't know what values should be specified please use passive mode.");
        }
        if (master != null && slave == null) {
            slave = Functions.slave(master);
        }
        if (master == null && slave != null) {
            master= Functions.master(slave);
        }

        return new EBusDeviceConfiguration(master, slave, deviceInformation);
    }

}
