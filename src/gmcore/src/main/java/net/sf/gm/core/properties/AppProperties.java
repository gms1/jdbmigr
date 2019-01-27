/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.properties;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.gm.core.utils.LoggerUtil;
import net.sf.gm.core.utils.PropertiesUtil;

//
/**
 * a pool for application properties (like: company, product, application,
 * version appUserDataDir, appUserDataSharedDir appSystemDataDir,
 * appSystemDataSharedDir)
 *
 * additional functionality: variable substitution, including a cache for
 * expanded values default value pool consistency check: a value cannot be
 * changed if it is already in use, unless the change will be forced.
 */
final public class AppProperties {

  /** The log. */
  static Logger log = Logger.getLogger(AppProperties.class.getName());

  /** The app properties. */
  private static Properties appProperties;

  /** The app properties cache. */
  private static Properties appPropertiesCache;

  /** The app properties used. */
  private static Set<String> appPropertiesUsed;

  /** The app properties default. */
  private static Properties appPropertiesDefault;

  /** The log file handle map. */
  private static final Map<String, FileHandler> logFileHandleMap =
      new HashMap<String, FileHandler>();

  /** The Constant appPropPattern. */
  private static final Pattern appPropPattern =
      Pattern.compile("\\$\\{([\\w\\d]*)\\}");

  // application settings

  /** The Constant propName_Company. */
  public static final String propName_Company = "COMPANY";

  /** The Constant propName_Product. */
  public static final String propName_Product = "PRODUCT";

  /** The Constant propName_Application. */
  public static final String propName_Application = "APPLICATION";

  /** The Constant propName_Version. */
  public static final String propName_Version = "VERSION";

  /** The Constant propName_AppSubDir. */
  public static final String propName_AppSubDir = "APPSUBDIR";

  /** The Constant propName_SharedSubDir. */
  public static final String propName_SharedSubDir = "SHAREDSUBDIR";

  // locations

  // application-user-data (configuration) directories
  /** The Constant propName_AppUserDataDir. */
  public static final String propName_AppUserDataDir = "APPUSERDATADIR";

  /** The Constant default_AppUserDataDir. */
  public static final String default_AppUserDataDir =
      "${HOMEDIR}/${HIDDENFILEPREFIX}${APPSUBDIR}/";

  /** The Constant propName_AppUserDataSharedDir. */
  public static final String propName_AppUserDataSharedDir =
      "APPUSERDATASHAREDDIR";

  /** The Constant default_AppUserDataSharedDir. */
  public static final String default_AppUserDataSharedDir =
      "${HOMEDIR}/${HIDDENFILEPREFIX}${SHAREDSUBDIR}/";

  // application-system-data (configuration) directories
  /** The Constant propName_AppSystemDataDir. */
  public static final String propName_AppSystemDataDir = "APPSYSTEMDATADIR";

  /** The Constant default_AppSystemDataDir. */
  public static final String default_AppSystemDataDir =
      "${ETCDIR}/${APPSUBDIR}/";

  /** The Constant propName_AppSystemDataSharedDir. */
  public static final String propName_AppSystemDataSharedDir =
      "APPSYSTEMDATASHAREDDIR";

  /** The Constant default_AppSystemDataSharedDir. */
  public static final String default_AppSystemDataSharedDir =
      "${ETCDIR}/${SHAREDSUBDIR}/";

  /** The modul props. */
  static ModuleProperties modulProps = null;

  static {
    AppProperties.appProperties = new Properties();
    AppProperties.appPropertiesCache = new Properties();
    AppProperties.appPropertiesUsed = new HashSet<String>();
    AppProperties.appPropertiesDefault = new Properties();

    GMCOREProperties.startup();
  }

  /**
   * Startup.
   */
  public static void startup() {}

  /**
   * initialize.
   *
   * @param app  the app
   * @param prod the prod
   * @param ver  the ver
   * @param comp the comp
   */

  protected static void doInit(final String comp, final String prod,
                               final String app, final String ver) {

    String company = comp;
    String product = prod;
    String application = app;
    String version = ver;

    if (company == null) // company name can be empty
      company = "";

    if (version == null) // version can be empty
      version = "";

    if (product == null && application == null)
      throw new ConfigurationException(
          "no product name and no application name defined");
    if (product == null)
      product = application;
    if (application == null)
      application = product;

    if (product.equals("") && application.equals(""))
      throw new ConfigurationException(
          "roduct name and application name is empty");

    if (product.equals(""))
      product = application;

    if (application.equals(""))
      application = product;

    String appSubDir;
    String sharedSubDir;

    if (company.equals("")) {

      appSubDir = product.equals(application) ? "${PRODUCT}"
                                              : "${PRODUCT}/${APPLICATION}";
      sharedSubDir = "${PRODUCT}/shared";

    } else {

      appSubDir = product.equals(application)
                      ? "${COMPANY}/${PRODUCT}"
                      : "${COMPANY}/${PRODUCT}/${APPLICATION}";
      sharedSubDir = "${COMPANY}/${PRODUCT}/shared";
    }

    AppProperties.setDefaultAppPropertyValueIfNotDefined(
        AppProperties.propName_AppUserDataDir,
        AppProperties.default_AppUserDataDir);
    AppProperties.setDefaultAppPropertyValueIfNotDefined(
        AppProperties.propName_AppUserDataSharedDir,
        AppProperties.default_AppUserDataSharedDir);

    AppProperties.setDefaultAppPropertyValueIfNotDefined(
        AppProperties.propName_AppSystemDataDir,
        AppProperties.default_AppSystemDataDir);
    AppProperties.setDefaultAppPropertyValueIfNotDefined(
        AppProperties.propName_AppSystemDataSharedDir,
        AppProperties.default_AppSystemDataSharedDir);

    AppProperties.setDefaultAppPropertyValueIfNotDefined(
        AppProperties.propName_AppSubDir, appSubDir);
    AppProperties.setDefaultAppPropertyValueIfNotDefined(
        AppProperties.propName_SharedSubDir, sharedSubDir);

    AppProperties.addAppProperty(AppProperties.propName_Company, company);
    AppProperties.addAppProperty(AppProperties.propName_Product, product);
    AppProperties.addAppProperty(AppProperties.propName_Application,
                                 application);
    AppProperties.addAppProperty(AppProperties.propName_Version, version);

    LoggerUtil.loadDefaultConfig();
  }

  /**
   * Init.
   *
   * @param product     the product
   * @param company     the company
   * @param application the application
   * @param version     the version
   */
  public static void init(final String company, final String product,
                          final String application, final String version) {

    AppProperties.modulProps = new ModuleProperties("APP", null, null);
    AppProperties.doInit(company, product, application, version);
  }

  /**
   * Init.
   *
   * @param product         the product
   * @param appClass        the app class
   * @param versionPrefix   the version prefix
   * @param versionResource the version resource
   * @param company         the company
   * @param application     the application
   */
  public static void init(final String company, final String product,
                          final String application,
                          final String versionResource,
                          final String versionPrefix, final Class<?> appClass) {

    AppProperties.modulProps =
        new ModuleProperties("APP", versionResource, appClass);
    final String version = AppProperties.getAppPropertyDefinition(
        AppProperties.modulProps.getModuleDependendValue(
            AppProperties.modulProps.propName_MODULE_Release));
    AppProperties.doInit(company, product, application, version);
  }

  /**
   * Init.
   *
   * @param product     the product
   * @param appClass    the app class
   * @param company     the company
   * @param application the application
   * @param version     the version
   */
  public static void init(final String company, final String product,
                          final String application, final String version,
                          final Class<?> appClass) {

    AppProperties.modulProps = new ModuleProperties("APP", null, appClass);
    AppProperties.doInit(company, product, application, version);
  }

  /**
   * get the company name.
   *
   * @return the company
   */
  public static String getCompany() {

    return AppProperties.getAppProperty(AppProperties.propName_Company);
  }

  /**
   * get the product name.
   *
   * @return the product
   */
  public static String getProduct() {

    return AppProperties.getAppProperty(AppProperties.propName_Product);
  }

  /**
   * get the application name.
   *
   * @return the application
   */
  public static String getApplication() {

    return AppProperties.getAppProperty(AppProperties.propName_Application);
  }

  /**
   * get the product version.
   *
   * @return the version
   */
  public static String getVersion() {

    return AppProperties.getAppProperty(AppProperties.propName_Version);
  }

  /**
   * get the application-data-user (configuration) directory.
   *
   * @return the app user data dir
   */
  public static String getAppUserDataDir() {

    return AppProperties.getAppProperty(AppProperties.propName_AppUserDataDir);
  }

  /**
   * set the application-data-user (configuration) directory.
   *
   * @param appUserDataDir the app user data dir
   */
  public static void setAppUserDataDir(final String appUserDataDir) {

    AppProperties.addAppProperty(AppProperties.propName_AppUserDataDir,
                                 appUserDataDir);
  }

  /**
   * get the application-shared-user-data directory.
   *
   * @return the app user data shared dir
   */
  public static String getAppUserDataSharedDir() {

    return AppProperties.getAppProperty(
        AppProperties.propName_AppUserDataSharedDir);
  }

  /**
   * set the application-shared-user-data directory.
   *
   * @param appUserDataSharedDir the app user data shared dir
   */
  public static void
  setAppUserDataSharedDir(final String appUserDataSharedDir) {

    AppProperties.addAppProperty(AppProperties.propName_AppUserDataSharedDir,
                                 appUserDataSharedDir);
  }

  /**
   * get the application-data-system (configuration) directory.
   *
   * @return the app system data dir
   */
  public static String getAppSystemDataDir() {

    return AppProperties.getAppProperty(
        AppProperties.propName_AppSystemDataDir);
  }

  /**
   * set the application-data-system (configuration) directory.
   *
   * @param appSystemDataDir the app system data dir
   */
  public static void setAppSystemDataDir(final String appSystemDataDir) {

    AppProperties.addAppProperty(AppProperties.propName_AppSystemDataDir,
                                 appSystemDataDir);
  }

  /**
   * get the application-shared-system-data directory.
   *
   * @return the app system data shared dir
   */
  public static String getAppSystemDataSharedDir() {

    return AppProperties.getAppProperty(
        AppProperties.propName_AppSystemDataSharedDir);
  }

  /**
   * set the application-shared-system-data directory.
   *
   * @param appSystemDataSharedDir the app system data shared dir
   */
  public static void
  setAppSystemDataSharedDir(final String appSystemDataSharedDir) {

    AppProperties.addAppProperty(AppProperties.propName_AppSystemDataSharedDir,
                                 appSystemDataSharedDir);
  }

  /**
   * test if property is available.
   *
   * @param propName the prop name
   *
   * @return true, if has app property
   */
  public static boolean hasAppProperty(final String propName) {

    return AppProperties.appProperties.getProperty(propName) != null ? true
                                                                     : false;
  }

  /**
   * test if property is already in use.
   *
   * @param propName the prop name
   *
   * @return true, if have app property used
   */
  public static boolean haveAppPropertyUsed(final String propName) {

    return AppProperties.appPropertiesUsed.contains(propName) ? true : false;
  }

  /**
   * get the default application property value.
   *
   * @param propName the prop name
   */
  public static void getDefaultAppPropertyValue(final String propName) {

    AppProperties.appPropertiesDefault.getProperty(propName);
  }

  /**
   * set the default application property value.
   *
   * @param propName  the prop name
   * @param propValue the prop value
   */
  public static void setDefaultAppPropertyValue(final String propName,
                                                final String propValue) {

    AppProperties.appPropertiesDefault.setProperty(propName, propValue);
  }

  /**
   * set the default application property value.
   *
   * @param propName  the prop name
   * @param propValue the prop value
   */
  public static void
  setDefaultAppPropertyValueIfNotDefined(final String propName,
                                         final String propValue) {

    if (AppProperties.appPropertiesDefault.getProperty(propName) == null)
      AppProperties.appPropertiesDefault.setProperty(propName, propValue);
  }

  /**
   * get application property.
   *
   * @param propName the prop name
   *
   * @return the app property
   */
  public static String getAppProperty(final String propName) {

    String value = AppProperties.appPropertiesCache.getProperty(propName);
    if (value != null)
      return value;
    value = AppProperties.appProperties.getProperty(propName);
    if (value == null) {
      value = AppProperties.appPropertiesDefault.getProperty(propName);
      if (value == null)
        throw new ConfigurationException("in getAppProperty(\"" + propName +
                                         "\"): property not initialized");
      AppProperties.appProperties.setProperty(propName, value);
    }
    value = AppProperties.expandApplicationProperties(value);
    synchronized (AppProperties.appPropertiesCache) {
      AppProperties.appPropertiesCache.setProperty(propName, value);
    }
    synchronized (AppProperties.appPropertiesUsed) {
      AppProperties.appPropertiesUsed.add(propName);
    }
    return value;
  }

  /**
   * get the defined application property value.
   *
   * @param propName the prop name
   *
   * @return the app property definition
   */
  public static String getAppPropertyDefinition(final String propName) {

    final String value = AppProperties.appPropertiesCache.getProperty(propName);
    if (value != null)
      return value;
    return AppProperties.appProperties.getProperty(propName);
  }

  /**
   * add application property a RuntimeException (ConfigurationException) will
   * be thrown, if: the input for the property value is null, or the property is
   * already in use.
   *
   * @param propName  the prop name
   * @param propValue the prop value
   */
  public static void addAppProperty(final String propName,
                                    final String propValue) {

    AppProperties.addAppProperty(propName, propValue, true, false);
  }

  /**
   * add application property if not already defined.
   *
   * @param propName  the prop name
   * @param propValue the prop value
   */
  public static void addAppPropertyIfNotDefined(final String propName,
                                                final String propValue) {

    AppProperties.addAppProperty(propName, propValue, false, false);
  }

  /**
   * add application property, if property already defined, a change will be
   * forced.
   *
   * @param propName  the prop name
   * @param propValue the prop value
   */
  public static void addAppPropertyForced(final String propName,
                                          final String propValue) {

    AppProperties.addAppProperty(propName, propValue, true, true);
  }

  /**
   * add application property a RuntimeException (ConfigurationException) will
   * be thrown, if: the input for the property value is null, or the property is
   * already in use, the change-flag is set and the force-flag is not set.
   *
   * @param propName  the prop name
   * @param propValue the prop value
   * @param force     the force
   * @param change    the change
   */
  private static void addAppProperty(final String propName,
                                     final String propValue, boolean change,
                                     boolean force) {

    synchronized (AppProperties.appProperties) {
      if (propValue == null)
        throw new ConfigurationException(
            "in addAppProperty(\"" + propName +
            "\",null): property must be initialized");
      final String value = AppProperties.appProperties.getProperty(propName);
      if (value != null) {
        // property is defined

        // return if change-flag is not set
        if (!change)
          return;

        // return if new value equals old value
        if (propValue.equals(value))
          return;

        final boolean used = AppProperties.appPropertiesUsed.contains(propName);

        if (used && !force)
          throw new ConfigurationException(
              "in addAppProperty(\"" + propName + "\",\"" + propValue +
              "\"): property already initialized: \"" + value + "\"");
        AppProperties.log.fine((used && force)
                                   ? "forced "
                                   : ""
                                         + "change of application property \"" +
                                         propName + "\" from \"" + value +
                                         "\" to \"" + propValue + "\"");
        if (used)
          synchronized (AppProperties.appPropertiesCache) {
            AppProperties.appPropertiesCache.clear();
          }
      }
      AppProperties.log.finest("add application property \"" + propName +
                               "\": \"" + propValue + "\"");
      AppProperties.appProperties.setProperty(propName, propValue);
    }
  }

  /**
   * expand a string containing markers like "${propName}" with the equivalent
   * property value.
   *
   * @param value the value
   *
   * @return the string
   */
  public static String expandApplicationProperties(final String value) {

    if (value == null)
      return null;
    StringBuilder buf = null;
    String res = value;
    boolean found;
    int start;
    // todo we should remove duplicate file or path seperator
    do {
      found = false;
      start = 0;
      buf = new StringBuilder();
      final Matcher matcher = AppProperties.appPropPattern.matcher(res);
      while (matcher.find()) {
        // we do not want to replace with expression,
        // we want to replace with string literal
        // matcher.appendReplacement(res);
        if (matcher.start() > start)
          buf.append(res.substring(start, matcher.start()));
        buf.append(AppProperties.getAppProperty(matcher.group(1)));
        start = matcher.end();
        found = true;
      }
      // matcher.appendTail():
      buf.append(res.substring(start));
      res = buf.toString();
    } while (found);
    return res;
  }

  /**
   * get all application property keys.
   *
   * @return the app property keys
   */
  public static String[] getAppPropertyKeys() {

    final Properties p = (Properties)AppProperties.appProperties.clone();
    p.putAll(AppProperties.appPropertiesDefault);
    return PropertiesUtil.getSortedKeys(p);
  }

  /**
   * log all application properties.
   */
  public static void logAppProperties() {

    if (!AppProperties.log.isLoggable(Level.FINEST))
      return;
    final Enumeration<Object> en = AppProperties.appProperties.keys();
    while (en.hasMoreElements()) {
      final String propName = (String)en.nextElement();
      final String propValue =
          AppProperties.appProperties.getProperty(propName);
      AppProperties.log.finest("application property \"" + propName + "\": \"" +
                               propValue + "\"");
    }
  }

  /**
   * add a logfile to the logger.
   *
   * @param level       the level
   * @param logFileName the log file name
   *
   * @return true, if add log file
   */
  public static boolean addLogFile(final String logFileName, final int level) {

    return AppProperties.addLogFile(logFileName,
                                    LoggerUtil.getLevelFromInt(level));
  }

  /**
   * add a logfile to the logger.
   *
   * @param level       the level
   * @param logFileName the log file name
   *
   * @return true, if add log file
   */
  public static boolean addLogFile(final String logFileName,
                                   final String level) {

    return AppProperties.addLogFile(logFileName,
                                    LoggerUtil.getLevelFromString(level));
  }

  /**
   * add a logfile to the logger.
   *
   * @param level       the level
   * @param logFileName the log file name
   *
   * @return true, if add log file
   */
  public static boolean addLogFile(final String logFileName,
                                   final Level level) {

    synchronized (AppProperties.logFileHandleMap) {
      final FileHandler fh = LoggerUtil.addLogFileHandler(logFileName, level);
      if (fh == null)
        return false;
      AppProperties.logFileHandleMap.put(logFileName, fh);
    }
    return true;
  }

  /**
   * remove a logfile from logger.
   *
   * @param logFileName the log file name
   */
  public static void remLogFile(final String logFileName) {

    synchronized (AppProperties.logFileHandleMap) {
      final FileHandler fh = AppProperties.logFileHandleMap.get(logFileName);
      if (fh != null) {
        AppProperties.logFileHandleMap.remove(logFileName);
        LoggerUtil.remLogFileHandler(fh);
      }
    }
  }
}
