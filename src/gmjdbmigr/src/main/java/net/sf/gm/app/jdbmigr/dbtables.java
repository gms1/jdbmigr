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
import net.sf.gm.core.cmdline.*;
import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.datasource.DataSourceManager;
import net.sf.gm.jdbc.io.TableDef;
import net.sf.gm.jdbc.io.TableList;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;

//


/**
 * The Class dbtables.
 */
public class dbtables extends JDbMigrApplicationBase {

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
     * The catalog pattern.
     */
    private String catalogPattern;

    /**
     * The schema name.
     */
    private String schemaPattern;

    /**
     * The table name.
     */
    private String tablePattern;

    /**
     * The output file.
     */
    private File outputFile;

    /**
     * The verbose.
     */
    private boolean verbose = false;

    /**
     * The con.
     */
    private Connection con;

    /**
     * The Constructor.
     */
    public dbtables() {

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
        final OptionalArgumentFile optFile =
            new OptionalArgumentFile(clp, "o", "outputfile", "output file");
        optFile.setParentMustExist(true);
        final OptionalFlag optVerbose = new OptionalFlag(
            clp, "v", "verbose",
            "verbose mode\nprint additional table info:\ntable type and table comment");
        clp.setArgumentDescription("datasource", 1, 1, null);
        final String[] argv = clp.getOptions(args);

        userName = optUserName.getValue();
        userPassword = optUserPassword.getValue();
        url = optUrl.getValue();

        catalogPattern = optCatalog.getValue("%");
        schemaPattern = optSchema.getValue("%");
        tablePattern = optTable.getValue("%");
        verbose = optVerbose.getValue(false);
        outputFile = optFile.getValue();
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

        AbstractApplication.setMessageLevel(outputFile != null
            ? OutputTarget.LEVEL_WARNING
            : OutputTarget.LEVEL_MESSAGE);
        setShowElapsed(true);
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
        final PrintStream os =
            outputFile == null
                ? System.out
                : new PrintStream(new FileOutputStream(outputFile.getPath()));
        final TableList list = TableList.createList(con, catalogPattern, schemaPattern, tablePattern, null, null);

        for (final TableDef table : list) {
            if (verbose) {
                os.printf("%-40s%-10s%s\n", table.getFullTableName(), table.getTableType() == null ? "" : table.getTableType(),
                    table.getComment() == null ? "" : table.getComment());
            } else {
                os.printf("%-40s\n", table.getFullTableName());
            }
        }
        os.close();
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

        AbstractApplication.runMain(null, "jdbmigr", dbtables.class.getSimpleName(),
            null, "gmjdbmigr.version", "gm.jdbmigr.release",
            dbtables.class.getName(), args);
    }
}
