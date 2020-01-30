/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import net.sf.gm.core.ui.Progress;

import java.io.IOException;
import java.io.InputStream;

/**
 * The Class DataStreamReader.
 */
public abstract class DataReaderAbstractStream
    extends DataReaderAbstract implements DataReader {

    /**
     * The is.
     */
    final InputStream is;

    /**
     * The row read count.
     */
    int allRowCount;

    /**
     * The Constructor.
     *
     * @param progress the progress
     * @param is       the is
     */
    public DataReaderAbstractStream(final Progress progress,
        final InputStream is) {

        super(progress);
        this.is = is;
        allRowCount = 0;
    }

    /**
     * Open data reading.
     *
     * @return the meta data
     * @throws DataIOException the data IO exception
     */
    public MetaData openDataReading() throws DataIOException {

        allRowCount = 0;
        return null;
    }

    /**
     * Close data reading.
     *
     * @throws DataIOException the data IO exception
     */
    public void closeDataReading() throws DataIOException {

        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the row read count.
     *
     * @return the row read count
     */
    public long getAllRowCount() {
        return allRowCount;
    }

    /**
     * Inc row read count.
     */
    public void incRowReadCount() {
        allRowCount++;
    }

    /**
     * Gets the input stream.
     *
     * @return the input stream
     */
    protected InputStream getInputStream() {
        return is;
    }
}
