/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import net.sf.gm.core.io.DataTypes.rowType;
import net.sf.gm.core.ui.Progress;

/**
 * The Interface DataWriter.
 */
public interface DataWriter {

    /**
     * Open data writing.
     *
     * @param metaData the meta data
     * @throws DataIOException the data IO exception
     */
    void openDataWriting(final MetaData metaData) throws DataIOException;

    /**
     * Close data writing.
     *
     * @throws DataIOException the data IO exception
     */
    void closeDataWriting() throws DataIOException;

    /**
     * Start row writing.
     *
     * @param type the type
     * @throws DataIOException the data IO exception
     */
    void startRowWriting(final rowType type) throws DataIOException;

    /**
     * End row writing.
     *
     * @throws DataIOException the data IO exception
     */
    void endRowWriting() throws DataIOException;

    /**
     * Sets the column value.
     *
     * @param idx    the idx
     * @param reader the reader
     * @throws DataIOException the data IO exception
     */
    void setColumnValue(final int idx, DataReader reader) throws DataIOException;

    /**
     * Gets the row write count.
     *
     * @return row count ( all rows )
     */
    long getAllRowCount();

    /**
     * Gets the row processed count.
     *
     * @return row count ( all rows processed )
     */
    long getRowProcessedCount();

    /**
     * Gets the row ignored count.
     *
     * @return row count ( all rows ignored )
     */
    long getRowIgnoredCount();

    /**
     * Gets the row failed count.
     *
     * @return row count ( all rows not written )
     */
    long getRowFailedCount();

    /**
     * Gets the row written count.
     *
     * @return row count ( all rows written )
     */
    long getRowWrittenCount();

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    Progress getProgress();

    /**
     * Sets the progress.
     *
     * @param progress the progress
     */
    void setProgress(Progress progress);

    /**
     * Write all data.
     *
     * @param reader the reader
     * @return row count
     * @throws DataIOException the data IO exception
     */
    long writeAllData(DataReader reader) throws DataIOException;
}
