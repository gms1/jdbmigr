/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.app.jdbmigr;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import net.sf.gm.app.jdbmigr.common.JDbMigrApplicationBase;
import net.sf.gm.core.app.AbstractApplication;
import net.sf.gm.core.app.AppProgress;
import net.sf.gm.core.cmdline.CmdLineParser;
import net.sf.gm.core.cmdline.OptionDelimiter;
import net.sf.gm.core.cmdline.OptionalArgumentFile;
import net.sf.gm.core.cmdline.OptionalArgumentInteger;
import net.sf.gm.core.cmdline.OptionalArgumentString;
import net.sf.gm.core.cmdline.OptionalFlag;
import net.sf.gm.core.ui.OutputLogFile;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.io.xml.XMLWebRowSetReaderFactory;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.datasource.DataSourceManager;
import net.sf.gm.jdbc.io.Importer;
import net.sf.gm.jdbc.io.ImporterImpl;
import net.sf.gm.jdbc.load.Loader;
import net.sf.gm.jdbc.load.LoaderImpl;

//
/**
 * The Class xmlimptab.
 */
public class xmlimptab extends JDbMigrApplicationBase {

  /** The default batch size. */
  private final int defaultBatchSize = 50;

  /** The user name. */
  private String userName;

  /** The user password. */
  private String userPassword;

  /** The url */
  private String url;

  /** The catalog name. */
  private String catalogName;

  /** The schema name. */
  private String schemaName;

  /** The table name. */
  private String tableName;

  /** The table list. */
  private File tableList;

  /** The input. */
  private File input;

  /** The batch size. */
  private int batchSize;

  /** The commit count. */
  private int commitCount;

  /** The doDelete. */
  private boolean doDelete;

  /** The doImport. */
  private boolean doImport;

  /** The doSync. */
  private boolean doSync;

  /** The map columns by column id. */
  private boolean mapColumnsByColumnId;

  /** The map relaxed. */
  private boolean mapRelaxed;

  /** The do not sort. */
  private boolean noSort;

  /** The log file. */
  private File logFile;

  /** The con. */
  private Connection con;

  /**
   * The Constructor.
   */
  public xmlimptab() {

    super();
    con = null;
  }

  /**
   * Parses the cmd line.
   *
   * @param args the args
   *
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

    final OptionalArgumentFile optFile =
        new OptionalArgumentFile(clp, "i", "input", "input file or directory");
    optFile.setMustExist(true);
    final OptionalFlag optDelete =
        new OptionalFlag(clp, "d", "delete", "delete the table content first");
    final OptionalFlag optAll = new OptionalFlag(
        clp, "a", "all",
        "import all, synchronize changes in the document back to the datasource");
    final OptionalFlag optSync = new OptionalFlag(
        clp, "", "sync",
        "synchronize changes in the document back to the datasource");

    final OptionalFlag optMapColumnsByColumnId =
        new OptionalFlag(clp, "", "mapcolid",
                         "map columns by col-id\n(default: map by col-name)");
    final OptionalFlag optMapRelaxed = new OptionalFlag(
        clp, "", "maprelaxed", "skp columns not found in target table");
    final OptionalFlag optNoSort =
        new OptionalFlag(clp, "", "nosort", "no database sequence sort");

    final OptionalArgumentInteger optBatchSize =
        new OptionalArgumentInteger(clp, "b", "batchsize", "batch size");
    final OptionalArgumentInteger optCommitCount =
        new OptionalArgumentInteger(clp, "n", "commitcount", "commit count");

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

    input = optFile.getValue();
    doDelete = optDelete.getValue(false);
    doSync = optAll.getValue(false) || optSync.getValue(false);
    doImport = optAll.getValue(false) || !optSync.getValue(false);

    mapColumnsByColumnId = optMapColumnsByColumnId.getValue(false);
    mapRelaxed = optMapRelaxed.getValue(false);
    noSort = optNoSort.getValue(false);

    commitCount = optCommitCount.getValue(0);
    batchSize = optBatchSize.getValue(defaultBatchSize);
    logFile = optLogFile.getValue();
    return argv;
  }

  /**
   * Inits the instance.
   *
   * @param args the args
   *
   * @return the int
   *
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
    con.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);

    return 0;
  }

  /**
   * Run instance.
   *
   * @return the int
   *
   * @throws Exception the exception
   */
  @Override
  protected int runInstance() throws Exception {

    Progress progress = new AppProgress();
    final Loader loader =
        new LoaderImpl(progress, con, batchSize, commitCount,
                       !mapColumnsByColumnId, mapRelaxed, doImport, doSync);

    final Importer imp = new ImporterImpl(new XMLWebRowSetReaderFactory());

    boolean res;
    if (tableList == null)
      res = imp.process(progress, input, ".xml", loader, !noSort, doDelete,
                        catalogName, schemaName, tableName);
    else
      res = imp.process(progress, input, ".xml", loader, !noSort, doDelete,
                        tableList);
    loader.close();
    con.commit(); // end transaction
    return res ? 0 : 1;
  }

  /**
   * Exit instance.
   *
   * @param exit the exit
   *
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
                                xmlimptab.class.getSimpleName(), null,
                                "gmjdbmigr.version", "gm.jdbmigr.release",
                                xmlimptab.class.getName(), args);
  }
}
