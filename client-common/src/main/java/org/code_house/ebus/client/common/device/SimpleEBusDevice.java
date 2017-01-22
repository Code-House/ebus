package org.code_house.ebus.client.common.device;

import org.code_house.ebus.client.api.Device;
import org.code_house.ebus.client.api.participant.Master;
import org.code_house.ebus.client.api.participant.Slave;

public class SimpleEBusDevice implements Device {

    private final Master master;
    private final Slave slave;

    public SimpleEBusDevice(Master master, Slave slave) {
        this.master = master;
        this.slave = slave;
    }

    @Override
    public Master getMaster() {
        return master;
    }

    @Override
    public Slave getSlave() {
        return slave;
    }

}
