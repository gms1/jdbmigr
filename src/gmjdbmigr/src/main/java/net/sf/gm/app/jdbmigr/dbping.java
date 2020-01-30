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
import net.sf.gm.core.cmdline.CmdLineParser;
import net.sf.gm.core.cmdline.OptionDelimiter;
import net.sf.gm.core.cmdline.OptionalArgumentString;
import net.sf.gm.core.cmdline.OptionalFlag;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.datasource.DataSourceManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

//


/**
 * The Class dbping.
 */
public class dbping extends JDbMigrApplicationBase {

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
     * The verbose.
     */
    private boolean verbose;

    /**
     * The con.
     */
    private Connection con;

    /**
     * The Constructor.
     */
    public dbping() {

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
        final OptionalFlag optVerbose =
            new OptionalFlag(clp, "v", "verbose", "verbose mode");

        final OptionalArgumentString optUserName =
            new OptionalArgumentString(clp, "U", "username", "database user name");
        final OptionalArgumentString optUserPassword = new OptionalArgumentString(
            clp, "P", "password", "database user password");
        final OptionalArgumentString optUrl = new OptionalArgumentString(
            clp, "u", "url", "url");

        new OptionDelimiter(clp, "");

        clp.setArgumentDescription("datasource", 1, 1, null);
        final String[] argv = clp.getOptions(args);

        verbose = optVerbose.getValue(false);
        userName = optUserName.getValue();
        userPassword = optUserPassword.getValue();
        url = optUrl.getValue();

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
        final DataSource ds = DataSourceManager.lookup(args[0], url);
        con = ds.getConnection(userName, userPassword);
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

        if (con != null)
            if (verbose) {
                AbstractApplication.messageln("connected to:");
                final DatabaseMetaData dm = con.getMetaData();
                AbstractApplication.messageln(
                    "  product: " + dm.getDatabaseProductName() + " " +
                        dm.getDatabaseMajorVersion() + "." + dm.getDatabaseMinorVersion());
                AbstractApplication.messageln("  version: " +
                    dm.getDatabaseProductVersion());
                AbstractApplication.messageln("");
                AbstractApplication.messageln("  driver : " + dm.getDriverName() + " " +
                    dm.getDriverMajorVersion() + "." +
                    dm.getDriverMinorVersion());
                // oracle issue: the 10.2 driver reports 10.2 for the jdbc version
                AbstractApplication.messageln("  jdbc : " + +dm.getJDBCMajorVersion() +
                    "." + dm.getJDBCMinorVersion());

                AbstractApplication.messageln("  user   : " + dm.getUserName());
            } else
                AbstractApplication.messageln("connected");
        return 0;
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

        AbstractApplication.runMain(null, "jdbmigr", dbping.class.getSimpleName(),
            null, "gmjdbmigr.version", "gm.jdbmigr.release",
            dbping.class.getName(), args);
    }
}
