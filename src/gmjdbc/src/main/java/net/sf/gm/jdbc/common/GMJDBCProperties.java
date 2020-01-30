/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.common;

import net.sf.gm.core.properties.AppProperties;
import net.sf.gm.core.properties.GMCOREProperties;
import net.sf.gm.core.properties.ModuleProperties;

//
//


/**
 * The Class GMJDBCProperties.
 */
public class GMJDBCProperties {

    /**
     * The Constant propName_DataSourceConfigFile.
     */
    public static final String propName_DataSourceConfigFile =
        "DATASOURCECONFIGFILE";

    // DATASOURCECONFIGFILE property:
    // default location for datasource.xml:
    // on unix: $HOME/.jdbmigr/shared
    // on windows: %APPDATA%\jdbmigr\shared
    /**
     * The Constant default_DataSourceConfigFile.
     */
    public static final String default_DataSourceConfigFile =
        "${APPUSERDATADIR}datasource.xml";

    /**
     * The Constant propName_DataSourceConfigSourceFile.
     */
    public static final String propName_DataSourceConfigSourceFile =
        "DATASOURCECONFIGSOURCEFILE";

    /**
     * The Constant default_DataSourceConfigSourceFile.
     */
    public static final String default_DataSourceConfigSourceFile =
        "${GMJDBCETCDIR}datasource.xml";

    /**
     * The Constant propName_WebRowSetSchemaFile.
     */
    public static final String propName_WebRowSetSchemaFile =
        "WEBROWSETSCHEMAFILE";

    /**
     * The Constant default_WebRowSetSchemaFile.
     */
    public static final String default_WebRowSetSchemaFile =
        "${GMJDBCSHAREDIR}webrowset.xsd";

    /**
     * The props.
     */
    static ModuleProperties props = null;

    /**
     * The types.
     */
    static JdbcTypes types;

    static {
        GMCOREProperties.startup();

        AppProperties.setDefaultAppPropertyValue(
            GMJDBCProperties.propName_DataSourceConfigFile,
            GMJDBCProperties.default_DataSourceConfigFile);
        AppProperties.setDefaultAppPropertyValue(
            GMJDBCProperties.propName_DataSourceConfigSourceFile,
            GMJDBCProperties.default_DataSourceConfigSourceFile);
        AppProperties.setDefaultAppPropertyValue(
            GMJDBCProperties.propName_WebRowSetSchemaFile,
            GMJDBCProperties.default_WebRowSetSchemaFile);

        GMJDBCProperties.props = new ModuleProperties("GMJDBC", "gmjdbc.properties",
            GMJDBCProperties.class);

        types = new JdbcTypes();
    }

    /**
     * Startup.
     */
    public static void startup() {
    }

    /**
     * Gets the jdbc types.
     *
     * @return the jdbc types
     */
    public static JdbcTypes getJdbcTypes() {
        return types;
    }
}
