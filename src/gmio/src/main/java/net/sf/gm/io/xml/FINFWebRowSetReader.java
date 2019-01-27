/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import java.io.InputStream;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.ui.Progress;

import com.sun.xml.fastinfoset.stax.StAXDocumentParser;

//
/**
 * The Class FINFWebRowSetReader.
 */
public class FINFWebRowSetReader
    extends XMLWebRowSetReaderBase implements DataReader {

  /**
   * The Constructor.
   *
   * @param validate the validate
   * @param progress the progress
   * @param is       the is
   *
   * @throws DataIOException the data IO exception
   */
  public FINFWebRowSetReader(Progress progress, InputStream is,
                             boolean validate) throws DataIOException {

    super(progress, is, new StAXDocumentParser(is), validate);
  }

  /**
   * The Constructor.
   *
   * @param progress the progress
   * @param is       the is
   *
   * @throws DataIOException the data IO exception
   */
  public FINFWebRowSetReader(Progress progress, InputStream is)
      throws DataIOException {

    super(progress, is, new StAXDocumentParser(is), false);
  }
}
