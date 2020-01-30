/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.app;

import net.sf.gm.core.properties.AppProperties;
import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.core.utils.DumpUtil;

//


/**
 * The Class App.
 */
public abstract class AbstractApplication {

    /**
     * show elapsed time.
     */
    boolean showElapsed;

    /**
     * show final message.
     */
    boolean showFinalMessage;

    /**
     * The elapsed time.
     */
    long elapsedTime;

    /**
     * The Constructor.
     */
    public AbstractApplication() {

        final String debugVar = System.getenv("DEBUG");
        final boolean debug = debugVar != null && !debugVar.equals("0");
        AbstractApplication.out.setMessageLevel(debug ? OutputTarget.LEVEL_DEBUG : OutputTarget.LEVEL_MESSAGE);

        showElapsed = false;
        elapsedTime = 0;
        showFinalMessage = false;
    }

    /**
     * Gets the show elapsed flag.
     *
     * @return the show elapsed
     */
    public boolean getShowElapsed() {
        return showElapsed;
    }

    /**
     * Sets the show elapsed.
     *
     * @param showElapsed the show elapsed
     */
    public void setShowElapsed(final boolean showElapsed) {

        this.showElapsed = showElapsed;
    }

    /**
     * Gets the show final message.
     *
     * @return the show final message
     */
    public boolean getShowFinalMessage() {
        return showFinalMessage;
    }

    /**
     * Sets the show final message.
     *
     * @param showFinalMessage the show final message
     */
    public void setShowFinalMessage(final boolean showFinalMessage) {

        this.showFinalMessage = showFinalMessage;
    }

    /**
     * Parses the cmd line.
     *
     * @param args the args
     * @return the string[]
     */
    protected String[] ParseCmdLine(final String[] args) {

        return args;
    }

    /**
     * Inits the instance.
     *
     * @param args the args
     * @return the int
     * @throws Exception the exception
     */
    protected int initInstance(final String[] args) throws Exception {
        return 0;
    }

    /**
     * Run instance.
     *
     * @return the int
     * @throws Exception the exception
     */
    protected int runInstance() throws Exception {
        return 0;
    }

    /**
     * Exit instance.
     *
     * @param exit the exit
     * @return the int
     */
    protected int exitInstance(final int exit) {

        if (showElapsed)
            AbstractApplication.messageln("elapsed time: " + DumpUtil.getElapsedTime(elapsedTime));
        if (showFinalMessage)
            if (exit == 0)
                AbstractApplication.out.messageln("finished successfully");
            else
                AbstractApplication.out.messageln("finished with error(s)");
        return exit;
    }

    // /////////////////////////////////////////////////////////////////
    // static

    /**
     * The self.
     */
    static AbstractApplication self = null;

    /**
     * Gets the application.
     *
     * @return the application
     */
    public static AbstractApplication getApplication() {

        return AbstractApplication.self;
    }

    /**
     * The out.
     */
    static AppOutputTarget out = new ConsoleAppOutput();

    /**
     * Gets the output target.
     *
     * @return the output target
     */
    public static AppOutputTarget getOutputTarget() {

        return AbstractApplication.out;
    }

    /**
     * Sets the output target.
     *
     * @param out the out
     */
    protected void setOutputTarget(final AppOutputTarget out) {

        AbstractApplication.out = out;
    }

    /**
     * Exit.
     */
    static public void exit() {
        throw new ExitException();
    }

    /**
     * Run main.
     *
     * @param appClass        the app class
     * @param appVersion      the app version
     * @param args            the args
     * @param appName         the app name
     * @param productName     the product name
     * @param versionPrefix   the version prefix
     * @param companyName     the company name
     * @param versionResource the version resource
     */
    public static void runMain(final String companyName, final String productName, final String appName,
        final String appVersion, final String versionResource, final String versionPrefix, final String appClass,
        final String[] args) {

        final long start = System.currentTimeMillis();

        AbstractApplication.self = null;
        try {
            final Class<?> myClass = Class.forName(appClass);
            // construct the application object
            AbstractApplication.self = (AbstractApplication) Class.forName(appClass).getConstructor().newInstance();
            // init the application properites
            if (appVersion != null)
                AppProperties.init(companyName, productName, appName, appVersion, myClass);
            else
                AppProperties.init(companyName, productName, appName, versionResource, versionPrefix, myClass);

        } catch (final ExitException e) {
            AbstractApplication.errorln("Application failed to initialize: ExitException", e);
            System.exit(1);
        } catch (final RuntimeException e) {
            AbstractApplication.errorln("Application failed to initialize: RuntimeException", e);
            System.exit(1);
        } catch (final Exception e) {
            AbstractApplication.errorln("Application failed to initialize: Exception", e);
            System.exit(1);
        } catch (final Throwable e) {
            AbstractApplication.errorln("Application failed to initialize: Throwable", e);
            System.exit(1);
        }

        int res = 1;
        try {
            res = AbstractApplication.self.initInstance(AbstractApplication.self.ParseCmdLine(args));
            if (res == 0)
                res = AbstractApplication.self.runInstance();
        } catch (final ExitException e) {
            System.exit(1);
        } catch (final RuntimeException e) {
            AbstractApplication.out.errorln("RuntimeException", e);
            e.printStackTrace();
            System.exit(1);
        } catch (final Exception e) {
            AbstractApplication.out.errorln("Exception", e);
            e.printStackTrace();
            System.exit(1);
        } catch (final Throwable e) {
            AbstractApplication.out.errorln("Throwable", e);
            e.printStackTrace();
            System.exit(1);
        }

        try {
            AbstractApplication.self.elapsedTime = System.currentTimeMillis() - start;
            res = AbstractApplication.self.exitInstance(res);
        } catch (final Throwable ignore) {
        }
        if (res != 0)
            System.exit(res);
    }

    /**
     * Gets the message level.
     *
     * @return the message level
     */
    public static int getMessageLevel() {

        return AbstractApplication.out.getMessageLevel();
    }

    /**
     * Sets the message level.
     *
     * @param level the level
     */
    public static void setMessageLevel(final int level) {

        AbstractApplication.out.setMessageLevel(level);
    }

    /**
     * Debugln.
     *
     * @param message the message
     */
    public static void debugln(final String message) {

        AbstractApplication.out.debugln(message);
    }

    /**
     * Verboseln.
     *
     * @param message the message
     */
    public static void verboseln(final String message) {

        AbstractApplication.out.verboseln(message);
    }

    /**
     * Messageln.
     *
     * @param message the message
     */
    public static void messageln(final String message) {

        AbstractApplication.out.messageln(message);
    }

    /**
     * Warningln.
     *
     * @param message the message
     */
    public static void warningln(final String message) {

        AbstractApplication.out.warningln(message);
    }

    /**
     * Warningln.
     *
     * @param message the message
     * @param e       the e
     */
    public static void warningln(final String message, final Throwable e) {

        AbstractApplication.out.warningln(message, e);
    }

    /**
     * Errorln.
     *
     * @param message the message
     */
    public static void errorln(final String message) {

        AbstractApplication.out.errorln(message);
    }

    /**
     * Errorln.
     *
     * @param message the message
     * @param e       the e
     */
    public static void errorln(final String message, final Throwable e) {

        AbstractApplication.out.errorln(message, e);
    }

    /**
     * Formatln.
     *
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     * @return the string
     */
    public static String formatln(final int inputlevel, final String message, final Throwable e) {

        return AbstractApplication.out.formatln(inputlevel, message, e);
    }

    /**
     * Outputln.
     *
     * @param inputlevel the inputlevel
     * @param message    the message
     * @param e          the e
     */
    public static void outputln(final int inputlevel, final String message, final Throwable e) {

        AbstractApplication.out.outputln(inputlevel, message, e);
    }

    /**
     * Write.
     *
     * @param text the text
     */
    public static void write(final String text) {

        AbstractApplication.out.write(text);
    }

    /**
     * Gets the log target.
     *
     * @return the log target
     */
    public OutputTarget getLogTarget() {

        return AbstractApplication.out.getLogTarget();
    }

    /**
     * Sets the log target.
     *
     * @param log the log
     */
    public void setLogTarget(final OutputTarget log) {

        AbstractApplication.out.setLogTarget(log);
    }
}
