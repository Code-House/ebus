package org.code_house.ebus.client.common;

import org.code_house.ebus.client.api.BusListener;
import org.code_house.ebus.client.api.event.Event;

import java.util.List;

/**
 * Very basic implementation of bus listener which multicasts received notification to other listeners.
 */
public class CompositeBusListener implements BusListener {

    private final List<BusListener> listeners;

    public CompositeBusListener(List<BusListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void onEvent(Event event) {
        for (BusListener listener : listeners) {
            listener.onEvent(event);
        }
    }

}
