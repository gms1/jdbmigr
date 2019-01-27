/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import net.sf.gm.core.utils.SAXUtil;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

//
/**
 * The Class ConfigurationXMLReader.
 */
public class ConfigurationXMLReader extends ConfigurationXMLBase {

  /** The xml reader. */
  XMLReader xmlReader;

  /** The is. */
  InputSource is;

  /**
   * The Constructor.
   */
  public ConfigurationXMLReader() {

    super();
    xmlReader = null;
    is = null;
  }

  /**
   * Process.
   *
   * @param newComment the new comment
   * @param data       the data
   * @param in         the in
   *
   * @return true, if process
   *
   * @throws ConfigurationException the configuration exception
   */
  public boolean process(final ConfigurationData data, final InputStream in, final StringBuilder newComment)
      throws ConfigurationException {

    try {

      final SAXParser parser = SAXUtil.createSAXParser(true, true, false, false, null);

      is = new InputSource(in);
      xmlReader = parser.getXMLReader();
      final ConfigurationXMLContentHandler ch = new ConfigurationXMLContentHandler(data, newComment);
      xmlReader.setContentHandler(ch);
      xmlReader.setErrorHandler(ch);
      xmlReader.setEntityResolver(new ConfigurationXMLBase.ConfigurationEntityResolver());
      xmlReader.parse(is);
      in.close();
      return true;
    } catch (final IOException ioe) {
      throw new ConfigurationException(ioe);
    } catch (final SAXException saxe) {
      throw new ConfigurationException(saxe);
    } catch (final ParserConfigurationException pce) {
      throw new ConfigurationException(pce);
    }
  }
}
