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

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

/**
 * The Class XMLReaderFactory.
 */
public class XMLWebRowSetReaderFactory implements DataReaderFactory {

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

        try {
            return new XMLWebRowSetReader(progress, inputStream);
        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
    }
}
