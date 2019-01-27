/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.properties;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import net.sf.gm.core.utils.LocationUtil;
import net.sf.gm.core.utils.PropertiesUtil;

//
/**
 * The Class ModuleProperties.
 */
public class ModuleProperties {

  /** The prop name_ MODUL e_ release. */
  public String propName_MODULE_Release = "[MODULENAME]RELEASE";

  /** The prop name_ MODUL e_ url. */
  public String propName_MODULE_Url = "[MODULENAME]URL";

  /** The prop name_ MODUL e_ dir. */
  public String propName_MODULE_Dir = "[MODULENAME]DIR";

  /** The prop name_ MODUL e_ prefix. */
  public String propName_MODULE_Prefix = "[MODULENAME]PREFIX";

  /** The prop name_ MODUL e_ exe prefix. */
  public String propName_MODULE_ExePrefix = "[MODULENAME]EXEPREFIX";

  /** The default_ MODUL e_ exe prefix. */
  public String default_MODULE_ExePrefix = "${[MODULENAME]PREFIX}";

  // // user executables:
  // public String default_MODULE_BinDir = "${[MODULENAME]EXEPREFIX}/bin/";

  // // system admin executables:
  // public String default_MODULE_SBinDir = "${[MODULENAME]EXEPREFIX}/sbin/";

  // program executables:
  /** The prop name_ MODUL e_ lib exec dir. */
  public String propName_MODULE_LibExecDir = "[MODULENAME]LIBEXECDIR";

  /** The default_ MODUL e_ lib exec dir. */
  public String default_MODULE_LibExecDir = "${[MODULENAME]EXEPREFIX}/libexec/";

  // (object code) libraries:
  /** The prop name_ MODUL e_ lib dir. */
  public String propName_MODULE_LibDir = "[MODULENAME]LIBDIR";

  /** The default_ MODUL e_ lib dir. */
  public String default_MODULE_LibDir = "${[MODULENAME]EXEPREFIX}/lib/";

  // read-only single-machine data
  /** The prop name_ MODUL e_ etc dir. */
  public String propName_MODULE_EtcDir = "[MODULENAME]ETCDIR";

  /** The default_ MODUL e_ etc dir. */
  public String default_MODULE_EtcDir = "${[MODULENAME]PREFIX}/etc/";

  // read-only architecture-independend data
  /** The prop name_ MODUL e_ share dir. */
  public String propName_MODULE_ShareDir = "[MODULENAME]SHAREDIR";

  /** The default_ MODUL e_ share dir. */
  public String default_MODULE_ShareDir = "${[MODULENAME]PREFIX}/share/";

  // modifiable single-machine data
  /** The prop name_ MODUL e_ var dir. */
  public String propName_MODULE_VarDir = "[MODULENAME]VARDIR";

  /** The default_ MODUL e_ var dir. */
  public String default_MODULE_VarDir = "${[MODULENAME]PREFIX}/var/";

  // modifiable architecture-independend data
  /** The prop name_ MODUL e_ com dir. */
  public String propName_MODULE_ComDir = "[MODULENAME]COMDIR";

  /** The default_ MODUL e_ com dir. */
  public String default_MODULE_ComDir = "${[MODULENAME]PREFIX}/com/";

  // // info documentation
  // public String default_MODULE_InfoDir = "${[MODULENAME]PREFIX}/info/";

  // // man documentation
  // public String default_MODULE_ManDir = "${[MODULENAME]PREFIX}/man/";

  /** The module class. */
  private Class<?> moduleClass;

  /** The module name. */
  private String moduleName;

  /**
   * The Constructor.
   *
   * @param moduleName      the module name
   * @param moduleClass     the module class
   * @param versionResource the version resource
   */
  public ModuleProperties(final String moduleName, final String versionResource, final Class<?> moduleClass) {

    this.moduleClass = moduleClass == null ? ModuleProperties.class : moduleClass;

    this.moduleName = moduleName;

    AppProperties.startup();

    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_Release), "");

    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_Prefix),
        getModuleDependendValue(getDefaultMODULEPrefix()));
    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_ExePrefix),
        getModuleDependendValue(default_MODULE_ExePrefix));

    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_LibExecDir),
        getModuleDependendValue(default_MODULE_LibExecDir));
    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_LibDir),
        getModuleDependendValue(default_MODULE_LibDir));
    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_EtcDir),
        getModuleDependendValue(default_MODULE_EtcDir));
    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_ShareDir),
        getModuleDependendValue(default_MODULE_ShareDir));
    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_VarDir),
        getModuleDependendValue(default_MODULE_VarDir));
    AppProperties.setDefaultAppPropertyValue(getModuleDependendValue(propName_MODULE_ComDir),
        getModuleDependendValue(default_MODULE_ComDir));

    AppProperties.addAppProperty(getModuleDependendValue(propName_MODULE_Url), getMODULEUrl());
    AppProperties.addAppProperty(getModuleDependendValue(propName_MODULE_Dir), getMODULEDirectory());

    if (versionResource != null) {
      final Properties props = new Properties();
      PropertiesUtil.createPropertiesFromResource(props, versionResource, false);
      final Set<Object> keys = props.keySet();
      final Iterator<Object> i = keys.iterator();
      while (i.hasNext()) {
        final String key = (String) i.next();
        final String value = props.getProperty(key);
        if (value != null)
          AppProperties.addAppProperty(key, value);
      }
    }
  }

  /**
   * Gets the module dependend value.
   *
   * @param property the property
   *
   * @return the module dependend value
   */
  public String getModuleDependendValue(final String property) {

    return property.replaceAll("\\[MODULENAME\\]", moduleName);
  }

  /**
   * get the root url for this library e.g. file:///path/libraryname.jar
   *
   * @return the MODULE url
   */
  private String getMODULEUrl() {

    return LocationUtil.getClassPathUrl(moduleClass).toExternalForm();
  }

  /**
   * get the library directory.
   *
   * @return the MODULE directory
   */
  private String getMODULEDirectory() {

    return SystemProperties.getLibraryDir(moduleClass);
  }

  /**
   * get the default prefix directory.
   *
   * @return the default MODULE prefix
   */
  private String getDefaultMODULEPrefix() {

    try {
      File file = new File(getMODULEDirectory());
      if (!file.exists())
        return null;
      if ((file = file.getParentFile()) == null)
        return null;
      return file.getAbsolutePath() + SystemProperties.getFileSeperator();
    } catch (final Exception ignore) {
    }
    return null;
  }
}
