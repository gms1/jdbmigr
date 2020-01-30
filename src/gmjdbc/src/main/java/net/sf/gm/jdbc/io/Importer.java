/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.io;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.jdbc.load.Loader;

import java.io.File;
import java.io.InputStream;

//


/**
 * The Interface Importer.
 */
public interface Importer {

    /**
     * Process.
     *
     * @param commitCount       the commit count
     * @param inputStream       the input stream
     * @param mapColumnsByNames the map columns by names
     * @param loader            the loader
     * @param con               the con
     * @param batchSize         the batch size
     * @param tableName         the table name
     * @param progress          the progress
     * @param schemaName        the schema name
     * @param catalogName       the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Loader loader, InputStream inputStream,
        String tableName, String schemaName, String catalogName)
        throws DataIOException;

    /**
     * Process.
     *
     * @param commitCount       the commit count
     * @param inputPath         the input path
     * @param mapColumnsByNames the map columns by names
     * @param loader            the loader
     * @param con               the con
     * @param batchSize         the batch size
     * @param tableName         the table name
     * @param progress          the progress
     * @param schemaName        the schema name
     * @param catalogName       the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Loader loader, String inputPath,
        String tableName, String schemaName, String catalogName)
        throws DataIOException;

    /**
     * Process.
     *
     * @param list              the list
     * @param commitCount       the commit count
     * @param sort              the sort
     * @param mapColumnsByNames the map columns by names
     * @param loader            the loader
     * @param delete            the delete
     * @param con               the con
     * @param batchSize         the batch size
     * @param progress          the progress
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Loader loader, TableList list,
        boolean sort, boolean delete) throws DataIOException;

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
     * @return true if succeeded, false if failed
     * @throws DataIOException the data IO exception
     */
    boolean process(final Progress progress, File input, String extension,
        final Loader loader, boolean sort, boolean delete,
        File tableList) throws DataIOException;

    /**
     * Process.
     *
     * @param extension   the extension
     * @param input       the input
     * @param sort        the sort
     * @param loader      the loader
     * @param delete      the delete
     * @param progress    the progress
     * @param tableName   the table name
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the data IO exception
     */
    boolean process(final Progress progress, File input, String extension,
        final Loader loader, boolean sort, boolean delete,
        String catalogName, String schemaName,
        String tableName) throws DataIOException;
}
