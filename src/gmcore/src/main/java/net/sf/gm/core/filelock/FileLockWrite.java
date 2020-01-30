/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.filelock;

import java.io.*;
import java.nio.channels.FileChannel;

//


/**
 * The Class FileLockWrite.
 */
public class FileLockWrite extends FileLockBase {

    /**
     * The file stream.
     */
    private FileOutputStream fileStream;

    /**
     * The Constructor.
     *
     * @param file the file
     */
    public FileLockWrite(final File file) {

        super(file);
        fileStream = null;
    }

    /**
     * The Constructor.
     *
     * @param filePath the file path
     */
    public FileLockWrite(final String filePath) {

        super(filePath);
        fileStream = null;
    }

    /**
     * Gets the FD.
     *
     * @return the FD
     */
    public FileDescriptor getFD() {

        if (fileStream == null)
            return null;
        FileDescriptor fd = null;
        try {
            fd = fileStream.getFD();
        } catch (final Exception ignore) {
        }
        return fd;
    }

    /**
     * Gets the channel.
     *
     * @return the channel
     */
    @Override
    public FileChannel getChannel() {

        if (fileStream == null)
            return null;
        return fileStream.getChannel();
    }

    /**
     * Gets the file stream.
     *
     * @return the file stream
     */
    public FileOutputStream getFileStream() {
        return fileStream;
    }

    /**
     * Release stream.
     */
    @Override
    protected void releaseStream() {

        fileStream = null;
    }

    /**
     * New stream.
     *
     * @throws FileNotFoundException the file not found exception
     * @throws IOException           the IO exception
     */
    @Override
    protected void newStream() throws FileNotFoundException, IOException {

        fileStream = new FileOutputStream(getFile().getCanonicalPath());
    }

    /**
     * Close stream.
     *
     * @throws IOException the IO exception
     */
    @Override
    protected void closeStream() throws IOException {

        fileStream.close();
    }

    /**
     * Checks for stream.
     *
     * @return true, if has stream
     */
    @Override
    protected boolean hasStream() {

        return fileStream != null;
    }
}
