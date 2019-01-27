/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import java.io.InputStream;

import net.sf.gm.core.ui.Progress;

/**
 * The Interface DataReaderFactory.
 */
public interface DataReaderFactory {

  /**
   * Gets the instance.
   *
   * @param inputStream the input stream
   * @param progress    the progress
   *
   * @return the instance
   *
   * @throws DataIOException the data IO exception
   */
  public DataReader getInstance(InputStream inputStream, Progress progress)
      throws DataIOException;
}
