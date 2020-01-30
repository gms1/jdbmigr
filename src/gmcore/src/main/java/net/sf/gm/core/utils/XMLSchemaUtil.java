/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.URL;

//


/**
 * The Class XMLSchemaUtil.
 */
public class XMLSchemaUtil {

    /**
     * The Constant W3C_XML_SCHEMA_NS_URI.
     */
    final static String W3C_XML_SCHEMA_NS_URI;

    static {
        W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    }

    /**
     * The schema w3 factory.
     */
    static SchemaFactory schemaW3Factory =
        SchemaFactory.newInstance(XMLSchemaUtil.W3C_XML_SCHEMA_NS_URI);

    /**
     * Creates the schema.
     *
     * @param url the url
     * @return the schema
     * @throws SAXException the SAX exception
     */
    public static Schema createSchema(final URL url) throws SAXException {

        return XMLSchemaUtil.schemaW3Factory.newSchema(url);
    }

    /**
     * Creates the schema.
     *
     * @param file the file
     * @return the schema
     * @throws SAXException the SAX exception
     */
    public static Schema createSchema(final File file) throws SAXException {

        return XMLSchemaUtil.schemaW3Factory.newSchema(file);
    }
}
