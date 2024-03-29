/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.csv;

import net.sf.gm.core.io.DataWriter;
import net.sf.gm.core.io.DataWriterFactory;
import net.sf.gm.core.ui.Progress;

import java.io.OutputStream;

/**
 * The Class CSVWriterFactory.
 */
public class CSVWriterFactory implements DataWriterFactory {

    /**
     * The options.
     */
    final CSVFormatOptions options;

    /**
     * The Constructor.
     *
     * @param options the options
     */
    public CSVWriterFactory(CSVFormatOptions options) {
        this.options = options;
    }

    /**
     * The Constructor.
     */
    public CSVWriterFactory() {
        this.options = new CSVFormatOptions();
    }

    /**
     * Gets the instance.
     *
     * @param outputStream the output stream
     * @param progress     the progress
     * @return the instance
     */
    public DataWriter getInstance(OutputStream outputStream, Progress progress) {

        return new CSVWriter(progress, outputStream, options);
    }
}
