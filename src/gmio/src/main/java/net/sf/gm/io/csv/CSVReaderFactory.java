/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.csv;

import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.io.DataReaderFactory;
import net.sf.gm.core.ui.Progress;

import java.io.InputStream;

/**
 * The Class CSVReaderFactory.
 *
 * @author gms
 */
public class CSVReaderFactory implements DataReaderFactory {

    /**
     * The options.
     */
    private final CSVFormatOptions options;

    /**
     * The Constructor.
     *
     * @param options the options
     */
    public CSVReaderFactory(CSVFormatOptions options) {
        this.options = options;
    }

    /**
     * The Constructor.
     */
    public CSVReaderFactory() {
        this.options = new CSVFormatOptions();
    }

    /**
     * Gets the instance.
     *
     * @param inputStream the input stream
     * @param progress    the progress
     * @return the instance
     */
    public DataReader getInstance(InputStream inputStream, Progress progress) {

        return new CSVReader(progress, inputStream, options);
    }
}
