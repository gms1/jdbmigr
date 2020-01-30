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
import net.sf.gm.io.sql.DMLWriter;
import net.sf.gm.io.xml.XMLWebRowSetReader;

import java.io.InputStream;
import java.io.OutputStream;

//


/**
 * The Class xmltofinf.
 */
public class xmltodml extends JDbMigrApplicationBase {

    /**
     * The force.
     */
    private boolean force;

    /**
     * The doDelete.
     */
    private boolean doDelete;

    /**
     * The doImport.
     */
    private boolean doImport;

    /**
     * The doSync.
     */
    private boolean doSync;

    /**
     * The file list.
     */
    private FileFilterList fileList;

    /**
     * The commitCount.
     */
    private int commitCount;

    /**
     * The Constructor.
     */
    public xmltodml() {
        super();
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
        final OptionalFlag optDelete =
            new OptionalFlag(clp, "d", "delete", "delete the table content first");
        final OptionalFlag optAll = new OptionalFlag(
            clp, "a", "all",
            "import all and synchronize changes in the document back to the datasource");
        final OptionalFlag optSync = new OptionalFlag(
            clp, "", "sync",
            "synchronize changes in the document back to the datasource");
        final OptionalArgumentInteger optCommitCount =
            new OptionalArgumentInteger(clp, "n", "commitcount", "commit count");

        clp.setArgumentDescription(
            "[[sourcefile [destinationfile]]\n[sourcefile... targetdirectory]]", -1,
            -1, null);
        final String[] argv = clp.getOptions(args);

        force = optForce.getValue(false);
        doDelete = optDelete.getValue(false);
        doSync = optAll.getValue(false) || optSync.getValue(false);
        doImport = optAll.getValue(false) || !optSync.getValue(false);
        commitCount = optCommitCount.getValue(0);

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
        fileList = new FileFilterList(args, ".sql", force, false);

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
                DataWriter writer = new DMLWriter(progress, os, doDelete, doImport,
                    doSync, commitCount);
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

        AbstractApplication.runMain(null, "jdbmigr", xmltodml.class.getSimpleName(),
            null, "gmjdbmigr.version", "gm.jdbmigr.release",
            xmltodml.class.getName(), args);
    }
}
