package org.code_house.ebus.client.common;

import org.code_house.ebus.client.api.Participant;

/**
 * An base implementation of participant which can be part of communication over eBUS.
 */
public abstract class SimpleParticipant implements Participant {

    private final byte address;

    protected SimpleParticipant(byte address) {
        this.address = address;
    }

    @Override
    public byte getAddress() {
        return address;
    }

}
