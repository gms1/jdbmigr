/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.core.utils.StAXUtil;

//
/**
 * The Class XMLWebRowSetReader.
 */
public class XMLWebRowSetReader
    extends XMLWebRowSetReaderBase implements DataReader {

  /**
   * The Constructor.
   *
   * @param validate the validate
   * @param progress the progress
   * @param is       the is
   *
   * @throws XMLStreamException the XML stream exception
   * @throws DataIOException    the data IO exception
   */
  public XMLWebRowSetReader(Progress progress, InputStream is, boolean validate)
      throws DataIOException, XMLStreamException {

    super(progress, is, StAXUtil.createXMLStreamReader(is), validate);
  }

  /**
   * The Constructor.
   *
   * @param progress the progress
   * @param is       the is
   *
   * @throws XMLStreamException the XML stream exception
   * @throws DataIOException    the data IO exception
   */
  public XMLWebRowSetReader(Progress progress, InputStream is)
      throws DataIOException, XMLStreamException {

    super(progress, is, StAXUtil.createXMLStreamReader(is), false);
  }
}
