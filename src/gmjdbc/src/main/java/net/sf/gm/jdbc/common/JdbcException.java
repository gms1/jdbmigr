/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.common;

import net.sf.gm.core.utils.StringUtil;

import java.sql.SQLException;

//


/**
 * The Class JdbcException.
 */
public class JdbcException extends SQLException {

    /**
     * The Constant serialVersionUID.
     */
    static final long serialVersionUID = 1;

    /**
     * The Constructor.
     */
    public JdbcException() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param message the message
     */
    public JdbcException(final String message) {
        super(message);
    }

    /**
     * The Constructor.
     *
     * @param from the from
     */
    public JdbcException(final Throwable from) {

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
    public JdbcException(final String message, final Throwable from) {

        super(message + ": " + StringUtil.getExceptionMessage(from));
        this.setStackTrace(from.getStackTrace());
        this.initCause(from);
    }
}
