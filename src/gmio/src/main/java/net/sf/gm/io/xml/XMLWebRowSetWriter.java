/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import net.sf.gm.core.ui.Progress;
import net.sf.gm.core.utils.StAXUtil;

import javax.xml.stream.XMLStreamException;
import java.io.OutputStream;

/**
 * The Class XMLWebRowSetWriter.
 */
public class XMLWebRowSetWriter extends XMLWebRowSetWriterBase {

    /**
     * The Constructor.
     *
     * @param os       the os
     * @param progress the progress
     * @throws XMLStreamException the XML stream exception
     */
    public XMLWebRowSetWriter(Progress progress, OutputStream os)
        throws XMLStreamException {

        super(progress, os, StAXUtil.createXMLStreamWriter(os, "UTF-8"));
    }
}
