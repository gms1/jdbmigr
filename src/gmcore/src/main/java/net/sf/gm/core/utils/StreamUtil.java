/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

//
/**
 * The Class StreamUtil.
 */
public class StreamUtil {

  /**
   * The Constructor.
   */
  public StreamUtil() { super(); }

  /**
   * Close output stream.
   *
   * @param os the os
   */
  public static void closeOutputStream(final OutputStream os) {

    try {
      if (os != null)
        os.close();
    } catch (final Exception ignore) {
    }
  }

  /**
   * Close input stream.
   *
   * @param is the is
   */
  public static void closeInputStream(final InputStream is) {

    try {
      if (is != null)
        is.close();
    } catch (final Exception ignore) {
    }
  }

  /**
   * Close reader.
   *
   * @param reader the reader
   */
  public static void closeReader(final Reader reader) {

    try {
      if (reader != null)
        reader.close();
    } catch (final Exception ignore) {
    }
  }

  /**
   * Close writer.
   *
   * @param writer the writer
   */
  public static void closeWriter(final Writer writer) {

    try {
      if (writer != null)
        writer.close();
    } catch (final Exception ignore) {
    }
  }
}
