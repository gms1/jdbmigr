/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.filelock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

//
/**
 * The Class FileLockBase.
 */
public abstract class FileLockBase {

  /** The file. */
  private File file;

  /** The lock. */
  private FileLock lock;

  /**
   * The Constructor.
   *
   * @param file the file
   */
  public FileLockBase(final File file) {

    super();
    this.file = file;
    this.lock = null;
  }

  /**
   * The Constructor.
   *
   * @param filePath the file path
   */
  public FileLockBase(final String filePath) {

    super();
    this.file = new File(filePath);
    this.lock = null;
  }

  /**
   * Lock.
   *
   * @param shared   the shared
   * @param waitTime the wait time
   *
   * @return true, if lock
   */
  public boolean lock(final boolean shared, final int waitTime) {

    this.releaseStream();
    lock = null;
    try {
      this.newStream();
      if (waitTime == 0) {
        lock = this.getChannel().lock();
        return lock == null ? false : true;
      }
      if (waitTime < 0) {
        lock = this.getChannel().tryLock();
        return lock == null ? false : true;
      }
      final int sleepTime = 50;
      for (int i = 0; i < waitTime; i++) {
        lock = this.getChannel().tryLock();
        if (lock != null)
          return true;
        Thread.sleep(sleepTime);
        i += sleepTime;
      }
      return false;
    } catch (final Exception ignore) {
    }
    return false;
  }

  /**
   * Release.
   *
   * @return true, if release
   */
  public boolean release() {

    if (!this.hasStream())
      return false;
    if (lock == null)
      return false;
    try {
      lock.release();
      this.closeStream();
      this.releaseStream();
      return true;
    } catch (final Exception ignore) {
    }
    return false;
  }

  /**
   * Checks if is valid.
   *
   * @return true, if is valid
   */
  public boolean isValid() {

    if (lock == null)
      return false;
    return lock.isValid() ? true : false;
  }

  /**
   * Gets the file.
   *
   * @return the file
   */
  public File getFile() { return file; }

  /**
   * Gets the lock.
   *
   * @return the lock
   */
  public FileLock getLock() { return lock; }

  /**
   * Release stream.
   */
  protected abstract void releaseStream();

  /**
   * New stream.
   *
   * @throws FileNotFoundException the file not found exception
   * @throws IOException           the IO exception
   */
  protected abstract void newStream() throws FileNotFoundException, IOException;

  /**
   * Close stream.
   *
   * @throws IOException the IO exception
   */
  protected abstract void closeStream() throws IOException;

  /**
   * Checks for stream.
   *
   * @return true, if has stream
   */
  protected abstract boolean hasStream();

  /**
   * Gets the channel.
   *
   * @return the channel
   */
  protected abstract FileChannel getChannel();
}
