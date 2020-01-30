/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.io.DataReaderFactory;
import net.sf.gm.core.ui.Progress;

import java.io.InputStream;

/**
 * The Class FINFReaderFactory.
 */
public class FINFWebRowSetReaderFactory implements DataReaderFactory {

    /**
     * Gets the instance.
     *
     * @param inputStream the input stream
     * @param progress    the progress
     * @return the instance
     * @throws DataIOException the data IO exception
     */
    public DataReader getInstance(InputStream inputStream, Progress progress)
        throws DataIOException {

        return new FINFWebRowSetReader(progress, inputStream);
    }
}
