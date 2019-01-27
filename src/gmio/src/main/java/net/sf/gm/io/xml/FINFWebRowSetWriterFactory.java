/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import java.io.OutputStream;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataWriter;
import net.sf.gm.core.io.DataWriterFactory;
import net.sf.gm.core.ui.Progress;

/**
 * The Class CSVWriterFactory.
 */
public class FINFWebRowSetWriterFactory implements DataWriterFactory {

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

    return new FINFWebRowSetWriter(progress, outputStream);
  }
}
