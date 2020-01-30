/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;

//


/**
 * The Class ConfigurationXMLBase.
 */
public class ConfigurationXMLBase {

    /**
     * The Constant CONFIGURATION_DTD_URI.
     */
    protected static final String CONFIGURATION_DTD_URI =
        "http://sf.net/gm/dtd/config.dtd";

    /**
     * The Constant CONFIGURATION_DTD.
     */
    protected static final String CONFIGURATION_DTD =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!-- DTD for configuration files -->"
            + "<!ELEMENT config ( comment?, node? ) >"
            + "<!ATTLIST config version CDATA #FIXED \"1.0\">"
            + "<!ELEMENT comment (#PCDATA) >"
            + "<!ELEMENT node ( properties?, childnodes? ) >"
            + "<!ATTLIST node name CDATA #REQUIRED>"
            + "<!ELEMENT childnodes ( node* ) >"
            + "<!ELEMENT properties ( entry* ) >"
            + "<!ELEMENT entry (#PCDATA) >"
            + "<!ATTLIST entry key CDATA #REQUIRED>";

    /**
     * The Constructor.
     */
    public ConfigurationXMLBase() {
        super();
    }

    /**
     * The Class ConfigurationEntityResolver.
     */
    protected static class ConfigurationEntityResolver implements EntityResolver {

        /**
         * Resolve entity.
         *
         * @param publicId the public id
         * @param systemId the system id
         * @return the input source
         * @throws SAXException the SAX exception
         */
        public InputSource resolveEntity(final String publicId,
            final String systemId)
            throws SAXException {

            if (systemId.equals(ConfigurationXMLBase.CONFIGURATION_DTD_URI)) {
                final InputSource is = new InputSource(
                    new StringReader(ConfigurationXMLBase.CONFIGURATION_DTD));
                is.setSystemId(ConfigurationXMLBase.CONFIGURATION_DTD_URI);
                return is;
            }
            throw new SAXException("EntityResolver: Invalid system identifier: " +
                systemId);
        }
    }
}
