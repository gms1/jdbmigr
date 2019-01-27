/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import java.io.OutputStream;

import net.sf.gm.core.ui.Progress;

import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;

/**
 * The Class FINFWebRowSetWriter.
 */
public class FINFWebRowSetWriter extends XMLWebRowSetWriterBase {

  /**
   * The Constructor.
   *
   * @param os       the os
   * @param progress the progress
   */
  public FINFWebRowSetWriter(Progress progress, OutputStream os) {

    super(progress, os, new StAXDocumentSerializer(os));
  }
}
