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
import net.sf.gm.core.ui.OutputLogFile;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.io.xml.XMLWebRowSetWriterFactory;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.datasource.DataSourceManager;
import net.sf.gm.jdbc.io.Exporter;
import net.sf.gm.jdbc.io.ExporterImpl;
import net.sf.gm.jdbc.load.Unloader;
import net.sf.gm.jdbc.load.UnloaderImpl;

import javax.sql.DataSource;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;

//


/**
 * The Class xmlexptab.
 */
public class xmlexptab extends JDbMigrApplicationBase {

    /**
     * The user name.
     */
    private String userName;

    /**
     * The user password.
     */
    private String userPassword;

    /**
     * The url
     */
    private String url;

    /**
     * The catalog name.
     */
    private String catalogName;

    /**
     * The schema name.
     */
    private String schemaName;

    /**
     * The table name.
     */
    private String tableName;

    /**
     * The table list.
     */
    private File tableList;

    /**
     * The output.
     */
    private File output;

    /**
     * The force.
     */
    private boolean force;

    /**
     * The log file.
     */
    private File logFile;

    /**
     * The con.
     */
    private Connection con;

    /**
     * The Constructor.
     */
    public xmlexptab() {

        super();
        con = null;
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

        final OptionalArgumentString optUserName =
            new OptionalArgumentString(clp, "U", "username", "database user name");
        final OptionalArgumentString optUserPassword = new OptionalArgumentString(
            clp, "P", "password", "database user password");
        final OptionalArgumentString optUrl = new OptionalArgumentString(
            clp, "u", "url", "url");

        new OptionDelimiter(clp, "");

        final OptionalArgumentString optCatalog =
            new OptionalArgumentString(clp, "c", "catalog", "catalog name pattern");
        final OptionalArgumentString optSchema =
            new OptionalArgumentString(clp, "s", "schema", "schema name pattern");
        final OptionalArgumentString optTable =
            new OptionalArgumentString(clp, "t", "table", "table name pattern");
        final OptionalArgumentFile optTableListFile = new OptionalArgumentFile(
            clp, "", "filelist",
            "file containing a list of tables\nnot allowed in conjuncton with catalog-, schema- or table-pattern");
        optTableListFile.setFileMustExist(true);
        optTableListFile.setMustExist(true);
        final OptionalFlag optForce =
            new OptionalFlag(clp, "f", "force", "force overwrite");
        final OptionalArgumentFile optFile = new OptionalArgumentFile(
            clp, "o", "output", "output file or directory");
        optFile.setParentMustExist(true);
        final OptionalArgumentFile optLogFile =
            new OptionalArgumentFile(clp, "l", "logfile", "log file");
        optLogFile.setParentMustExist(true);

        clp.setArgumentDescription("datasource", 1, 1, null);

        // start the option parsing
        final String[] argv = clp.getOptions(args);

        if (optTableListFile.isSelected() &&
            (optCatalog.isSelected() || optSchema.isSelected() ||
                optTable.isSelected())) {
            AbstractApplication.errorln(
                "option '" + optTableListFile.getLongNames() +
                    "' not allowd in conjunction with catalog-, schema- or table-pattern");
            System.exit(1);
        }

        userName = optUserName.getValue();
        userPassword = optUserPassword.getValue();
        url = optUrl.getValue();

        catalogName = optCatalog.getValue("%");
        schemaName = optSchema.getValue("%");
        tableName = optTable.getValue("%");
        tableList = optTableListFile.getValue();
        output = optFile.getValue();
        logFile = optLogFile.getValue();

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

        setShowElapsed(true);
        setShowFinalMessage(true);
        PrintWriter pw = null;
        if (logFile != null) {
            pw = new PrintWriter(logFile);
            setLogTarget(new OutputLogFile(pw));
        }
        final DataSource ds = DataSourceManager.lookup(args[0], url);
        con = ds.getConnection(userName, userPassword);
        con.setAutoCommit(false);

        return 0;
    }

    /**
     * Run instance.
     *
     * @return the int
     * @throws Exception the exception
     */
    @Override
    protected int runInstance() throws Exception {

        Progress progress = new AppProgress();
        final Unloader unloader = new UnloaderImpl(con);

        final Exporter exp = new ExporterImpl(new XMLWebRowSetWriterFactory());

        boolean res;
        if (tableList == null)
            res = exp.process(progress, output, ".xml", unloader, force, catalogName,
                schemaName, tableName);
        else
            res = exp.process(progress, output, ".xml", unloader, force, tableList);

        unloader.close();
        return res ? 0 : 1;
    }

    /**
     * Exit instance.
     *
     * @param exit the exit
     * @return the int
     */
    @Override
    protected int exitInstance(final int exit) {

        SqlUtil.closeConnection(con);
        super.exitInstance(exit);
        return exit;
    }

    /**
     * The main method.
     *
     * @param args the args
     */
    public static void main(final String[] args) {

        AbstractApplication.runMain(null, "jdbmigr",
            xmlexptab.class.getSimpleName(), null,
            "gmjdbmigr.version", "gm.jdbmigr.release",
            xmlexptab.class.getName(), args);
    }
}
