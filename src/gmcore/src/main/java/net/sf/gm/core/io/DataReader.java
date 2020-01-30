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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The Interface DataReader.
 */
public interface DataReader {

    /**
     * Open data reading.
     *
     * @return metadata
     * @throws DataIOException the data IO exception
     */
    MetaData openDataReading() throws DataIOException;

    /**
     * Close data reading.
     *
     * @throws DataIOException the data IO exception
     */
    void closeDataReading() throws DataIOException;

    /**
     * Read next row.
     *
     * @return true, if next row is available
     * @throws DataIOException the data IO exception
     */
    boolean readNextRow() throws DataIOException;

    /**
     * Gets the current row type.
     *
     * @return row type
     */
    rowType getCurrentRowType();

    /**
     * Was column value null.
     *
     * @return true, if the last column value was null
     * @throws DataIOException the data IO exception
     */
    boolean wasColumnValueNull() throws DataIOException;

    /**
     * Gets the column value string.
     *
     * @param idx the idx
     * @return the column value string
     * @throws DataIOException the data IO exception
     */
    String getColumnValueString(int idx) throws DataIOException;

    /**
     * Gets the column value boolean.
     *
     * @param idx the idx
     * @return the column value boolean
     * @throws DataIOException the data IO exception
     */
    boolean getColumnValueBoolean(int idx) throws DataIOException;

    /**
     * Gets the column value short.
     *
     * @param idx the idx
     * @return the column value short
     * @throws DataIOException the data IO exception
     */
    short getColumnValueShort(int idx) throws DataIOException;

    /**
     * Gets the column value int.
     *
     * @param idx the idx
     * @return the column value int
     * @throws DataIOException the data IO exception
     */
    int getColumnValueInt(int idx) throws DataIOException;

    /**
     * Gets the column value long.
     *
     * @param idx the idx
     * @return the column value long
     * @throws DataIOException the data IO exception
     */
    long getColumnValueLong(int idx) throws DataIOException;

    /**
     * Gets the column value double.
     *
     * @param idx the idx
     * @return the column value double
     * @throws DataIOException the data IO exception
     */
    double getColumnValueDouble(int idx) throws DataIOException;

    /**
     * Gets the column value big decimal.
     *
     * @param idx the idx
     * @return the column value big decimal
     * @throws DataIOException the data IO exception
     */
    BigDecimal getColumnValueBigDecimal(int idx) throws DataIOException;

    /**
     * Gets the column value date.
     *
     * @param idx the idx
     * @return the column value date
     * @throws DataIOException the data IO exception
     */
    Date getColumnValueDate(int idx) throws DataIOException;

    /**
     * Gets the column value time.
     *
     * @param idx the idx
     * @return the column value time
     * @throws DataIOException the data IO exception
     */
    Time getColumnValueTime(int idx) throws DataIOException;

    /**
     * Gets the column value timestamp.
     *
     * @param idx the idx
     * @return the column value timestamp
     * @throws DataIOException the data IO exception
     */
    Timestamp getColumnValueTimestamp(int idx) throws DataIOException;

    /**
     * Gets the column value bytes.
     *
     * @param idx the idx
     * @return the column value bytes
     * @throws DataIOException the data IO exception
     */
    byte[] getColumnValueBytes(int idx) throws DataIOException;

    /**
     * Gets the column value binary stream.
     *
     * @param idx the idx
     * @return the column value binary stream
     * @throws DataIOException the data IO exception
     */
    InputStream getColumnValueBinaryStream(int idx) throws DataIOException;

    /**
     * Gets the column value character stream.
     *
     * @param idx the idx
     * @return the column value clob
     * @throws DataIOException the data IO exception
     */
    Reader getColumnValueCharacterStream(int idx) throws DataIOException;

    /**
     * Checks if is update column.
     *
     * @param idx the idx
     * @return true if the current row type is UPDATE and the specified column
     * should be updated
     */
    boolean isUpdateColumn(int idx);

    /**
     * Gets the row read count.
     *
     * @return row count
     */
    long getAllRowCount();

    /**
     * Sets the progress.
     *
     * @param progress the progress
     */
    void setProgress(Progress progress);

    /**
     * Read all data.
     *
     * @param writer the writer
     * @param is     * @param progress *
     * @return row count
     * @throws DataIOException the data IO exception
     */
    long readAllData(DataWriter writer) throws DataIOException;
}
