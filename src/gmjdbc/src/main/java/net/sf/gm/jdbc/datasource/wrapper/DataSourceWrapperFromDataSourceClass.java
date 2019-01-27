/*
 *
 */
package net.sf.gm.jdbc.datasource.wrapper;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Logger;

import net.sf.gm.jdbc.common.JdbcException;
import net.sf.gm.jdbc.datasource.config.DataSourceInfo;
import net.sf.gm.jdbc.datasource.config.DriverClassInfo;

/**
 * The Class DataSourceWrapperFromDataSourceClass.
 */
public class DataSourceWrapperFromDataSourceClass implements DataSourceWrapper {

  /** The cl instance. */
  private DataSource clInstance;

  /** The cl info. */
  protected DriverClassInfo clInfo;

  /** The ds info. */
  protected DataSourceInfo dsInfo;

  /**
   * The Constructor.
   *
   * @param dsInfo the ds info
   * @param clInfo the cl info
   *
   * @throws JdbcException the jdbc exception
   */
  public DataSourceWrapperFromDataSourceClass(DriverClassInfo clInfo, DataSourceInfo dsInfo) throws JdbcException {

    this.clInfo = clInfo;
    this.dsInfo = dsInfo;
    this.clInstance = createInstance();
    throw new JdbcException("class DataSourceWrapperFromDataSourceClass not supported yet");
  }

  /**
   * Gets the connection.
   *
   * @return the connection
   *
   * @throws SQLException the SQL exception
   */
  public Connection getConnection() throws SQLException {

    return this.clInstance.getConnection();
  }

  /**
   * Gets the connection.
   *
   * @param password the password
   * @param username the username
   *
   * @return the connection
   *
   * @throws SQLException the SQL exception
   */
  public Connection getConnection(final String username, final String password) throws SQLException {

    return this.clInstance.getConnection(username, password);
  }

  /**
   * Gets the log writer.
   *
   * @return the log writer
   *
   * @throws SQLException the SQL exception
   */
  public PrintWriter getLogWriter() throws SQLException {

    return clInstance.getLogWriter();
  }

  /**
   * Sets the log writer.
   *
   * @param out the out
   *
   * @throws SQLException the SQL exception
   */
  public void setLogWriter(final PrintWriter out) throws SQLException {

    clInstance.setLogWriter(out);
  }

  /**
   * Sets the login timeout.
   *
   * @param seconds the seconds
   *
   * @throws SQLException the SQL exception
   */
  public void setLoginTimeout(final int seconds) throws SQLException {

    clInstance.setLoginTimeout(seconds);
  }

  /**
   * Gets the login timeout.
   *
   * @return the login timeout
   *
   * @throws SQLException the SQL exception
   */
  public int getLoginTimeout() throws SQLException {

    return clInstance.getLoginTimeout();
  }

  /**
   * Creates the instance.
   *
   * @return the data source
   *
   * @throws JdbcException the jdbc exception
   */
  protected DataSource createInstance() throws JdbcException {

    if (clInstance != null)
      return clInstance;
    clInstance = (DataSource) clInfo.getClassInstance();
    return clInstance;
  }

  /**
   * Gets the properties.
   *
   * @return the properties
   */
  public Properties getProperties() {

    // todo: implement
    return null;
  }

  /**
   * Gets the property info.
   *
   * @param info the info
   * @param url  the url
   *
   * @return the property info
   *
   * @throws SQLException the SQL exception
   */
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {

    // todo: implement
    return null;
  }

  /**
   * Gets the url.
   *
   * @return the url
   */
  public String getUrl() {

    // todo: implement
    return null;
  }

  /**
   * Sets the url.
   *
   */
  public void setUrl(final String newUrl) {
    // todo: implement
  }

  /**
   * Sets the properties.
   *
   * @param newProps the new props
   */
  public void setProperties(Properties newProps) {

    // todo: implement
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return clInstance.getParentLogger();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return clInstance.isWrapperFor(iface);
  }

  @Override
  public <T> T unwrap(java.lang.Class<T> iface) throws SQLException {
    return clInstance.unwrap(iface);
  }
}
