/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import net.sf.gm.core.utils.StringUtil;

//


/**
 * The Class DataIOException.
 */
public class DataIOException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8001475298306036108L;

    /**
     * The Constructor.
     */
    public DataIOException() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param arg0 the arg0
     */
    public DataIOException(final String arg0) {
        super(arg0);
    }

    /**
     * The Constructor.
     *
     * @param from the from
     */
    public DataIOException(final Throwable from) {

        super(StringUtil.getExceptionMessage(from));
        this.setStackTrace(from.getStackTrace());
        this.initCause(from);
    }

    /**
     * The Constructor.
     *
     * @param message the message
     * @param from    the from
     */
    public DataIOException(final String message, final Throwable from) {

        super(message + ": " + StringUtil.getExceptionMessage(from));
        this.setStackTrace(from.getStackTrace());
        this.initCause(from);
    }

    /**
     * The Class UnsupportedDataType.
     */
    public static class UnsupportedDataType extends DataIOException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = 6830499131004232532L;

        /**
         * The Constructor.
         *
         * @param typeName the type name
         */
        public UnsupportedDataType(String typeName) {

            super("unsupported data type: '" + typeName + "'");
        }

        /**
         * The Constructor.
         *
         * @param typeName  the type name
         * @param className the class name
         */
        public UnsupportedDataType(String typeName, String className) {

            super(className + ": unsupported data type: '" + typeName + "'");
        }
    }
}
