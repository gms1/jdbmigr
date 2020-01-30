/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

//


/**
 * The Class SAXUtil.
 */
public class SAXUtil {

    /**
     * The parser factory.
     */
    static final SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    /**
     * Creates the SAX parser.
     *
     * @param namespace the namespace
     * @param useschema the useschema
     * @param validate  the validate
     * @param xinclude  the xinclude
     * @param schema    the schema
     * @return the SAX parser
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException                 the SAX exception
     */
    static public SAXParser createSAXParser(final boolean namespace, final boolean validate, final boolean xinclude,
        final boolean useschema, final Schema schema) throws SAXException, ParserConfigurationException {

        SAXParser parser;
        SAXUtil.parserFactory.setNamespaceAware(namespace);
        SAXUtil.parserFactory.setValidating(validate);
        SAXUtil.parserFactory.setXIncludeAware(xinclude);
        SAXUtil.parserFactory.setSchema(schema);
        parser = SAXUtil.parserFactory.newSAXParser();

        if (schema == null && useschema)
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");

        return parser;
    }

}
