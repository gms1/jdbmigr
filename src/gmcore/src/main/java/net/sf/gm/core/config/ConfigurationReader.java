/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

//


/**
 * reader class for properties.
 */
public class ConfigurationReader extends Reader {

    /**
     * The Constructor.
     *
     * @param config the config
     * @param node   the node
     */
    public ConfigurationReader(final Configuration config, final String node) {

        super();
        init(config.getProperties(node));
    }

    /**
     * The Constructor.
     *
     * @param lock   the lock
     * @param config the config
     * @param node   the node
     */
    public ConfigurationReader(final Configuration config, final String node,
        final Object lock) {

        super(lock);
        init(config.getProperties(node));
    }

    /**
     * The Constructor.
     *
     * @param props the props
     */
    public ConfigurationReader(final Properties props) {

        super();
        init(props);
    }

    /**
     * The Constructor.
     *
     * @param lock  the lock
     * @param props the props
     */
    public ConfigurationReader(final Properties props, final Object lock) {

        super(lock);
        final Properties newProps = new Properties(props);
        init(newProps);
    }

    /**
     * Init.
     *
     * @param props the props
     */
    void init(final Properties props) {

        this.props = props;
        this.isOpen = true;
        this.buf = new StringBuilder();
        this.en = (props == null) ? null : props.keys();
    }

    /**
     * The props.
     */
    private Properties props;

    /**
     * The buf.
     */
    private StringBuilder buf;

    /**
     * The is open.
     */
    private boolean isOpen;

    /**
     * The en.
     */
    private Enumeration<Object> en;

    /**
     * Read.
     *
     * @param len  the len
     * @param cbuf the cbuf
     * @param off  the off
     * @return the int
     * @throws IOException the IO exception
     */
    @Override
    public int read(final char[] cbuf, final int off, final int len)
        throws IOException {

        synchronized (lock) {
            if (!isOpen)
                throw new ConfigurationException("PropertyStream is closed");
            if (en == null)
                return -1;
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                ((off + len) > cbuf.length) || ((off + len) < 0))
                throw new IndexOutOfBoundsException();
            else if (len == 0)
                return 0;
            while (len > buf.length() && en.hasMoreElements()) {
                final String key = (String) en.nextElement();
                final String value = props.getProperty(key);
                if (value != null) {
                    buf.append(key);
                    buf.append("=");
                    buf.append(value);
                    buf.append("\n");
                }
                // write properties to buffer
            }
            final int length = buf.length();
            if (length == 0)
                return -1;
            final int n = Math.min(length, len);
            buf.getChars(0, n, cbuf, off);
            buf = buf.delete(0, n);
            return n;
        }
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
    public void reset() throws IOException {

        synchronized (lock) {
            if (!isOpen)
                throw new ConfigurationException("PropertyStream is closed");
            this.buf = new StringBuilder();
            this.en = (props == null) ? null : props.keys();
        }
    }

    /**
     * Ready.
     *
     * @return true, if ready
     * @throws IOException the IO exception
     */
    @Override
    public boolean ready() throws IOException {

        if (!isOpen)
            throw new ConfigurationException("PropertyStream is closed");
        return true;
    }

    /**
     * Close.
     *
     * @throws IOException the IO exception
     */
    @Override
    public void close() throws IOException {

        synchronized (lock) {
            isOpen = false;
        }
    }
}
