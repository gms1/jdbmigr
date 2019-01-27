/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.csv;

import java.io.OutputStream;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataWriter;
import net.sf.gm.core.io.DataWriterFactory;
import net.sf.gm.core.ui.Progress;

/**
 * The Class CSVWriterFactory.
 */
public class CSVWriterFactory implements DataWriterFactory {

  /** The options. */
  CSVFormatOptions options;

  /**
   * The Constructor.
   *
   * @param options the options
   */
  public CSVWriterFactory(CSVFormatOptions options) { this.options = options; }

  /**
   * The Constructor.
   */
  public CSVWriterFactory() { this.options = new CSVFormatOptions(); }

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

    return new CSVWriter(progress, outputStream, options);
  }
}
