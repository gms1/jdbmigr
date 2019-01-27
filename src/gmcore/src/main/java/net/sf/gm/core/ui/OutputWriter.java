/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

// 
/**
 * The Class OutputWriter.
 */
public class OutputWriter implements OutputTarget {

    /** The out. */
    private Writer out;

    /** The level. */
    private int level;

    /**
     * The Constructor.
     */
    public OutputWriter() {

        level = OutputTarget.LEVEL_MESSAGE;
        out = null;
    }

    /**
     * The Constructor.
     * 
     * @param out the out
     */
    public OutputWriter(final Writer out) {

        level = OutputTarget.LEVEL_MESSAGE;
        this.out = out;
    }

    /**
     * Gets the writer.
     * 
     * @return the writer
     */
    protected Writer getWriter() {

        return out;
    }

    /**
     * Sets the writer.
     * 
     * @param out the out
     */
    protected void setWriter(final Writer out) {

        this.out = out;
    }

    /**
     * Gets the message level.
     * 
     * @return the message level
     */
    public int getMessageLevel() {

        return level;
    }

    /**
     * Sets the message level.
     * 
     * @param level the level
     */
    public void setMessageLevel(final int level) {

        this.level = level;
    }

    /**
     * Debugln.
     * 
     * @param message the message
     */
    public void debugln(final String message) {

        outputln(OutputTarget.LEVEL_DEBUG, message, null);
    }

    /**
     * Verboseln.
     * 
     * @param message the message
     */
    public void verboseln(final String message) {

        outputln(OutputTarget.LEVEL_VERBOSE, message, null);
    }

    /**
     * Messageln.
     * 
     * @param message the message
     */
    public void messageln(final String message) {

        outputln(OutputTarget.LEVEL_MESSAGE, message, null);
    }

    /**
     * Warningln.
     * 
     * @param message the message
     */
    public void warningln(final String message) {

        outputln(OutputTarget.LEVEL_WARNING, message, null);
    }

    /**
     * Warningln.
     * 
     * @param message the message
     * @param e       the e
     */
    public void warningln(final String message, final Throwable e) {

        outputln(OutputTarget.LEVEL_WARNING, message, e);
    }

    /**
     * Errorln.
     * 
     * @param message the message
     */
    public void errorln(final String message) {

        outputln(OutputTarget.LEVEL_ERROR, message, null);
    }

    /**
     * Errorln.
     * 
     * @param message the message
     * @param e       the e
     */
    public void errorln(final String message, final Throwable e) {

        outputln(OutputTarget.LEVEL_ERROR, message, e);
    }

    /**
     * Formatln.
     * 
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     * 
     * @return the string
     */
    public String formatln(final int inputlevel, final String message, final Throwable e) {

        if (level > inputlevel)
            return null;
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            switch (level) {
            case LEVEL_WARNING:
                pw.append("WARNING: ");
                break;
            case LEVEL_ERROR:
                pw.append("ERROR: ");
                break;
            }
            pw.append(message);
            if (e != null) {
                pw.append(": ");
                if (e.getMessage() != null)
                    pw.append(e.getMessage());
                else
                    pw.append(e.getClass().getName());

                if (level <= OutputTarget.LEVEL_DEBUG) {
                    pw.println("");
                    e.printStackTrace(pw);
                }
            }
            pw.println("");
            return sw.toString();
        } catch (final Throwable ignore) {
            return null;
        }
    }

    /**
     * Outputln.
     * 
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     */
    public void outputln(final int inputlevel, final String message, final Throwable e) {

        final String text = formatln(inputlevel, message, e);
        if (text != null)
            write(text);
    }

    /**
     * Write.
     * 
     * @param text the text
     */
    public void write(final String text) {

        try {
            out.write(text);
            out.flush();
        } catch (final Throwable ignore) {
        }
    }
}
