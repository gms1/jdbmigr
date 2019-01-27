/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

//
/**
 * The Class Base64Decoder.
 */
public class Base64Decoder extends Base64Base {

  /** The br. */
  private InputStreamReader br;

  /** The os. */
  private OutputStream os;

  /**
   * The Constructor.
   *
   * @param os the os
   * @param is the is
   */
  public Base64Decoder(final InputStream is, final OutputStream os) {

    br = new InputStreamReader(is);
    this.os = os;
  }

  /**
   * Process.
   *
   * @throws IOException the IO exception
   */
  public void process() throws IOException {

    int readLen;
    final char[] buf = new char[getBufferSize()];
    while ((readLen = br.read(buf)) != -1)
      os.write(Base64Base.decode(buf, 0, readLen));
  }
}
