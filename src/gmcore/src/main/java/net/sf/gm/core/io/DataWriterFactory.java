/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import net.sf.gm.core.ui.Progress;

import java.io.OutputStream;

/**
 * The Interface DataWriterFactory.
 */
public interface DataWriterFactory {

    /**
     * Gets the instance.
     *
     * @param outputStream the output stream
     * @param progress     the progress
     * @return the instance
     * @throws DataIOException the data IO exception
     */
    DataWriter getInstance(OutputStream outputStream, Progress progress)
        throws DataIOException;
}
