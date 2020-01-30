/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.ui;

import java.io.Writer;

//


/**
 * The Class OutputLogFile.
 */
public class OutputLogFile extends OutputWriter {

    /**
     * The Constructor.
     */
    public OutputLogFile() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param writer the writer
     */
    public OutputLogFile(final Writer writer) {
        super(writer);
    }
}
