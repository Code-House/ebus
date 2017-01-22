package org.code_house.ebus.client.common.participant;

import org.code_house.ebus.client.api.participant.Master;
import org.code_house.ebus.client.api.participant.Slave;
import org.code_house.ebus.client.common.SimpleParticipant;

public class SimpleSlave extends SimpleParticipant implements Slave {
    private Master master;

    public SimpleSlave(byte address) {
        super(address);
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    @Override
    public Master getMaster() {
        return master;
    }

}
