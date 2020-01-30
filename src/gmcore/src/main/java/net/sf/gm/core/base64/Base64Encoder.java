/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

//


/**
 * The Class Base64Encoder.
 */
public class Base64Encoder extends Base64Base {

    /**
     * The is.
     */
    private final InputStream is;

    /**
     * The pw.
     */
    private final OutputStreamWriter pw;

    /**
     * The current line size.
     */
    private int currentLineSize;

    /**
     * The Constructor.
     *
     * @param os the os
     * @param is the is
     */
    public Base64Encoder(final InputStream is, final OutputStream os) {

        super();
        this.is = is;
        pw = new OutputStreamWriter(os);
        currentLineSize = 0;
    }

    /**
     * Process.
     *
     * @throws IOException the IO exception
     */
    public void process() throws IOException {

        int readLen;
        final byte[] buf = new byte[getBufferSize()];
        while ((readLen = is.read(buf)) != -1) {
            pw.write(Base64Base.encode(buf, 0, readLen, currentLineSize));
            currentLineSize =
                (currentLineSize + readLen) % Base64Base.DECODED_CHUNK_SIZE;
        }
    }
}
