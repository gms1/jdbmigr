/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.csv;

import java.io.InputStream;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.io.DataReaderFactory;
import net.sf.gm.core.ui.Progress;

/**
 * The Class CSVReaderFactory.
 *
 * @author gms
 */
public class CSVReaderFactory implements DataReaderFactory {

  /** The options. */
  private CSVFormatOptions options;

  /**
   * The Constructor.
   *
   * @param options the options
   */
  public CSVReaderFactory(CSVFormatOptions options) { this.options = options; }

  /**
   * The Constructor.
   */
  public CSVReaderFactory() { this.options = new CSVFormatOptions(); }

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
      throws DataIOException {

    return new CSVReader(progress, inputStream, options);
  }
}
