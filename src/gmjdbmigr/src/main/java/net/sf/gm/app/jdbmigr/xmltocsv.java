/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.app.jdbmigr;

import net.sf.gm.app.jdbmigr.common.JDbMigrApplicationBase;
import net.sf.gm.core.app.AbstractApplication;
import net.sf.gm.core.app.AppProgress;
import net.sf.gm.core.cmdline.*;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.io.DataWriter;
import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.io.csv.CSVFormatOptions;
import net.sf.gm.io.csv.CSVWriter;
import net.sf.gm.io.xml.XMLWebRowSetReader;

import java.io.InputStream;
import java.io.OutputStream;

//


/**
 * The Class xmltofinf.
 */
public class xmltocsv extends JDbMigrApplicationBase {

    /**
     * The force.
     */
    private boolean force;

    /**
     * The format options.
     */
    private final CSVFormatOptions formatOptions;

    /**
     * The file list.
     */
    private FileFilterList fileList;

    /**
     * The Constructor.
     */
    public xmltocsv() {

        super();
        formatOptions = new CSVFormatOptions();
    }

    /**
     * Parses the cmd line.
     *
     * @param args the args
     * @return the string[]
     */
    @Override
    protected String[] ParseCmdLine(final String[] args) {

        final CmdLineParser clp = new CmdLineParser();
        final OptionalFlag optHelp = new OptionalFlag(
            clp, "h?", "help", "help mode\nprint this usage information and exit");
        optHelp.forHelpUsage();
        final OptionalFlag optForce =
            new OptionalFlag(clp, "f", "force", "force overwrite");

        new OptionDelimiter(clp, "csv format options:");
        final OptionalArgumentChar optRowDel =
            new OptionalArgumentChar(clp, "", "rowdel", "row delimiter");
        final OptionalArgumentChar optColDel =
            new OptionalArgumentChar(clp, "", "coldel", "column delimiter");
        final OptionalArgumentChar optCharDel = new OptionalArgumentChar(
            clp, "", "chardel", "character delimiter / quote character");
        final OptionalFlag optNoQuote =
            new OptionalFlag(clp, "", "no-quotes", "do not use quoting");
        final OptionalFlag optNoDoubleQuotes = new OptionalFlag(
            clp, "", "no-doublequotes",
            "do not escape the quote character using double quotes");
        final OptionalFlag optNoColumnHeader =
            new OptionalFlag(clp, "", "no-columnheader", "disable column header");

        optRowDel.setUnescape(true);
        optColDel.setUnescape(true);
        optCharDel.setUnescape(true);

        clp.setArgumentDescription(
            "[[sourcefile [destinationfile]]\n[sourcefile... targetdirectory]]", -1,
            -1, null);
        final String[] argv = clp.getOptions(args);

        force = optForce.getValue(false);

        formatOptions.setRowDelimiter(
            optRowDel.getValue(formatOptions.getRowDelimiter()));
        formatOptions.setColDelimiter(
            optColDel.getValue(formatOptions.getColDelimiter()));
        formatOptions.setCharDelimiter(
            optCharDel.getValue(formatOptions.getCharDelimiter()));
        formatOptions.setQuoting(!optNoQuote.getValue(!formatOptions.isQuoting()));
        formatOptions.setDoubleQuotes(
            !optNoDoubleQuotes.getValue(!formatOptions.isDoubleQuotes()));
        formatOptions.setColumnHeader(
            !optNoColumnHeader.getValue(!formatOptions.isColumnHeader()));

        return argv;
    }

    /**
     * Inits the instance.
     *
     * @param args the args
     * @return the int
     * @throws Exception the exception
     */
    @Override
    protected int initInstance(final String[] args) throws Exception {

        setShowElapsed(false);
        fileList = new FileFilterList(args, ".csv", force, false);

        if (fileList.size() == 1 && fileList.get(0).isStdOut())
            AbstractApplication.setMessageLevel(OutputTarget.LEVEL_WARNING);

        return 0;
    }

    /**
     * Run instance.
     *
     * @return the int
     */
    @Override
    protected int runInstance() {

        int failedCount = 0;
        Progress progress = new AppProgress();
        int currentItems = 0;
        int allItems = fileList.size();
        progress.setProgress(0);
        for (FileFilterItem item : fileList) {
            currentItems++;
            try {
                progress.messageln("converting '" + item.getInputPath() + "' ...");
                InputStream is = item.getInputStream();
                OutputStream os = item.getOutputStream();
                DataReader reader = new XMLWebRowSetReader(progress, is);
                DataWriter writer = new CSVWriter(progress, os, formatOptions);
                writer.writeAllData(reader);
                item.close();
                progress.messageln("  '" + item.getOutputPath() + "' created");
            } catch (Exception e) {
                failedCount++;
                item.closeAndDelete();
                progress.errorln("failed: ", e);
            }
            progress.setProgress(currentItems * 100 / allItems);
        }
        if (failedCount > 0)
            progress.errorln("convertion failed for " + failedCount + " of " +
                fileList.size() + " file(s)");
        progress.setProgress(100);
        return failedCount;
    }

    /**
     * Exit instance.
     *
     * @param exit the exit
     * @return the int
     */
    @Override
    protected int exitInstance(final int exit) {

        super.exitInstance(exit);
        fileList = null;
        return exit;
    }

    /**
     * The main method.
     *
     * @param args the args
     */
    public static void main(final String[] args) {

        AbstractApplication.runMain(null, "jdbmigr", xmltocsv.class.getSimpleName(),
            null, "gmjdbmigr.version", "gm.jdbmigr.release",
            xmltocsv.class.getName(), args);
    }
}
