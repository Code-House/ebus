package org.code_house.ebus.api;

/**
 * eBUS application specification type or any other type which coding is general and can be wrapped into one type.
 * Be aware that each type have specific length which
 */
public interface Type<X> {

    /**
     * Encode java value as a byte sequence.
     */
    byte[] encode(X value);

    /**
     * Decode java value from a byte sequence.
     */
    X decode(byte[] data);

    /**
     * Returns number of bytes take by type.
     */
    short getSize();

}
