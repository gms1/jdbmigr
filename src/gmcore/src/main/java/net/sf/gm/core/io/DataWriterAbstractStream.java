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

import java.io.IOException;
import java.io.OutputStream;

/**
 * The Class DataStreamWriter.
 */
public abstract class DataWriterAbstractStream
    extends DataWriterAbstract implements DataWriter {

    /**
     * The os.
     */
    private OutputStream os;

    /**
     * The meta data.
     */
    private MetaData metaData;

    /**
     * The col count.
     */
    private int colCount;

    /**
     * The row write count.
     */
    protected long allRowCount;

    /**
     * The row write count.
     */
    private long processedRowCount;

    /**
     * The current row type.
     */
    private rowType currentRowType;

    /**
     * The Constructor.
     *
     * @param os       the os
     * @param progress the progress
     */
    public DataWriterAbstractStream(Progress progress, OutputStream os) {

        super(progress);
        this.os = os;
        metaData = null;
        allRowCount = 0;
        processedRowCount = 0;
    }

    /**
     * Open data writing.
     *
     * @param metaData the meta data
     * @throws DataIOException the data IO exception
     */
    public void openDataWriting(final MetaData metaData) throws DataIOException {

        this.metaData = metaData;
        colCount = 0;
        allRowCount = 0;
        processedRowCount = 0;
        colCount = metaData.getColumnCount();
    }

    /**
     * Close data writing.
     *
     * @throws DataIOException the data IO exception
     */
    public void closeDataWriting() throws DataIOException {

        metaData = null;
        try {
            os.close();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Start row writing.
     *
     * @param type the type
     * @throws DataIOException the data IO exception
     */
    public void startRowWriting(final rowType type) throws DataIOException {

        currentRowType = type;
        allRowCount++;
    }

    /**
     * Gets the current row type.
     *
     * @return the current row type
     */
    protected rowType getCurrentRowType() {
        return currentRowType;
    }

    /**
     * End row writing.
     *
     * @throws DataIOException the data IO exception
     */
    public void endRowWriting() throws DataIOException {
        processedRowCount++;
    }

    /**
     * Gets the output stream.
     *
     * @return the output stream
     */
    protected OutputStream getOutputStream() {
        return os;
    }

    /**
     * Gets the meta data.
     *
     * @return the meta data
     */
    protected MetaData getMetaData() {
        return metaData;
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    protected int getColumnCount() {
        return colCount;
    }

    /**
     * Gets the row failed count.
     *
     * @return the row failed count
     */
    public long getRowFailedCount() {
        return 0;
    }

    /**
     * Gets the row ignored count.
     *
     * @return the row ignored count
     */
    public long getRowIgnoredCount() {
        return 0;
    }

    /**
     * Gets the row processed count.
     *
     * @return the row processed count
     */
    public long getRowProcessedCount() {
        return processedRowCount;
    }

    /**
     * Gets the row written count.
     *
     * @return the row written count
     */
    public long getRowWrittenCount() {
        return processedRowCount;
    }

    /**
     * Gets the row write count.
     *
     * @return the row write count
     */
    public long getAllRowCount() {
        return allRowCount;
    }
}
