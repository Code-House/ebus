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

package org.code_house.ebus.example;

import org.code_house.ebus.api.Command;
import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.device.EBusDevice;
import org.code_house.ebus.device.UnsupportedCommandException;
import org.code_house.ebus.example.cmd.TemperaturePropertyValue;
import org.code_house.ebus.participant.Master;
import org.code_house.ebus.participant.Slave;

/**
 * Basic regulator device which is passive, meaning does not emmit anything to bus directly but just send reply for requests.
 *
 * Two supported operations are get and set temperature.
 * - Get temperature returns current temperature.
 * - Set temperature changes temperature and returns it.
 */
public class Regulator implements EBusDevice {

    private Integer temperature;

    public Regulator(Integer temperature) {
        this.temperature = temperature;
    }

    @Override
    public Master getMaster() {
        return null;
    }

    @Override
    public Slave getSlave() {
        return null;
    }

    @Override
    public <T, R> PropertyValue<R> set(Command<T, R> command, PropertyValue<T> value) throws UnsupportedCommandException {
        if (Commands.SET_TEMPERATURE_SETPOINT.equals(command)) {
            this.temperature = (Integer) value.getValue();
            return fetchTemperature();
        } else if (Commands.GET_TEMPERATURE_SETPOINT.equals(command)) {
            return fetchTemperature();
        }

        throw new UnsupportedCommandException(command, "Command not supported");
    }

    @Override
    public <T> boolean receive(Command<T, Boolean> name, PropertyValue<T> value) throws UnsupportedCommandException {
        return false;
    }

    private <R> PropertyValue<R> fetchTemperature() {
        return (PropertyValue<R>) new TemperaturePropertyValue(temperature);
    }

}
