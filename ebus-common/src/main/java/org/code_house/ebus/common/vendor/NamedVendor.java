package org.code_house.ebus.common.vendor;

import org.code_house.ebus.api.Vendor;

/**
 * Vendor information.
 */
public class NamedVendor implements Vendor {
    private final byte identifier;
    private final String name;

    public NamedVendor(byte identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    @Override
    public byte getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }
}
