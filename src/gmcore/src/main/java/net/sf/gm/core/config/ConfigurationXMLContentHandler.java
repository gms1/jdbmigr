/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

//


/**
 * The Class ConfigurationXMLContentHandler.
 */
public class ConfigurationXMLContentHandler extends DefaultHandler {

    /**
     * The data.
     */
    ConfigurationData data;

    /**
     * The data stack.
     */
    private Stack<ConfigurationData> dataStack;

    /**
     * The current text.
     */
    StringBuilder currentText;

    /**
     * The current key.
     */
    String currentKey;

    /**
     * The comment.
     */
    StringBuilder comment;


    /**
     * The Enum NodeType.
     */
    enum NodeType {

        /**
         * The unknown.
         */
        UNKNOWN,

        /**
         * The comment.
         */
        COMMENT,

        /**
         * The node.
         */
        NODE,

        /**
         * The childnodes.
         */
        CHILDNODES,

        /**
         * The properties.
         */
        PROPERTIES,

        /**
         * The entry.
         */
        ENTRY
    }


    /**
     * The current text type.
     */
    NodeType currentTextType;

    /**
     * The Constructor.
     *
     * @param data    the data
     * @param comment the comment
     */
    public ConfigurationXMLContentHandler(final ConfigurationData data,
        final StringBuilder comment) {

        super();
        this.data = data;
        this.comment = comment;
        dataStack = new Stack<ConfigurationData>();
        currentText = null;
        currentKey = null;
        currentTextType = NodeType.UNKNOWN;
        comment.setLength(0);
    }

    /**
     * Start document.
     *
     * @throws SAXException the SAX exception
     */
    @Override
    public void startDocument() throws SAXException {
    }

    /**
     * End document.
     *
     * @throws SAXException the SAX exception
     */
    @Override
    public void endDocument() throws SAXException {
    }

    /**
     * Start element.
     *
     * @param atts         the atts
     * @param qName        the q name
     * @param namespaceURI the namespace URI
     * @param localName    the local name
     * @throws SAXException the SAX exception
     */
    @Override
    public void startElement(final String namespaceURI, final String localName,
        final String qName, final Attributes atts)
        throws SAXException {

        if (qName.equals("comment")) {
            currentTextType = NodeType.COMMENT;
            currentText = new StringBuilder();
            return;
        }
        if (qName.equals("node")) {
            currentTextType = NodeType.NODE;
            String currentNodeName = null;
            final int attrCount = atts.getLength();
            for (int i = 0; i < attrCount; i++)
                if (atts.getQName(i).equals("name")) {
                    currentNodeName = atts.getValue(i);
                    break;
                }
            if (dataStack.isEmpty())
                dataStack.push(data);
            else {
                final ConfigurationData newNode =
                    new ConfigurationData(currentNodeName);
                dataStack.peek().childNodes.put(currentNodeName, newNode);
                dataStack.push(newNode);
            }
            return;
        }
        if (qName.equals("childnodes")) {
            currentTextType = NodeType.CHILDNODES;
            return;
        }
        if (qName.equals("properties")) {
            currentTextType = NodeType.PROPERTIES;
            return;
        }
        if (qName.equals("entry")) {
            final int attrCount = atts.getLength();
            for (int i = 0; i < attrCount; i++)
                if (atts.getQName(i).equals("key")) {
                    currentKey = atts.getValue(i);
                    break;
                }
            currentTextType = NodeType.ENTRY;
            currentText = new StringBuilder();
            return;
        }
    }

    /**
     * End element.
     *
     * @param qName        the q name
     * @param namespaceURI the namespace URI
     * @param localName    the local name
     * @throws SAXException the SAX exception
     */
    @Override
    public void endElement(final String namespaceURI, final String localName,
        final String qName) throws SAXException {

        if (currentTextType == NodeType.ENTRY) {
            dataStack.peek().props.setProperty(currentKey, currentText.toString());
            currentKey = null;
            currentText = null;
            currentTextType = NodeType.UNKNOWN;
        }
        if (currentTextType == NodeType.COMMENT) {
            comment.append(currentText);
            currentText = null;
        }
        if (qName.equals("node"))
            dataStack.pop();
    }

    /**
     * Characters.
     *
     * @param ch     the ch
     * @param start  the start
     * @param length the length
     * @throws SAXException the SAX exception
     */
    @Override
    public void characters(final char[] ch, final int start, final int length)
        throws SAXException {

        if (currentText != null)
            currentText.append(ch, start, length);
    }

    /**
     * Warning.
     *
     * @param exception the exception
     * @throws SAXException the SAX exception
     */
    @Override
    public void warning(final SAXParseException exception) throws SAXException {

        // App.errorln("sax warning:" , exception.getMessage());
    }

    /**
     * Error.
     *
     * @param exception the exception
     * @throws SAXException the SAX exception
     */
    @Override
    public void error(final SAXParseException exception) throws SAXException {

        throw exception;
    }

    /**
     * Fatal error.
     *
     * @param exception the exception
     * @throws SAXException the SAX exception
     */
    @Override
    public void fatalError(final SAXParseException exception)
        throws SAXException {

        throw exception;
    }
}
