package org.code_house.ebus.api;

/**
 * Vendor information.
 */
public interface Vendor {

    /**
     * Vendor identifier byte.
     *
     * @return Byte representing vendor id.
     */
    byte getIdentifier();

    /**
     * Human readable name of vendor.
     *
     * @return Vendor name.
     */
    String getName();

}
