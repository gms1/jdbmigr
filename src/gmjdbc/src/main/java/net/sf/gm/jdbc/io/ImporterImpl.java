/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.io;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.io.DataReaderFactory;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.jdbc.load.Loader;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

//


/**
 * The Class ImporterBase.
 */
public class ImporterImpl implements Importer {

    /**
     * The factory.
     */
    private final DataReaderFactory factory;

    /**
     * The Constructor.
     *
     * @param factory the factory
     */
    public ImporterImpl(final DataReaderFactory factory) {

        this.factory = factory;
    }

    /**
     * Process.
     *
     * @param inputStream the input stream
     * @param loader      the loader
     * @param progress    the progress
     * @param tableName   the table name
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    public boolean process(final Progress progress, final Loader loader,
        final InputStream inputStream, final String tableName,
        final String schemaName, final String catalogName)
        throws DataIOException {

        loader.startLoading(tableName, schemaName, catalogName);
        final DataReader reader = factory.getInstance(inputStream, progress);
        final long all = reader.readAllData(loader);
        final long written = loader.getRowWrittenCount();
        final long failed = loader.getRowFailedCount();
        final long ignored = loader.getRowIgnoredCount();
        final long unknown = all - written - failed - ignored;
        loader.endLoading();
        return failed <= 0 && unknown <= 0 && all == loader.getAllRowCount();
    }

    /**
     * Process.
     *
     * @param inputPath   the input path
     * @param loader      the loader
     * @param progress    the progress
     * @param tableName   the table name
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    public boolean process(final Progress progress, Loader loader,
        final String inputPath, final String tableName,
        final String schemaName, final String catalogName)
        throws DataIOException {

        final File inputFile = new File(inputPath);
        try {
            return process(progress, loader,
                new BufferedInputStream(new FileInputStream(inputFile)),
                tableName, schemaName, catalogName);
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Process.
     *
     * @param sort      the sort
     * @param tableList the table list
     * @param loader    the load rdh
     * @param delete    the delete
     * @param progress  the progress
     * @return true, if process
     * @throws DataIOException the sql IO exception
     */
    public boolean process(final Progress progress, final Loader loader,
        final TableList tableList, boolean sort,
        boolean delete) throws DataIOException {

        Connection con = loader.getConnection();

        TableList list = tableList;
        if (sort && list.size() > 1) {
            progress.messageln("sorting table list...");
            list = TableList.sortDatabaseSequence(con, list);
            progress.messageln("  sorted");
        }

        try {
            loader.commit(); // end transaction

            progress.setProgress(0);
            progress.setCancel(false);
            final int maxDeleteProgress = delete ? 10 : 0;
            final int maxImportProgress = 100 - maxDeleteProgress;

            final int countTables = list.size();
            int succeededDeletes = 0;
            int succeededImports = 0;

            if (delete) {

                // if the list is sorted in database sequence order
                // ( primary key tables first, foreign key tables last)
                // we have to delete in reverse order
                int countDeletes = 0;
                for (int i = countTables - 1; i >= 0; i--) {
                    final TableDef table = list.get(i);
                    ++countDeletes;
                    if (progress.getCancel()) {
                        loader.rollback(); // end transaction
                        progress.errorln("import canceled");
                        return false;
                    }
                    try {
                        progress.messageln("delete from table '" +
                            table.getFullTableName() + "' ...");
                        final long rows = loader.delete(table.getTable(), table.getSchema(),
                            table.getCatalog());
                        progress.messageln("  " + rows + " row(s) deleted");
                        succeededDeletes++;
                    } catch (final Exception e) {
                        progress.errorln("delete failed", e);
                    }
                    progress.setProgress(countDeletes * maxDeleteProgress / countTables);
                }
            }

            progress.setProgress(maxDeleteProgress);

            // end transaction
            loader.commit();

            int countImports = 0;
            for (final TableDef table : list) {
                ++countImports;
                if (progress.getCancel()) {
                    loader.rollback(); // end transaction
                    progress.errorln("import canceled");
                    return false;
                }
                try {
                    progress.messageln("importing '" + table.getFullTableName() +
                        "' ...");
                    final boolean tableres = this.process(
                        progress, loader, table.getFileName(), table.getTable(),
                        table.getSchema(), table.getCatalog());
                    final long all = loader.getAllRowCount();
                    final long written = loader.getRowWrittenCount();
                    final long failed = loader.getRowFailedCount();
                    final long ignored = loader.getRowIgnoredCount();
                    final long unknown = all - written - failed - ignored;
                    if (tableres && failed == 0) {
                        if (ignored == 0)
                            progress.messageln("  " + written + " row(s) imported");
                        else
                            progress.messageln("  " + written + " row(s) imported, " +
                                ignored + " rows(s) ignored");
                        succeededImports++;
                    } else {
                        progress.errorln(
                            "  " + written + " row(s) imported, " + ignored +
                                " rows(s) ignored, " + failed + " rows(s) failed" +
                                (unknown == 0 ? "" : (", " + unknown + " rows(s) unknown")));
                    }
                } catch (final RuntimeException r) {
                    progress.errorln("import failed: " + r.getClass().getName(), r);
                    r.printStackTrace();
                } catch (final Exception e) {
                    progress.errorln("import failed", e);
                }
                loader.commit(); // end transaction
                progress.setProgress(maxDeleteProgress +
                    countImports * maxImportProgress / countTables);
            }
            progress.setProgress(100);
            loader.commit(); // end transaction
            boolean res = true;
            if (delete && succeededDeletes != countTables) {
                progress.errorln("delete failed for " +
                    (countTables - succeededDeletes) + " of " +
                    countTables + " table(s)");
                res = false;
            }
            if (succeededImports != countTables) {
                progress.errorln("import failed for " +
                    (countTables - succeededImports) + " of " +
                    countTables + " table(s)");
                res = false;
            }
            return res;
        } catch (final SQLException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Process.
     *
     * @param extension the extension
     * @param input     the input
     * @param sort      the sort
     * @param tableList the table list
     * @param loader    the loader
     * @param delete    the delete
     * @param progress  the progress
     * @return true, if process
     * @throws DataIOException the data IO exception
     */
    public boolean process(final Progress progress, File input, String extension,
        final Loader loader, boolean sort, boolean delete,
        File tableList) throws DataIOException {

        Connection con = loader.getConnection();

        TableList allTables =
            TableList.createList(con, tableList, input, extension);
        if (allTables.size() == 0) {
            progress.warningln("no table selected");
            return false;
        }
        if (input != null && !input.isDirectory() && allTables.size() > 1)
            throw new DataIOException("'" + input.getPath() + "' is not a directory");

        return this.process(progress, loader, allTables, sort, delete);
    }

    /**
     * Process.
     *
     * @param extension   the extension
     * @param input       the input
     * @param sort        the sort
     * @param loader      the loader
     * @param delete      the delete
     * @param tableName   the table name
     * @param progress    the progress
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     * @return true, if process
     * @throws DataIOException the data IO exception
     */
    public boolean process(final Progress progress, File input, String extension,
        final Loader loader, boolean sort, boolean delete,
        String catalogName, String schemaName,
        String tableName) throws DataIOException {

        Connection con = loader.getConnection();

        TableList allTables = TableList.createList(con, catalogName, schemaName,
            tableName, input, extension);
        if (allTables.size() == 0) {
            progress.warningln("no table selected");
            return false;
        }
        if (input != null && !input.isDirectory() && allTables.size() > 1)
            throw new DataIOException("'" + input.getPath() + "' is not a directory");
        return this.process(progress, loader, allTables, sort, delete);
    }
}
