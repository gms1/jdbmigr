/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.util.Comparator;

//


/**
 * The Class Comparators.
 */
public class Comparators {

    /**
     * The Class ObjectToStringComparator.
     */
    public static class ObjectToStringComparator implements Comparator<Object> {

        /**
         * Compare.
         *
         * @param arg1 the arg1
         * @param arg0 the arg0
         * @return the int
         */
        public int compare(final Object arg0, final Object arg1) {

            return arg0.toString().compareTo(arg1.toString());
        }
    }


    /**
     * The Class StringCSComparator.
     */
    public static class StringCSComparator implements Comparator<String> {

        /**
         * Compare.
         *
         * @param arg1 the arg1
         * @param arg0 the arg0
         * @return the int
         */
        public int compare(final String arg0, final String arg1) {

            return arg0.compareTo(arg1);
        }
    }


    /**
     * The Class StringCIComparator.
     */
    public static class StringCIComparator implements Comparator<String> {

        /**
         * Compare.
         *
         * @param arg1 the arg1
         * @param arg0 the arg0
         * @return the int
         */
        public int compare(final String arg0, final String arg1) {

            return arg0.toUpperCase().compareTo(arg1.toUpperCase());
        }
    }
}
