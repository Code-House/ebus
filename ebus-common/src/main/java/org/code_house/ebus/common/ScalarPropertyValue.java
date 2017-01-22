package org.code_house.ebus.common;

import org.code_house.ebus.api.PropertyValue;

/**
 * Simple property which is supposed to keep scalar values.
 *
 * @author Łuaksz Dywicki &lt;luke@code-house.org&gt;
 */
public class ScalarPropertyValue<T> implements PropertyValue<T> {

    private final T value;

    public ScalarPropertyValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

}
