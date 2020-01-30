/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

//


/**
 * The Class ObjectRef.
 *
 * @param <T>
 */
public class ObjectRef<T> {

    /**
     * The value.
     */
    private T value;

    /**
     * The Constructor.
     */
    public ObjectRef() {
        value = null;
    }

    /**
     * The Constructor.
     *
     * @param value the value
     */
    public ObjectRef(final T value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the value
     */
    public void setValue(final T value) {
        this.value = value;
    }
}
