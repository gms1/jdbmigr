/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.properties;

import net.sf.gm.core.utils.StringUtil;

//


/**
 * The Class ConfigurationException.
 */
public class ConfigurationException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    static final long serialVersionUID = 1;

    /**
     * The Constructor.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param arg0 the arg0
     */
    public ConfigurationException(final String arg0) {
        super(arg0);
    }

    /**
     * The Constructor.
     *
     * @param from the from
     */
    public ConfigurationException(final Throwable from) {

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
    public ConfigurationException(final String message, final Throwable from) {

        super(message + ": " + StringUtil.getExceptionMessage(from));
        this.setStackTrace(from.getStackTrace());
        this.initCause(from);
    }
}
