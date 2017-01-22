package org.code_house.ebus.api.identification;

/**
 * Simple representation of version and revision.
 */
public class VersionAndRevision {

    private final int version;
    private final int revision;

    public VersionAndRevision(int version, int revision) {
        this.version = version;
        this.revision = revision;
    }

    public int getVersion() {
        return version;
    }

    public int getRevision() {
        return revision;
    }

}
