/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataWriter;
import net.sf.gm.core.io.DataWriterFactory;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.core.utils.FileUtil;
import net.sf.gm.core.utils.StreamUtil;
import net.sf.gm.jdbc.load.Unloader;

/**
 * The Class ExporterImpl.
 */
public class ExporterImpl implements Exporter {

  /** The factory. */
  private final DataWriterFactory factory;

  /**
   * The Constructor.
   *
   * @param factory the factory
   */
  public ExporterImpl(DataWriterFactory factory) { this.factory = factory; }

  /**
   * Process.
   *
   * @param unloader      the unloader
   * @param tableName     the table name
   * @param progress      the progress
   * @param outputFile    the output file
   * @param schemaName    the schema name
   * @param statementText the statement text
   * @param catalogName   the catalog name
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the sql IO exception
   */
  public boolean process(final Progress progress, final Unloader unloader,
                         final File outputFile, final String statementText,
                         final String tableName, final String schemaName,
                         final String catalogName) throws DataIOException {

    File tmpFile = null;
    OutputStream outputStream = null;
    boolean res = false;
    try {
      if (outputFile.isFile() && outputFile.exists())
        if (!outputFile.delete())
          throw new DataIOException("delete file '" + outputFile.getPath() +
                                    "' failed");

      File outputDirectory = outputFile.getParentFile();
      if (outputDirectory == null)
        outputDirectory = new File(".");
      if (!outputDirectory.exists())
        outputDirectory.mkdir();
      tmpFile = File.createTempFile(".exp", ".tmp", outputDirectory);
      outputStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
    } catch (final IOException e) {
      throw new DataIOException(e);
    }

    try {
      res = process(progress, unloader, outputStream, statementText, tableName,
                    schemaName, catalogName);
      if (!tmpFile.renameTo(outputFile))
        throw new DataIOException("failed to rename output file from '" +
                                  tmpFile.getPath() + "' to '" + outputFile +
                                  "'");
      return res;
    } finally {
      StreamUtil.closeOutputStream(outputStream);
      FileUtil.delete(tmpFile);
    }
  }

  /**
   * Process.
   *
   * @param unloader      the unloader
   * @param progress      the progress
   * @param outputFile    the output file
   * @param statementText the statement text
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the sql IO exception
   */
  public boolean process(final Progress progress, final Unloader unloader,
                         final File outputFile, final String statementText)
      throws DataIOException {

    return process(progress, unloader, outputFile, statementText, "{TABLE}",
                   "{SCHEMA}", "{CATALOG}");
  }

  /**
   * Process.
   *
   * @param unloader      the unloader
   * @param outputStream  the output stream
   * @param progress      the progress
   * @param statementText the statement text
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the sql IO exception
   */
  public boolean process(final Progress progress, final Unloader unloader,
                         final OutputStream outputStream,
                         final String statementText) throws DataIOException {

    return process(progress, unloader, outputStream, statementText, "{TABLE}",
                   "{SCHEMA}", "{CATALOG}");
  }

  /**
   * Process.
   *
   * @param unloader      the unloader
   * @param outputStream  the output stream
   * @param tableName     the table name
   * @param progress      the progress
   * @param schemaName    the schema name
   * @param statementText the statement text
   * @param catalogName   the catalog name
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the sql IO exception
   */
  public boolean process(final Progress progress, final Unloader unloader,
                         final OutputStream outputStream,
                         final String statementText, final String tableName,
                         final String schemaName, final String catalogName)
      throws DataIOException {

    try {
      unloader.setProgress(progress);
      unloader.startUnLoading(tableName, schemaName, catalogName,
                              statementText);
      final DataWriter writer = factory.getInstance(outputStream, progress);
      writer.writeAllData(unloader);
      unloader.endUnLoading();
      return true;
    } catch (final SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Process.
   *
   * @param list      the list
   * @param unloader  the unload handler
   * @param overwrite the overwrite
   * @param progress  the progress
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the sql IO exception
   */
  public boolean process(final Progress progress, final Unloader unloader,
                         final TableList list, final boolean overwrite)
      throws DataIOException {

    try {
      progress.setProgress(0);
      progress.setCancel(false);
      final int countTables = list.size();
      final int currentTables = 0;

      // test if output files exist
      for (final TableDef table : list) {
        if (progress.getCancel()) {
          progress.errorln("export canceled");
          return false;
        }
        final File outputFile = new File(table.getFileName());
        if (outputFile.isFile() && outputFile.exists())
          if (overwrite) {
            progress.messageln("deleting file '" + table.getFileName() + "'");
            if (!FileUtil.delete(outputFile)) {
              progress.errorln("delete failed");
              progress.errorln("export canceled");
              return false;
            }
          } else {
            progress.errorln("file '" + table.getFileName() + "' exist");
            progress.errorln("export canceled");
            return false;
          }
      }

      // export
      int successCount = 0;
      for (final TableDef table : list) {
        try {
          if (progress.getCancel()) {
            progress.errorln("export canceled");
            return false;
          }
          progress.messageln("exporting '" + table.getFullTableName() +
                             "' ...");
          final StringBuilder tabSelect = new StringBuilder();
          tabSelect.append("select * from ");
          tabSelect.append(table.getFullTableName());
          if (table.getWhereClause() != null) {
            tabSelect.append(" ");
            tabSelect.append(table.getWhereClause());
          }
          final boolean tableres =
              this.process(progress, unloader, new File(table.getFileName()),
                           tabSelect.toString(), table.getTable(),
                           table.getSchema(), table.getCatalog());
          final long all = unloader.getAllRowCount();
          progress.setProgress(currentTables * 100 / countTables);
          if (tableres) {
            progress.messageln("  " + all + " row(s) exported");
            successCount++;
          } else {
            progress.errorln("export failed");
          }
        } catch (final Exception e) {
          progress.errorln("export failed", e);
        }
      }

      progress.setProgress(100);
      if (successCount != countTables) {
        progress.errorln("export failed for " + (countTables - successCount) +
                         " of " + countTables + " table(s)");
        return false;
      }
      return true;
    } catch (final Exception e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Process.
   *
   * @param extension the extension
   * @param unloader  the unloader
   * @param output    the output
   * @param overwrite the overwrite
   * @param tableList the table list
   * @param progress  the progress
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the data IO exception
   */
  public boolean process(final Progress progress, File output, String extension,
                         final Unloader unloader, boolean overwrite,
                         File tableList) throws DataIOException {

    Connection con = unloader.getConnection();
    TableList allTables =
        TableList.createList(con, tableList, output, extension);
    if (allTables.size() == 0) {
      progress.warningln("no table selected");
      return false;
    }
    if (output != null) {
      if (allTables.size() > 1 && !output.isDirectory())
        throw new DataIOException("'" + output.getPath() +
                                  "' is not a directory");
      if (allTables.size() == 1 && !output.isDirectory()) {
        final File parentDir = output.getParentFile();
        if (parentDir == null || !parentDir.isDirectory())
          throw new DataIOException("parent directory for '" +
                                    output.getPath() + "' not found");
      }
    }
    return this.process(progress, unloader, allTables, overwrite);
  }

  /**
   * Process.
   *
   * @param extension   the extension
   * @param unloader    the unloader
   * @param output      the output
   * @param overwrite   the overwrite
   * @param tableName   the table name
   * @param progress    the progress
   * @param schemaName  the schema name
   * @param catalogName the catalog name
   *
   * @return true if succeeded, false if failed
   *
   * @throws DataIOException the data IO exception
   */
  public boolean process(final Progress progress, File output, String extension,
                         final Unloader unloader, boolean overwrite,
                         String catalogName, String schemaName,
                         String tableName) throws DataIOException {

    Connection con = unloader.getConnection();
    TableList allTables = TableList.createList(con, catalogName, schemaName,
                                               tableName, output, extension);
    if (allTables.size() == 0) {
      progress.warningln("no table selected");
      return false;
    }
    if (output != null) {
      if (allTables.size() > 1 && !output.isDirectory())
        throw new DataIOException("'" + output.getPath() +
                                  "' is not a directory");
      if (allTables.size() == 1 && !output.isDirectory()) {
        final File parentDir = output.getParentFile();
        if (parentDir == null || !parentDir.isDirectory())
          throw new DataIOException("parent directory for '" +
                                    output.getPath() + "' not found");
      }
    }
    return this.process(progress, unloader, allTables, overwrite);
  }
}
