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

import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.client.api.device.UnsupportedCommandException;
import org.code_house.ebus.example.cmd.TemperaturePropertyValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * An idiomatic code to verify regulator implementation.
 */
class RegulatorMain {

    private static final int DEFAULT_TEMPERATURE = 22;
    private static final int DESIRED_TEMPERATURE = 24;

    @Test
    public void testRegulatorImplementation() throws UnsupportedCommandException {
        Regulator regulator = new Regulator(DEFAULT_TEMPERATURE);

        PropertyValue<Integer> setpointValue = regulator.set(Commands.GET_TEMPERATURE_SETPOINT, PropertyValue.EMPTY);
        assertThat(setpointValue).as("Default value of temperature setpoint").isNotNull()
            .extracting(pv -> pv.getValue())
            .containsOnly(DEFAULT_TEMPERATURE);

        setpointValue = regulator.set(Commands.SET_TEMPERATURE_SETPOINT, new TemperaturePropertyValue(DESIRED_TEMPERATURE));
        assertThat(setpointValue).as("Updated setpoint value").isNotNull()
            .extracting(pv -> pv.getValue()).containsOnly(DESIRED_TEMPERATURE);
    }
}
