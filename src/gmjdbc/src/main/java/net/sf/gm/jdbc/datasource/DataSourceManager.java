/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import net.sf.gm.core.model.SortedSetModel;
import net.sf.gm.jdbc.common.JdbcException;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.datasource.config.DataSourceConfiguration;
import net.sf.gm.jdbc.datasource.config.DataSourceInfo;
import net.sf.gm.jdbc.datasource.config.DriverClassInfo;
import net.sf.gm.jdbc.datasource.wrapper.LocalDataSource;

//
/**
 * DataSourceManager singleton with on demand initialization.
 */
final public class DataSourceManager {

  /** The dsm. */
  private static DataSourceManagerImpl dsm = null;

  /**
   * The Constructor.
   */
  private DataSourceManager() {}

  /**
   * Lookup.
   *
   * @param dataSouceOrDriverName the data source name
   *
   * @return the data source
   *
   * @throws JdbcException the jdbc exception
   */
  public static synchronized DataSource lookup(final String dataSouceOrDriverName, final String url)
      throws JdbcException {

    return DataSourceManager.getInstance().lookup(dataSouceOrDriverName, url);
  }

  /**
   * Test data source info.
   *
   * @param password       the password
   * @param username       the username
   * @param dataSourceInfo the data source info
   * @param classInfo      the class info
   *
   * @return true, if test data source info
   *
   * @throws SQLException  the SQL exception
   * @throws JdbcException the jdbc exception
   */
  public static boolean testDataSourceInfo(final DriverClassInfo classInfo,
                                           final DataSourceInfo dataSourceInfo,
                                           final String username,
                                           final String password)
      throws JdbcException, SQLException {

    return DataSourceManager.getInstance().testDataSourceInfo(
        classInfo, dataSourceInfo, username, password);
  }

  /**
   * Test class info.
   *
   * @param classInfo the class info
   *
   * @return true, if test class info
   *
   * @throws JdbcException the jdbc exception
   */
  public static boolean testClassInfo(final DriverClassInfo classInfo)
      throws JdbcException {

    return DataSourceManager.getInstance().testClassInfo(classInfo);
  }

  /**
   * Load new configuration.
   *
   * @param classInfoList      the class info list
   * @param dataSourceInfoList the data source info list
   *
   * @return true, if load new configuration
   */
  public static boolean
  loadNewConfiguration(final ArrayList<DriverClassInfo> classInfoList,
                       final ArrayList<DataSourceInfo> dataSourceInfoList) {

    return DataSourceManager.getInstance().loadNewConfiguration(
        classInfoList, dataSourceInfoList);
  }

  /**
   * Gets the data source names.
   *
   * @return the data source names
   */
  public static SortedSetModel<String> getDataSourceNames() {

    return DataSourceManager.getInstance().getDataSourceNames();
  }

  /**
   * Gets the instance.
   *
   * @return the instance
   */
  public static synchronized DataSourceManagerImpl getInstance() {

    return DataSourceManager.dsm == null
        ? (DataSourceManager.dsm = new DataSourceManagerImpl())
        : DataSourceManager.dsm;
  }

  /**
   * The Class DataSourceManagerImpl.
   */
  protected static class DataSourceManagerImpl extends DataSourceConfiguration {

    /**
     * The Constructor.
     */
    protected DataSourceManagerImpl() { super(); }

    /**
     * Lookup.
     *
     * @param dataSourceOrDriverName the data source name
     *
     * @return the local data source
     *
     * @throws JdbcException the jdbc exception
     */
    protected synchronized LocalDataSource lookup(final String dataSourceOrDriverName, final String url)
        throws JdbcException {

      LocalDataSource res = null;
      try {
        if (url == null) {
          final DataSourceInfo dsInfo = getDataSourceInfo(dataSourceOrDriverName);
          if (dsInfo == null)
            throw new JdbcException("datasource lookup for \"" + dataSourceOrDriverName + "\" failed");
          final DriverClassInfo clInfo =
              getDriverClassInfo(dsInfo.getClassName());
          if (clInfo == null)
            throw new JdbcException("class not found for datasource (\"local." +
                                    dataSourceOrDriverName + "\"");
          res = new LocalDataSource(clInfo, dsInfo);
        } else {
          final DriverClassInfo clInfo = getDriverClassInfo(dataSourceOrDriverName);
          if (clInfo == null)
            throw new JdbcException("driver lookup for \"" + dataSourceOrDriverName + "\" failed");
          
          DataSourceInfo dsInfo = new DataSourceInfo();
          dsInfo.setUrl(url);
          res = new LocalDataSource(clInfo, dsInfo);
        }
      } catch (final Exception e) {
        throw new JdbcException(e);
      }
      return res;
    }

    /**
     * Test class info.
     *
     * @param classInfo the class info
     *
     * @return true, if test class info
     *
     * @throws JdbcException the jdbc exception
     */
    protected boolean testClassInfo(final DriverClassInfo classInfo)
        throws JdbcException {

      if (classInfo.getNewClassInstance() != null)
        return true;
      return false;
    }

    /**
     * Test data source info.
     *
     * @param password       the password
     * @param username       the username
     * @param dataSourceInfo the data source info
     * @param classInfo      the class info
     *
     * @return true, if test data source info
     *
     * @throws SQLException  the SQL exception
     * @throws JdbcException the jdbc exception
     */
    protected boolean testDataSourceInfo(final DriverClassInfo classInfo,
                                         final DataSourceInfo dataSourceInfo,
                                         final String username,
                                         final String password)
        throws JdbcException, SQLException {

      final DataSource ds = new LocalDataSource(classInfo, dataSourceInfo);
      final Connection con = ds.getConnection(username, password);
      if (con == null)
        return false;
      SqlUtil.closeConnection(con);
      return true;
    }
  }
}
