/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import net.sf.gm.core.utils.StringUtil;

import java.io.IOException;

//


/**
 * The Class ConfigurationException.
 */
public class ConfigurationException extends IOException {

    /**
     * The Constant serialVersionUID.
     */
    static final long serialVersionUID = 1;

    /**
     * The Constructor.
     *
     * @param message the message
     */
    ConfigurationException(final String message) {
        super(message);
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
