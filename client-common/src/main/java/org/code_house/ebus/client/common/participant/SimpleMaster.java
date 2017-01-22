package org.code_house.ebus.client.common.participant;

import org.code_house.ebus.client.api.participant.Master;
import org.code_house.ebus.client.api.participant.Slave;
import org.code_house.ebus.client.common.SimpleParticipant;

public class SimpleMaster extends SimpleParticipant implements Master {

    private final Slave slave;

    public SimpleMaster(byte address, Slave slave) {
        super(address);
        this.slave = slave;
    }

    @Override
    public Slave getSlave() {
        return slave;
    }

}
