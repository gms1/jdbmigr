/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import net.sf.gm.core.ui.Progress;

/**
 * The Class DataReaderImpl.
 */
public abstract class DataReaderAbstract implements DataReader {

    /**
     * The progress.
     */
    Progress progress;

    /**
     * The Constructor.
     */
    public DataReaderAbstract() {
        this.progress = null;
    }

    /**
     * The Constructor.
     *
     * @param progress the progress
     */
    public DataReaderAbstract(Progress progress) {
        this.progress = progress;
    }

    /**
     * Read all data.
     *
     * @param writer the writer
     * @return the long
     * @throws DataIOException the data IO exception
     */
    public long readAllData(DataWriter writer) throws DataIOException {

        // the real work will be done by DataWriter
        return writer.writeAllData(this);
    }

    /**
     * Sets the progress.
     *
     * @param progress the progress
     */
    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public Progress getProgress() {
        return this.progress;
    }
}
