/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.app.jdbmigr;

import com.sun.xml.fastinfoset.sax.SAXDocumentSerializer;
import net.sf.gm.app.jdbmigr.common.JDbMigrApplicationBase;
import net.sf.gm.core.app.AbstractApplication;
import net.sf.gm.core.app.AppProgress;
import net.sf.gm.core.cmdline.CmdLineParser;
import net.sf.gm.core.cmdline.FileFilterItem;
import net.sf.gm.core.cmdline.FileFilterList;
import net.sf.gm.core.cmdline.OptionalFlag;
import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.core.ui.Progress;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.OutputStream;

//


/**
 * The Class xmltofinf.
 */
public class xmltofinf extends JDbMigrApplicationBase {

    /**
     * The force.
     */
    private boolean force;

    /**
     * The file list.
     */
    private FileFilterList fileList;

    /**
     * The Constructor.
     */
    public xmltofinf() {
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

        clp.setArgumentDescription(
            "[[sourcefile [destinationfile]]\n[sourcefile... targetdirectory]]", -1,
            -1, null);
        final String[] argv = clp.getOptions(args);

        force = optForce.getValue(false);

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
        fileList = new FileFilterList(args, ".finf", force, false);

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

        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);

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
                final SAXDocumentSerializer documentSerializer =
                    new SAXDocumentSerializer();
                documentSerializer.setOutputStream(os);
                final SAXParser saxParser = saxParserFactory.newSAXParser();
                // saxParser.setProperty(
                // "http://xml.org/sax/properties/lexical-rdh",
                // documentSerializer );
                saxParser.parse(is, documentSerializer);
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

        AbstractApplication.runMain(null, "jdbmigr",
            xmltofinf.class.getSimpleName(), null,
            "gmjdbmigr.version", "gm.jdbmigr.release",
            xmltofinf.class.getName(), args);
    }
}
