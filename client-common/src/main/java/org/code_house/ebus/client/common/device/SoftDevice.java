package org.code_house.ebus.client.common.device;

import org.code_house.ebus.api.Command;
import org.code_house.ebus.api.PropertyValue;
import org.code_house.ebus.client.api.device.EBusDevice;
import org.code_house.ebus.client.api.device.UnsupportedCommandException;
import org.code_house.ebus.client.api.participant.Master;
import org.code_house.ebus.client.api.participant.Slave;

/**
 * Device defined as an software program.
 */
public class SoftDevice extends SimpleEBusDevice implements EBusDevice {

    public SoftDevice(Master master, Slave slave) {
        super(master, slave);
    }

    @Override
    public <T, R> PropertyValue<R> set(Command<T, R> property, PropertyValue<T> value) throws UnsupportedCommandException {
        return null;
    }

    @Override
    public <T> boolean receive(Command<T, Boolean> name, PropertyValue<T> value) throws UnsupportedCommandException {
        return false;
    }

}
