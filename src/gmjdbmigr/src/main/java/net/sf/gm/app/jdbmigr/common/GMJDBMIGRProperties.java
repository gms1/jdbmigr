/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.app.jdbmigr.common;

import net.sf.gm.core.properties.GMCOREProperties;
import net.sf.gm.core.properties.ModuleProperties;
import net.sf.gm.io.common.GMIOProperties;
import net.sf.gm.jdbc.common.GMJDBCProperties;

//


/**
 * The Class GMJDBMIGRProperties.
 */
public class GMJDBMIGRProperties {

    /**
     * The props.
     */
    static ModuleProperties props = null;

    static {
        GMCOREProperties.startup();
        GMIOProperties.startup();
        GMJDBCProperties.startup();
        GMJDBMIGRProperties.props = new ModuleProperties(
            "GMJDBMIGR", "properties.version", GMJDBMIGRProperties.class);
    }

    /**
     * Startup.
     */
    public static void startup() {
    }
}
