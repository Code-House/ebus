package org.code_house.ebus.api.identification;

import org.code_house.ebus.api.Vendor;

/**
 * Information about device it's hardware, software, vendor and identifier.
 */
public class DeviceInformation {

    private final Vendor manufacturer;
    private final String id;
    private final VersionAndRevision software;
    private final VersionAndRevision hardware;

    public DeviceInformation(Vendor manufacturer, String id, VersionAndRevision software, VersionAndRevision hardware) {
        this.manufacturer = manufacturer;
        this.id = id;
        this.software = software;
        this.hardware = hardware;
    }

    public Vendor getManufacturer() {
        return manufacturer;
    }

    public String getId() {
        return id;
    }

    public VersionAndRevision getSoftware() {
        return software;
    }

    public VersionAndRevision getHardware() {
        return hardware;
    }
}
