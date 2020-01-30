/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.app;

import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.core.ui.Progress;

//


/**
 * The Class AppProgress.
 */
public class AppProgress implements Progress {

    /**
     * The progress.
     */
    private int progress;

    /**
     * The cancel.
     */
    private boolean cancel;

    /**
     * The level.
     */
    private int level;

    /**
     * The Constructor.
     */
    public AppProgress() {

        progress = 0;
        cancel = false;
        level = OutputTarget.LEVEL_MESSAGE;
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

        if (level > OutputTarget.LEVEL_DEBUG)
            return;
        AbstractApplication.debugln(message);
    }

    /**
     * Verboseln.
     *
     * @param message the message
     */
    public void verboseln(final String message) {

        if (level > OutputTarget.LEVEL_VERBOSE)
            return;
        AbstractApplication.debugln(message);
    }

    /**
     * Messageln.
     *
     * @param message the message
     */
    public void messageln(final String message) {

        if (level > OutputTarget.LEVEL_MESSAGE)
            return;
        AbstractApplication.messageln(message);
    }

    /**
     * Warningln.
     *
     * @param message the message
     */
    public void warningln(final String message) {

        if (level > OutputTarget.LEVEL_WARNING)
            return;
        AbstractApplication.warningln(message);
    }

    /**
     * Warningln.
     *
     * @param message the message
     * @param e       the e
     */
    public void warningln(final String message, final Throwable e) {

        if (level > OutputTarget.LEVEL_WARNING)
            return;
        AbstractApplication.warningln(message, e);
    }

    /**
     * Errorln.
     *
     * @param message the message
     */
    public void errorln(final String message) {

        if (level > OutputTarget.LEVEL_ERROR)
            return;
        AbstractApplication.errorln(message);
    }

    /**
     * Errorln.
     *
     * @param message the message
     * @param e       the e
     */
    public void errorln(final String message, final Throwable e) {

        if (level > OutputTarget.LEVEL_ERROR)
            return;
        AbstractApplication.errorln(message, e);
    }

    /**
     * Formatln.
     *
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     * @return the string
     */
    public String formatln(final int inputlevel, final String message, final Throwable e) {

        return AbstractApplication.formatln(inputlevel, message, e);
    }

    /**
     * Outputln.
     *
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     */
    public void outputln(final int inputlevel, final String message, final Throwable e) {

        AbstractApplication.outputln(inputlevel, message, e);
    }

    /**
     * Write.
     *
     * @param text the text
     */
    public void write(final String text) {
        AbstractApplication.write(text);
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Increment progress.
     *
     * @param amount the amount
     */
    public void incrementProgress(final int amount) {

        this.progress += amount;
        if (this.progress > 100)
            this.progress = 100;
    }

    /**
     * Sets the progress.
     *
     * @param progress the progress
     */
    public void setProgress(final int progress) {

        this.progress = progress;
        if (this.progress > 100)
            this.progress = 100;
        if (this.progress < 0)
            this.progress = 0;
    }

    /**
     * Gets the cancel.
     *
     * @return the cancel
     */
    public boolean getCancel() {
        return cancel;
    }

    /**
     * Sets the cancel.
     *
     * @param cancel the cancel
     */
    public void setCancel(final boolean cancel) {
        this.cancel = cancel;
    }
}
