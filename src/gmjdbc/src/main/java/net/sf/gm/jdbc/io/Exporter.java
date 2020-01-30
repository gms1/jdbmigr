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
import net.sf.gm.jdbc.load.Unloader;

import java.io.File;
import java.io.OutputStream;

//


/**
 * The Interface Exporter.
 */
public interface Exporter {

    /**
     * Process.
     *
     * @param unloader      the unloader
     * @param progress      the progress
     * @param outputfile    the output file
     * @param statementText the statement text
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Unloader unloader, File outputfile,
        String statementText) throws DataIOException;

    /**
     * Process.
     *
     * @param unloader      the unloader
     * @param tableName     the table name
     * @param progress      the progress
     * @param schemaName    the schema name
     * @param outputfile    the output file
     * @param statementText the statement text
     * @param catalogName   the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Unloader unloader, File outputfile,
        String statementText, String tableName, String schemaName,
        String catalogName) throws DataIOException;

    /**
     * Process.
     *
     * @param unloader      the unloader
     * @param outputStream  the output stream
     * @param progress      the progress
     * @param statementText the statement text
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Unloader unloader,
        OutputStream outputStream, String statementText)
        throws DataIOException;

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
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Unloader unloader,
        OutputStream outputStream, String statementText,
        String tableName, String schemaName, String catalogName)
        throws DataIOException;

    /**
     * Process.
     *
     * @param list      the list
     * @param unloader  the unloader
     * @param overwrite the overwrite
     * @param con       the con
     * @param progress  the progress
     * @return true if succeeded, false if failed
     * @throws DataIOException the sql IO exception
     */
    boolean process(Progress progress, Unloader unloader, TableList list,
        boolean overwrite) throws DataIOException;

    /**
     * Process.
     *
     * @param extension the extension
     * @param unloader  the unloader
     * @param overwrite the overwrite
     * @param output    the output
     * @param tableList the table list
     * @param progress  the progress
     * @return true if succeeded, false if failed
     * @throws DataIOException the data IO exception
     */
    boolean process(final Progress progress, File output, String extension,
        final Unloader unloader, boolean overwrite,
        File tableList) throws DataIOException;

    /**
     * Process.
     *
     * @param extension   the extension
     * @param unloader    the unloader
     * @param overwrite   the overwrite
     * @param output      the output
     * @param progress    the progress
     * @param tableName   the table name
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     * @return true if succeeded, false if failed
     * @throws DataIOException the data IO exception
     */
    boolean process(final Progress progress, File output, String extension,
        final Unloader unloader, boolean overwrite,
        String catalogName, String schemaName,
        String tableName) throws DataIOException;
}
