package org.code_house.ebus.client.api;

import org.code_house.ebus.api.EBusException;

/**
 * Exceptions raised by client itself.
 */
public class EBusClientException extends EBusException {
    public EBusClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
