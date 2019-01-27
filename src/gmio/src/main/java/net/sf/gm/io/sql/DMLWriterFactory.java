/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.sql;

import java.io.OutputStream;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataWriter;
import net.sf.gm.core.io.DataWriterFactory;
import net.sf.gm.core.ui.Progress;

/**
 * The Class CSVWriterFactory.
 */
public class DMLWriterFactory implements DataWriterFactory {

  /** The doDelete. */
  private boolean doDelete;

  /** The doImport. */
  private boolean doImport;

  /** The doSync. */
  private boolean doSync;

  /** The commitCount. */
  private int commitCount;

  /**
   * The Constructor.
   *
   * @param doImport    the do import
   * @param doDelete    the do delete
   * @param doSync      the do sync
   * @param commitCount the commit count
   */
  public DMLWriterFactory(boolean doDelete, boolean doImport, boolean doSync,
                          int commitCount) {

    this.doDelete = doDelete;
    this.doImport = doImport;
    this.doSync = doSync;
    this.commitCount = commitCount;
  }

  /**
   * Gets the instance.
   *
   * @param outputStream the output stream
   * @param progress     the progress
   *
   * @return the instance
   *
   * @throws DataIOException the data IO exception
   */
  public DataWriter getInstance(OutputStream outputStream, Progress progress)
      throws DataIOException {

    return new DMLWriter(progress, outputStream, doDelete, doImport, doSync,
                         commitCount);
  }
}
