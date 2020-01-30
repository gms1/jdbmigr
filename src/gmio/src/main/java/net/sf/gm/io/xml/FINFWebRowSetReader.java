/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.ui.Progress;

import java.io.InputStream;

//


/**
 * The Class FINFWebRowSetReader.
 */
public class FINFWebRowSetReader
    extends XMLWebRowSetReaderBase implements DataReader {

    /**
     * The Constructor.
     *
     * @param validate the validate
     * @param progress the progress
     * @param is       the is
     */
    public FINFWebRowSetReader(Progress progress, InputStream is,
        boolean validate) {

        super(progress, is, new StAXDocumentParser(is), validate);
    }

    /**
     * The Constructor.
     *
     * @param progress the progress
     * @param is       the is
     */
    public FINFWebRowSetReader(Progress progress, InputStream is) {

        super(progress, is, new StAXDocumentParser(is), false);
    }
}
