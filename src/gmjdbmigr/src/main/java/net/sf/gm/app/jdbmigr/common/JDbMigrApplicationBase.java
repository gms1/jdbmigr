/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.app.jdbmigr.common;

import net.sf.gm.core.app.AbstractApplication;

//


/**
 * The Class JDbMigrApplication.
 */
public abstract class JDbMigrApplicationBase extends AbstractApplication {

    /**
     * The Constructor.
     */
    public JDbMigrApplicationBase() {

        super();
        GMJDBMIGRProperties.startup();
    }
}
