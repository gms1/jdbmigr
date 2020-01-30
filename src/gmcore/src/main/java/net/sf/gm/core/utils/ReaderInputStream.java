/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

//


/**
 * inputstream for reading from java.io.Reader
 */
public class ReaderInputStream extends InputStream {

    /**
     * The reader.
     */
    private Reader reader = null;

    /**
     * The encoding.
     */
    private String encoding = null;

    /**
     * The buffer.
     */
    private byte buffer[];

    /**
     * The startpos.
     */
    private int startpos;

    /**
     * The Constructor.
     *
     * @param reader the reader
     */
    public ReaderInputStream(final Reader reader) {

        super();
        this.reader = reader;
        this.encoding = System.getProperty("file.encoding");
        this.buffer = null;
    }

    /**
     * The Constructor.
     *
     * @param encoding the encoding
     * @param reader   the reader
     */
    public ReaderInputStream(final Reader reader, final String encoding) {

        super();
        this.reader = reader;
        this.encoding = encoding;
        this.buffer = null;
    }

    /**
     * Read.
     *
     * @return the int
     * @throws IOException the IO exception
     */
    @Override
    public int read() throws IOException {

        final byte res[] = new byte[1];
        int n;
        if ((n = this.read(res, 0, 1)) <= 0)
            return n;
        return res[0] < 0 ? 256 + res[0] : res[0];
    }

    /**
     * Read.
     *
     * @param length    the length
     * @param offset    the offset
     * @param outbuffer the outbuffer
     * @return the int
     * @throws IOException the IO exception
     */
    @Override
    public synchronized int read(final byte outbuffer[], final int offset,
        final int length) throws IOException {

        int len = length;
        if (reader == null)
            throw new IOException("ReaderInputStream is closed");
        while (buffer == null) {
            final char cbuf[] = new char[len];
            final int n = reader.read(cbuf);
            if (n < 0)
                return -1;
            if (n > 0) {
                final String s = new String(cbuf, 0, n);
                buffer = encoding == null ? s.getBytes() : s.getBytes(encoding);
                startpos = 0;
            }
        }
        if (len > buffer.length - startpos)
            len = buffer.length - startpos;
        System.arraycopy(buffer, startpos, outbuffer, offset, len);
        if ((startpos += len) >= buffer.length)
            buffer = null;
        return len;
    }

    /**
     * Available.
     *
     * @return the int
     * @throws IOException the IO exception
     */
    @Override
    public synchronized int available() throws IOException {

        if (reader == null)
            throw new IOException("ReaderInputStream is closed");
        if (buffer != null)
            return buffer.length - startpos;
        return reader.ready() ? 1 : 0;
    }

    /**
     * Mark supported.
     *
     * @return true, if mark supported
     */
    @Override
    public boolean markSupported() {

        return false;
    }

    /**
     * Reset.
     *
     * @throws IOException the IO exception
     */
    @Override
    public synchronized void reset() throws IOException {

        if (reader == null)
            throw new IOException("ReaderInputStream is closed");
        buffer = null;
        reader.reset();
    }

    /**
     * Close.
     *
     * @throws IOException the IO exception
     */
    @Override
    public synchronized void close() throws IOException {

        if (reader != null)
            reader.close();
        buffer = null;
        reader = null;
    }
}
