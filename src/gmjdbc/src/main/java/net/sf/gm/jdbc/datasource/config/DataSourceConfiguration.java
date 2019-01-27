/*
 *
 */
package net.sf.gm.jdbc.datasource.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.gm.core.config.Configuration;
import net.sf.gm.core.config.ConfigurationFile;
import net.sf.gm.core.model.SortedSetModel;
import net.sf.gm.core.properties.AppProperties;
import net.sf.gm.jdbc.common.GMJDBCProperties;

/**
 * The Class DataSourceConfiguration.
 */
public class DataSourceConfiguration {

  /** The Constant propClassNode. */
  final static String propClassNode = "/jdbc/driver";

  /** The Constant propDataSourceNode. */
  final static String propDataSourceNode = "jdbc/datasource";

  /** The Constant propClassNameKey. */
  final static String propClassNameKey = "classname";

  /** The Constant propClassPathKey. */
  final static String propClassPathKey = "classpath";

  /** The Constant propClassTypeKey. */
  final static String propClassTypeKey = "classtype";

  /** The Constant propClassUrlTemplateKey. */
  final static String propClassUrlTemplateKey = "urltemplate";

  /** The Constant propClassDescriptionKey. */
  final static String propClassDescriptionKey = "description";

  /** The Constant propDataSourceClassKey. */
  final static String propDataSourceClassKey = "class";

  /** The Constant propDataSourceUrlKey. */
  final static String propDataSourceUrlKey = "url";

  /** The cfg. */
  private Configuration cfg;

  /** The class info map. */
  private Map<String, DriverClassInfo> classInfoMap;

  /** The class names. */
  private SortedSet<String> classNames;

  /** The data source info map. */
  private Map<String, DataSourceInfo> dataSourceInfoMap;

  /** The data source names. */
  private SortedSetModel<String> dataSourceNames;

  /**
   * The Constructor.
   */
  protected DataSourceConfiguration() {
    GMJDBCProperties.startup();

    cfg = createConfigurationFile();
    cfg.load();
    load();
  }

  /**
   * Creates the configuration file.
   *
   * @return the configuration file
   */
  protected ConfigurationFile createConfigurationFile() {

    final File cfgfile = new File(AppProperties.getAppProperty(
        GMJDBCProperties.propName_DataSourceConfigFile));

    final File sourcecfgfile = new File(AppProperties.getAppProperty(
        GMJDBCProperties.propName_DataSourceConfigSourceFile));

    return new ConfigurationFile(cfgfile, new ConfigurationFile(sourcecfgfile));
  }

  /**
   * Load.
   */
  protected void load() {

    classInfoMap = new Hashtable<String, DriverClassInfo>();
    classNames = new TreeSet<String>();
    dataSourceInfoMap = new Hashtable<String, DataSourceInfo>();
    dataSourceNames = new SortedSetModel<String>();
    loadClassInfo(classInfoMap, classNames);
    loadDataSourceInfo(dataSourceInfoMap, dataSourceNames);
  }

  /**
   * Load class info.
   *
   * @param classInfoMap the class info map
   * @param classNames   the class names
   */
  protected void loadClassInfo(final Map<String, DriverClassInfo> classInfoMap,
                               final SortedSet<String> classNames) {

    classNames.clear();
    classInfoMap.clear();
    final String[] driverNodes = cfg.getChildNodes(propClassNode);
    if (driverNodes == null)
      return;
    for (final String nodeName : driverNodes) {

      final StringBuilder driverNode = new StringBuilder(propClassNode);
      driverNode.append("/");
      driverNode.append(nodeName);

      classNames.add(nodeName);
      classInfoMap.put(
          nodeName,
          new DriverClassInfo(
              nodeName,
              cfg.getStoredProperty(driverNode.toString(), propClassNameKey),
              cfg.getStoredProperty(driverNode.toString(), propClassPathKey),
              cfg.getStoredProperty(driverNode.toString(), propClassTypeKey),
              cfg.getStoredProperty(driverNode.toString(),
                                    propClassUrlTemplateKey),
              cfg.getStoredProperty(driverNode.toString(),
                                    propClassDescriptionKey)));
    }
  }

  /**
   * Load data source info.
   *
   * @param dataSourceNames   the data source names
   * @param dataSourceInfoMap the data source info map
   */
  protected void
  loadDataSourceInfo(final Map<String, DataSourceInfo> dataSourceInfoMap,
                     final SortedSetModel<String> dataSourceNames) {

    dataSourceNames.clear();
    dataSourceInfoMap.clear();
    final String[] dataSourceNodes = cfg.getChildNodes(propDataSourceNode);

    if (dataSourceNodes == null)
      return;
    for (final String nodeName : dataSourceNodes) {
      final StringBuilder dataSourceNode =
          new StringBuilder(propDataSourceNode);
      dataSourceNode.append("/");
      dataSourceNode.append(nodeName);
      dataSourceNames.add(nodeName);
      dataSourceInfoMap.put(
          nodeName,
          new DataSourceInfo(nodeName,
                             cfg.getStoredProperty(dataSourceNode.toString(),
                                                   propDataSourceClassKey),
                             cfg.getStoredProperty(dataSourceNode.toString(),
                                                   propDataSourceUrlKey)));
    }
  }

  /**
   * Gets the data source names impl.
   *
   * @return the data source names impl
   */
  public SortedSetModel<String> getDataSourceNames() { return dataSourceNames; }

  /**
   * Load new configuration impl.
   *
   * @param dataSourceInfoList the data source info list
   * @param classInfoList      the class info list
   *
   * @return true, if load new configuration impl
   */
  public boolean
  loadNewConfiguration(final ArrayList<DriverClassInfo> classInfoList,
                       final ArrayList<DataSourceInfo> dataSourceInfoList) {

    cfg.load();
    final Map<String, DriverClassInfo> classInfoMap =
        new Hashtable<String, DriverClassInfo>();
    final SortedSet<String> classNames = new TreeSet<String>();
    final Map<String, DataSourceInfo> dataSourceInfoMap =
        new Hashtable<String, DataSourceInfo>();
    final SortedSetModel<String> dataSourceNames = new SortedSetModel<String>();
    loadClassInfo(classInfoMap, classNames);
    loadDataSourceInfo(dataSourceInfoMap, dataSourceNames);
    for (final String className : classNames)
      classInfoList.add(classInfoMap.get(className));
    for (final String dataSourceName : dataSourceNames.toArray())
      dataSourceInfoList.add(dataSourceInfoMap.get(dataSourceName));
    return true;
  }


  /**
   * Gets the data source info.
   *
   * @param dataSourceName the data source name
   *
   * @return the data source info
   */
  public DataSourceInfo getDataSourceInfo(String dataSourceName) {
    return dataSourceInfoMap.get(dataSourceName);
  }

  /**
   * Gets the driver class info.
   *
   * @param driverClassName the driver class name
   *
   * @return the driver class info
   */
  public DriverClassInfo getDriverClassInfo(String driverClassName) {
    return classInfoMap.get(driverClassName);
  }
}
