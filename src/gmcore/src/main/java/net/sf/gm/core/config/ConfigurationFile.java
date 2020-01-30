/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import net.sf.gm.core.filelock.FileLockRead;
import net.sf.gm.core.utils.LocationUtil;
import net.sf.gm.core.utils.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

//


/**
 * The Class ConfigurationFile.
 */
public class ConfigurationFile extends ConfigurationBase {

    /**
     * The log.
     */
    static final Logger log = Logger.getLogger(ConfigurationFile.class.getName());

    /**
     * The file.
     */
    private final File file;

    /**
     * The Constructor.
     *
     * @param file the file
     */
    public ConfigurationFile(final File file) {

        super(LocationUtil.toUrl(file));
        this.file = file;
    }

    /**
     * The Constructor.
     *
     * @param url the url
     */
    public ConfigurationFile(final URL url) {

        super(url);
        this.file = LocationUtil.toFile(url);
    }

    /**
     * The Constructor.
     *
     * @param file                 the file
     * @param defaultConfiguration the default configuration
     */
    public ConfigurationFile(final File file, final Configuration defaultConfiguration) {

        super(LocationUtil.toUrl(file), defaultConfiguration);
        this.file = file;
    }

    /**
     * The Constructor.
     *
     * @param url                  the url
     * @param defaultConfiguration the default configuration
     */
    public ConfigurationFile(final URL url, final Configuration defaultConfiguration) {

        super(url, defaultConfiguration);
        this.file = LocationUtil.toFile(url);
    }

    /**
     * Load.
     *
     * @param data the data
     * @return true, if load
     */
    @Override
    protected boolean load(final ConfigurationData data) {

        return (file == null) ? loadFromURL(data) : loadFromFile(data);
    }

    /**
     * Load from file.
     *
     * @param data the data
     * @return true, if load from file
     */
    protected boolean loadFromFile(final ConfigurationData data) {

        if (!file.exists() || !file.isFile())
            return false;
        FileLockRead lockFile = null;
        try {
            lockFile = new FileLockRead(file);
            lockFile.lock(false, 0);
            final StringBuilder newComment = new StringBuilder();
            final ConfigurationXMLReader reader = new ConfigurationXMLReader();
            return reader.process(data, lockFile.getFileStream(), newComment);
        } catch (final Exception e) {
            ConfigurationFile.log
                .warning("load cfgfile failed \"" + file + "\" failed:" + StringUtil.getShortExceptionMessage(e));
        } finally {
            if (lockFile != null)
                lockFile.release();
        }
        return false;
    }

    /**
     * Load from URL.
     *
     * @param data the data
     * @return true, if load from URL
     */
    protected boolean loadFromURL(final ConfigurationData data) {

        if (getURL() == null)
            return false;
        try {
            final StringBuilder newComment = new StringBuilder();
            final InputStream is = getURL().openStream();
            final ConfigurationXMLReader reader = new ConfigurationXMLReader();
            return reader.process(data, is, newComment);
        } catch (final Exception e) {
            ConfigurationFile.log
                .warning("load cfgfile \"" + getURL() + "\" failed:" + StringUtil.getShortExceptionMessage(e));
        }
        return false;
    }
}
