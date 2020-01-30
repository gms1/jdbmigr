/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.model;

import java.util.SortedSet;
import java.util.TreeSet;

//


/**
 * The Class SortedSetModel.
 *
 * @param <T>
 */
public class SortedSetModel<T> extends ModelServer<SortedSetModel<T>> {

    /**
     * The set.
     */
    private final SortedSet<T> set;

    /**
     * The Constructor.
     */
    public SortedSetModel() {

        super();
        //noinspection SortedCollectionWithNonComparableKeys
        set = new TreeSet<T>();
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    @Override
    protected SortedSetModel<T> getModel() {

        return this;
    }

    /**
     * Clear.
     */
    public void clear() {

        set.clear();
        notifyClients();
    }

    /**
     * Add.
     *
     * @param text the text
     */
    public void add(final T text) {

        set.add(text);
        notifyClients();
    }

    /**
     * Remove.
     *
     * @param text the text
     */
    public void remove(final T text) {

        set.remove(text);
        notifyClients();
    }

    /**
     * Contains.
     *
     * @param text the text
     * @return true, if contains
     */
    public boolean contains(final T text) {
        return set.contains(text);
    }

    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
        return set.size();
    }

    /**
     * To array.
     *
     * @return the t[]
     */
    public T[] toArray() {

        // T[] s = new T[size()];
        // return set.toArray(s);
        // compiler error: "cannot create a generic array of T"

        // 1. dirty hack:
        // T[] s =
        // T[].class.cast(java.lang.reflect.Array.newInstance(T[].class.getComponentType(),
        // size()));
        // return set.toArray(s);
        // compile error:
        // [javac]
        // /home/gms/xgen/share/dev/java/jgms/src/org/gms/core/model/SortedSetModel.java:58:
        // cannot select from a type variable
        // [javac] T[] s =
        // T[].class.cast(java.lang.reflect.Array.newInstance(T[].class.getComponentType(),
        // size()));

        // 2. dirty hack:
        // return (T[]) set.toArray();
        throw new UnsupportedOperationException(
            "T[] SortedSetModel< T >.toArray() is not supported");
    }
}
