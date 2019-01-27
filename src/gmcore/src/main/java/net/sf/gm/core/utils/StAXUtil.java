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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.sf.gm.core.io.DataIOException;

//
//
/**
 * The Class StAXUtil.
 */
public class StAXUtil {

  /////////////////////////////////////////////////////////////////////////////////////
  // parser

  /** The parser factory. */
  static XMLInputFactory parserFactory = XMLInputFactory.newInstance();

  /**
   * Creates the XML stream reader.
   *
   * @param in the in
   *
   * @return XMLStreamReader
   *
   * @throws XMLStreamException the XML stream exception
   */
  static public XMLStreamReader createXMLStreamReader(final InputStream in)
      throws XMLStreamException {

    // encoding should be defined in the xml input stream
    return StAXUtil.parserFactory.createXMLStreamReader(in);
  }

  /**
   * Close XML stream reader.
   *
   * @param xmlreader the xmlreader
   */
  static public void closeXMLStreamReader(XMLStreamReader xmlreader) {

    try {
      if (xmlreader != null)
        xmlreader.close();
    } catch (XMLStreamException e) {
    }
  }

  /**
   * Down to.
   *
   * @param parentElement the parent element
   * @param elementName   the element name
   * @param xmlreader     the xmlreader
   *
   * @throws DataIOException the data IO exception
   */
  public static void downTo(XMLStreamReader xmlreader, String elementName,
                            String parentElement) throws DataIOException {

    try {
      if (!StAXUtil.moveDownToStartOfElement(xmlreader, elementName))
        throw new DataIOException("start of element '" + elementName +
                                  "' not found in '" + parentElement + "'");
    } catch (XMLStreamException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Down to child.
   *
   * @param parentElement the parent element
   * @param elementName   the element name
   * @param xmlreader     the xmlreader
   *
   * @throws DataIOException the data IO exception
   */
  public static void downToChild(XMLStreamReader xmlreader, String elementName,
                                 String parentElement) throws DataIOException {

    try {
      String res = StAXUtil.moveDown(xmlreader);
      if (res == null || !res.equals(elementName))
        throw new DataIOException("start of element '" + elementName +
                                  "' not found in '" + parentElement + "'");
    } catch (XMLStreamException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Up to.
   *
   * @param parentElement the parent element
   * @param elementName   the element name
   * @param xmlreader     the xmlreader
   *
   * @throws DataIOException the data IO exception
   */
  public static void upTo(XMLStreamReader xmlreader, String elementName,
                          String parentElement) throws DataIOException {

    try {
      if (!StAXUtil.moveUpToEndOfElement(xmlreader, elementName))
        throw new DataIOException("end of element '" + elementName +
                                  "' not found in '" + parentElement + "'");
    } catch (XMLStreamException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Up to parent.
   *
   * @param parentElement the parent element
   * @param elementName   the element name
   * @param xmlreader     the xmlreader
   *
   * @throws DataIOException the data IO exception
   */
  public static void upToParent(XMLStreamReader xmlreader, String elementName,
                                String parentElement) throws DataIOException {

    try {
      String res = StAXUtil.moveUp(xmlreader);
      if (res == null || !res.equals(elementName))
        throw new DataIOException("end of element '" + elementName +
                                  "' not found in '" + parentElement + "'");
    } catch (XMLStreamException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Read text only child element.
   *
   * @param parentElement the parent element
   * @param elementName   the element name
   * @param xmlreader     the xmlreader
   *
   * @return the string
   *
   * @throws DataIOException the data IO exception
   */
  public static String readTextOnlyChildElement(XMLStreamReader xmlreader,
                                                String elementName,
                                                String parentElement)
      throws DataIOException {

    String text;
    StAXUtil.downToChild(xmlreader, elementName, parentElement);
    try {
      text = xmlreader.getElementText();
    } catch (XMLStreamException e) {
      throw new DataIOException(e);
    }
    // after getElementText, the current event is the corresponding end element
    // StAXUtil.upTo(xmlreader, elementName,parentElement);
    return text;
  }

  /**
   * Move down.
   *
   * @param xmlreader the xmlreader
   *
   * @return the string
   *
   * @throws XMLStreamException the XML stream exception
   */
  public static String moveDown(XMLStreamReader xmlreader)
      throws XMLStreamException {

    while (xmlreader.hasNext()) {
      int event = xmlreader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        return null;
      case XMLStreamConstants.START_ELEMENT:
        return xmlreader.getLocalName();
      case XMLStreamConstants.END_ELEMENT:
        return null;
      }
    }
    return null;
  }

  /**
   * Move up.
   *
   * @param xmlreader the xmlreader
   *
   * @return the string
   *
   * @throws XMLStreamException the XML stream exception
   */
  public static String moveUp(XMLStreamReader xmlreader)
      throws XMLStreamException {

    int stack = 0;
    while (xmlreader.hasNext()) {
      int event = xmlreader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        return null;
      case XMLStreamConstants.START_ELEMENT:
        stack++;
        break;
      case XMLStreamConstants.END_ELEMENT:
        if (stack <= 0)
          return xmlreader.getLocalName();
        stack--;
        break;
      }
    }
    return null;
  }

  /**
   * Move to start of element.
   *
   * @param elementName the element name
   * @param xmlreader   the xmlreader
   *
   * @return true, if move to start of element
   *
   * @throws XMLStreamException the XML stream exception
   */
  public static boolean moveToStartOfElement(XMLStreamReader xmlreader,
                                             String elementName)
      throws XMLStreamException {

    while (xmlreader.hasNext()) {
      int event = xmlreader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        return false;
      case XMLStreamConstants.START_ELEMENT:
        if (xmlreader.getLocalName().equals(elementName))
          return true;
        break;
      }
    }
    return false;
  }

  /**
   * Move to end of element.
   *
   * @param elementName the element name
   * @param xmlreader   the xmlreader
   *
   * @return true, if move to end of element
   *
   * @throws XMLStreamException the XML stream exception
   */
  public static boolean moveToEndOfElement(XMLStreamReader xmlreader,
                                           String elementName)
      throws XMLStreamException {

    while (xmlreader.hasNext()) {
      int event = xmlreader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        return false;
      case XMLStreamConstants.END_ELEMENT:
        if (xmlreader.getLocalName().equals(elementName))
          return true;
        break;
      }
    }
    return false;
  }

  /**
   * Move down to start of element.
   *
   * @param elementName the element name
   * @param xmlreader   the xmlreader
   *
   * @return true, if move down to start of element
   *
   * @throws XMLStreamException the XML stream exception
   */
  public static boolean moveDownToStartOfElement(XMLStreamReader xmlreader,
                                                 String elementName)
      throws XMLStreamException {

    int stack = 0;
    while (xmlreader.hasNext()) {
      int event = xmlreader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        return false;
      case XMLStreamConstants.START_ELEMENT:
        if (xmlreader.getLocalName().equals(elementName))
          return true;
        stack++;
        break;
      case XMLStreamConstants.END_ELEMENT:
        if (stack <= 0)
          return false;
        stack--;
        break;
      }
    }
    return false;
  }

  /**
   * Move up to end of element.
   *
   * @param elementName the element name
   * @param xmlreader   the xmlreader
   *
   * @return true, if move up to end of element
   *
   * @throws XMLStreamException the XML stream exception
   */
  public static boolean moveUpToEndOfElement(XMLStreamReader xmlreader,
                                             String elementName)
      throws XMLStreamException {

    int stack = 0;
    while (xmlreader.hasNext()) {
      int event = xmlreader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        return false;
      case XMLStreamConstants.START_ELEMENT:
        stack++;
        break;
      case XMLStreamConstants.END_ELEMENT:
        if (stack <= 0)
          if (xmlreader.getLocalName().equals(elementName))
            return true;
        stack--;
        break;
      }
    }
    return false;
  }

  /////////////////////////////////////////////////////////////////////////////////////
  // serializer

  /** The serializer factory. */
  static XMLOutputFactory serializerFactory = XMLOutputFactory.newInstance();

  /**
   * Creates the XML stream writer.
   *
   * @param out      the out
   * @param encoding the encoding
   *
   * @return the XML stream writer
   *
   * @throws XMLStreamException the XML stream exception
   */
  static public XMLStreamWriter createXMLStreamWriter(final OutputStream out,
                                                      final String encoding)
      throws XMLStreamException {

    return StAXUtil.serializerFactory.createXMLStreamWriter(out, encoding);
  }

  /**
   * Close XML stream writer.
   *
   * @param xmlwriter the xmlwriter
   */
  static public void closeXMLStreamWriter(XMLStreamWriter xmlwriter) {

    try {
      if (xmlwriter != null)
        xmlwriter.close();
    } catch (XMLStreamException e) {
    }
  }
}
