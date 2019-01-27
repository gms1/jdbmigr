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

import javax.sql.DataSource;

import net.sf.gm.app.jdbmigr.common.JDbMigrApplicationBase;
import net.sf.gm.core.app.AbstractApplication;
import net.sf.gm.core.cmdline.CmdLineParser;
import net.sf.gm.core.cmdline.OptionDelimiter;
import net.sf.gm.core.cmdline.OptionalArgumentChar;
import net.sf.gm.core.cmdline.OptionalArgumentFile;
import net.sf.gm.core.cmdline.OptionalArgumentString;
import net.sf.gm.core.cmdline.OptionalFlag;
import net.sf.gm.core.ui.OutputLogFile;
import net.sf.gm.core.ui.OutputTarget;
import net.sf.gm.io.csv.CSVFormatOptions;
import net.sf.gm.io.csv.CSVWriterFactory;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.datasource.DataSourceManager;
import net.sf.gm.jdbc.io.Exporter;
import net.sf.gm.jdbc.io.ExporterImpl;
import net.sf.gm.jdbc.load.Unloader;
import net.sf.gm.jdbc.load.UnloaderImpl;

//
/**
 * The Class csvexpqry.
 */
public class csvexpqry extends JDbMigrApplicationBase {

  /** The output file. */
  private File outputFile;

  /** The user name. */
  private String userName;

  /** The user password. */
  private String userPassword;

  /** The url */
  private String url;

  /** The format options. */
  private CSVFormatOptions formatOptions;

  /** The log file. */
  private File logFile;

  /** The connection. */
  private Connection con;

  /** The query string. */
  private String queryString;

  /**
   * The Constructor.
   */
  public csvexpqry() {

    super();
    con = null;
    formatOptions = new CSVFormatOptions();
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

    final OptionalArgumentFile optFile =
        new OptionalArgumentFile(clp, "o", "outputfile", "output file");
    optFile.setParentMustExist(true);
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

    final OptionalArgumentFile optLogFile =
        new OptionalArgumentFile(clp, "l", "logfile", "log file");
    optLogFile.setParentMustExist(true);

    clp.setArgumentDescription("datasource \"select-statement\"", 2, 2, null);

    final String[] argv = clp.getOptions(args);

    userName = optUserName.getValue();
    userPassword = optUserPassword.getValue();
    url = optUrl.getValue();

    outputFile = optFile.getValue();
    logFile = optLogFile.getValue();

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
   *
   * @return the int
   *
   * @throws Exception the exception
   */
  @Override
  protected int initInstance(final String[] args) throws Exception {

    AbstractApplication.setMessageLevel(outputFile == null
                                            ? OutputTarget.LEVEL_WARNING
                                            : OutputTarget.LEVEL_MESSAGE);
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
    queryString = args[1];
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

    final Exporter exp = new ExporterImpl(new CSVWriterFactory(formatOptions));
    final Unloader unloader = new UnloaderImpl(con);
    boolean res;
    if (outputFile != null)
      res = exp.process(null, unloader, outputFile, queryString);
    else
      res = exp.process(null, unloader, System.out, queryString);
    final long rowCount = unloader.getAllRowCount();
    if (res)
      AbstractApplication.messageln("  exported " + rowCount + " row(s)");
    unloader.close();
    return 0;
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
                                csvexpqry.class.getSimpleName(), null,
                                "gmjdbmigr.version", "gm.jdbmigr.release",
                                csvexpqry.class.getName(), args);
  }
}
