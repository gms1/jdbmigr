/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import net.sf.gm.core.utils.FileUtil;
import net.sf.gm.core.utils.StreamUtil;

import java.io.*;

/**
 * The Class FileFilterItem.
 */
public class FileFilterItem {

    /**
     * The src file.
     */
    private File srcFile;

    /**
     * The trg file.
     */
    private File trgFile;

    /**
     * The tmp file.
     */
    private File tmpFile;

    /**
     * The is.
     */
    private InputStream is;

    /**
     * The os.
     */
    private OutputStream os;

    /**
     * The is std in.
     */
    private boolean isStdIn;

    /**
     * The is std out.
     */
    private boolean isStdOut;

    /**
     * The Constructor.
     *
     * @param trgFile the trg file
     * @param srcFile the src file
     */
    public FileFilterItem(final File srcFile, final File trgFile) {

        this.srcFile = srcFile;
        this.trgFile = trgFile;
        this.isStdIn = srcFile == null ? true : false;
        this.isStdOut = trgFile == null ? true : false;
        this.tmpFile = null;
        this.is = null;
        this.os = null;
    }

    /**
     * Gets the input stream.
     *
     * @return inputstream from source file
     * @throws FileNotFoundException the file not found exception
     */
    public InputStream getInputStream() throws FileNotFoundException {

        if (isStdIn)
            return new BufferedInputStream(System.in);
        if (is != null)
            return new BufferedInputStream(is);
        is = new BufferedInputStream(new FileInputStream(srcFile));
        return is;
    }

    /**
     * Gets the output stream.
     *
     * @return outputstream to target file
     * @throws Exception the exception
     */
    public OutputStream getOutputStream() throws Exception {

        if (isStdOut)
            return new BufferedOutputStream(System.out);
        if (os != null)
            return new BufferedOutputStream(os);
        File outputDirectory = trgFile.getParentFile();
        if (outputDirectory == null)
            outputDirectory = new File(".");
        if (!outputDirectory.exists())
            outputDirectory.mkdir();
        tmpFile = File.createTempFile(".flt", ".tmp", outputDirectory);
        os = new BufferedOutputStream(new FileOutputStream(tmpFile));
        return os;
    }

    /**
     * Checks if is std in.
     *
     * @return true, if input is stdin
     */
    public boolean isStdIn() {
        return isStdIn;
    }

    /**
     * Checks if is std out.
     *
     * @return true, if output goes to stdout
     */
    public boolean isStdOut() {
        return isStdOut;
    }

    /**
     * Gets the input path.
     *
     * @return input path
     */
    public String getInputPath() {

        return isStdIn ? "(stdin)" : srcFile.getPath();
    }

    /**
     * Gets the output path.
     *
     * @return output path
     */
    public String getOutputPath() {

        return isStdOut ? "(stdout)" : trgFile.getPath();
    }

    /**
     * Close.
     *
     * @throws IOException the IO exception
     */
    public void close() throws IOException {

        try {
            if (os != null)
                StreamUtil.closeOutputStream(os);
            if (tmpFile != null)
                if (!tmpFile.renameTo(trgFile))
                    throw new IOException("failed to rename temporary file to '" + trgFile + "'");
            tmpFile = null;
        } finally {
            if (tmpFile != null)
                FileUtil.delete(tmpFile);
        }
    }

    /**
     * Close and delete.
     */
    public void closeAndDelete() {

        if (os != null)
            StreamUtil.closeOutputStream(os);
        if (tmpFile != null)
            FileUtil.delete(tmpFile);
        if (trgFile != null)
            FileUtil.delete(tmpFile);
        if (is != null)
            StreamUtil.closeInputStream(is);
    }

    /**
     * Finalize.
     *
     * @throws Throwable the throwable
     */
    @Override
    protected void finalize() throws Throwable {

        if (tmpFile != null)
            FileUtil.delete(tmpFile);
    }
}
