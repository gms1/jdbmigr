/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.common;

//


/**
 * The Class SQLState.
 */
public class SQLState {

    /**
     * Gets the message.
     *
     * @param msg       the msg
     * @param sqlstate  the sqlstate
     * @param errorCode the error code
     * @return the message
     */
    public static String getMessage(final String sqlstate, final int errorCode,
        final String msg) {

        final String message =
            "sqlstate=" + sqlstate + ", error=" + errorCode + ", " + msg;
        if (sqlstate.startsWith("23")) {
            if (sqlstate.endsWith("001") || sqlstate.endsWith("504") ||
                sqlstate.endsWith("511"))
                return "cannot update or delete a parent key, " + message;
            if (sqlstate.endsWith("503"))
                return "invalid foreign key, " + message;
            if (sqlstate.endsWith("520"))
                return "cannot add foreign key constraint, " + message;
            if (sqlstate.endsWith("502"))
                return "a column cannot contain null values, " + message;
            if (sqlstate.endsWith("505"))
                return "unique constraint violation, " + message;
            if (sqlstate.endsWith("515"))
                return "cannot add unique index, " + message;
        }
        return message;
    }
}
