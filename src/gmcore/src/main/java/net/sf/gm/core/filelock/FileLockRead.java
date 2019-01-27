/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.filelock;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

//
/**
 * The Class FileLockRead.
 */
public class FileLockRead extends FileLockBase {

  /** The file stream. */
  private FileInputStream fileStream;

  /**
   * The Constructor.
   *
   * @param file the file
   *
   * @throws IOException the IO exception
   */
  public FileLockRead(final File file) throws IOException {

    super(file);
    fileStream = null;
  }

  /**
   * The Constructor.
   *
   * @param filePath the file path
   *
   * @throws IOException the IO exception
   */
  public FileLockRead(final String filePath) throws IOException {

    super(filePath);
    fileStream = null;
  }

  /**
   * Gets the FD.
   *
   * @return the FD
   */
  public FileDescriptor getFD() {

    if (!this.hasStream())
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
  public FileInputStream getFileStream() { return fileStream; }

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

    fileStream = new FileInputStream(getFile().getCanonicalPath());
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

    return fileStream == null ? false : true;
  }
}
