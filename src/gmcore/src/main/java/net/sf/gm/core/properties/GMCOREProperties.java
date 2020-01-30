/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.properties;

//


/**
 * The Class GMCOREProperties.
 */
public class GMCOREProperties {

    /**
     * The Constant propName_LogConfigFile.
     */
    public static final String propName_LogConfigFile = "LOGCONFIGFILE";

    // LOGCONFIGFILE property:
    // default location for log.xml:
    // on unix: $HOME/.jdbmigr/shared
    // on windows: %APPDATA%\jdbmigr\shared
    /**
     * The Constant default_LogConfigFile.
     */
    public static final String default_LogConfigFile = "${APPUSERDATADIR}log.xml";

    /**
     * The Constant propName_LogConfigSourceFile.
     */
    public static final String propName_LogConfigSourceFile =
        "LOGCONFIGSOURCEFILE";

    /**
     * The Constant default_LogConfigSourceFile.
     */
    public static final String default_LogConfigSourceFile =
        "${GMCOREETCDIR}log.xml";

    // os settings

    /**
     * The Constant propName_HiddenFilePrefix.
     */
    public static final String propName_HiddenFilePrefix = "HIDDENFILEPREFIX";

    // HOMEDIR: ( Windows: %APPDATA%, Unix $HOME )
    /**
     * The Constant propName_HomeDir.
     */
    public static final String propName_HomeDir = "HOMEDIR";

    // ETCDIR: ( Windows: %ALLUSERSPROFILE%\Application Data, Unix /etc )
    /**
     * The Constant propName_EtcDir.
     */
    public static final String propName_EtcDir = "ETCDIR";

    /**
     * The props.
     */
    static ModuleProperties props = null;

    static {

        AppProperties.startup();

        AppProperties.setDefaultAppPropertyValue(GMCOREProperties.propName_HomeDir,
            SystemProperties.getHomeDir());

        AppProperties.setDefaultAppPropertyValue(GMCOREProperties.propName_EtcDir,
            SystemProperties.getEtcDir());

        AppProperties.setDefaultAppPropertyValue(
            GMCOREProperties.propName_LogConfigFile,
            GMCOREProperties.default_LogConfigFile);
        AppProperties.setDefaultAppPropertyValue(
            GMCOREProperties.propName_LogConfigSourceFile,
            GMCOREProperties.default_LogConfigSourceFile);

        AppProperties.addAppProperty(GMCOREProperties.propName_HiddenFilePrefix,
            SystemProperties.getHiddenFilePrefix());

        GMCOREProperties.props = new ModuleProperties("GMCORE", "gmcore.properties",
            GMCOREProperties.class);
    }

    /**
     * Startup.
     */
    public static void startup() {
    }
}
