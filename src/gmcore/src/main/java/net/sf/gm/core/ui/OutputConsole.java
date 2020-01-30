/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.ui;

import java.io.PrintWriter;
import java.io.Writer;

//


/**
 * The Class OutputConsole.
 */
public class OutputConsole extends OutputWriter {

    /**
     * The err.
     */
    final PrintWriter err;

    /**
     * The Constructor.
     */
    public OutputConsole() {

        super(new PrintWriter(System.out));
        err = new PrintWriter(System.err);
    }

    /**
     * Outputln.
     *
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     */
    @Override
    public void outputln(final int inputlevel, final String message,
        final Throwable e) {

        if (inputlevel < OutputTarget.LEVEL_ERROR &&
            inputlevel > OutputTarget.LEVEL_DEBUG)
            super.outputln(inputlevel, message, e);
        else {
            final Writer out = getWriter();
            setWriter(err);
            super.outputln(inputlevel, message, e);
            setWriter(out);
        }
    }
}
