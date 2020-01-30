/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.common;

import net.sf.gm.core.properties.GMCOREProperties;
import net.sf.gm.core.properties.ModuleProperties;

//
//


/**
 * The Class GMJDBCProperties.
 */
public class GMIOProperties {

    /**
     * The props.
     */
    static ModuleProperties props = null;

    static {
        GMCOREProperties.startup();

        GMIOProperties.props =
            new ModuleProperties("GMIO", "gmio.properties", GMIOProperties.class);
    }

    /**
     * Startup.
     */
    public static void startup() {
    }
}
