/*
 *
 */
package net.sf.gm.jdbc.datasource.wrapper;

import net.sf.gm.jdbc.common.JdbcException;
import net.sf.gm.jdbc.datasource.config.DataSourceInfo;
import net.sf.gm.jdbc.datasource.config.DriverClassInfo;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The Class DataSourceWrapperFromDriverClass.
 */
public class DataSourceWrapperFromDriverClass implements DataSourceWrapper {

    /**
     * The cl instance.
     */
    private Driver clInstance;

    /**
     * The login timeout.
     */
    private int loginTimeout;

    /**
     * The print writer.
     */
    private PrintWriter printWriter;

    /**
     * The cl info.
     */
    protected DriverClassInfo clInfo;

    /**
     * The ds info.
     */
    protected DataSourceInfo dsInfo;

    /**
     * The props.
     */
    Properties props = null;

    /**
     * The Constructor.
     *
     * @param dsInfo the ds info
     * @param clInfo the cl info
     * @throws JdbcException the jdbc exception
     */
    public DataSourceWrapperFromDriverClass(DriverClassInfo clInfo, DataSourceInfo dsInfo) throws JdbcException {

        this.loginTimeout = 0;
        this.printWriter = null;
        this.clInfo = clInfo;
        this.dsInfo = dsInfo;
        this.clInstance = createInstance();
    }

    /**
     * Gets the cl instance.
     *
     * @return the cl instance
     */
    public Driver getClInstance() {
        return clInstance;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException the SQL exception
     */
    public Connection getConnection() throws SQLException {

        final int oldsec = DriverManager.getLoginTimeout();

        DriverManager.setLoginTimeout(getLoginTimeout());
        DriverManager.setLogWriter(printWriter);
        final Properties info = props == null ? new Properties() : (Properties) props.clone();

        Connection res = null;
        try {
            res = clInstance.connect(dsInfo.getUrl(), info);
        } finally {
            DriverManager.setLoginTimeout(oldsec);
            DriverManager.setLogWriter(printWriter);
        }
        return res;
    }

    /**
     * Gets the connection.
     *
     * @param password the password
     * @param username the username
     * @return the connection
     * @throws SQLException the SQL exception
     */
    public Connection getConnection(final String username, final String password) throws SQLException {

        final int oldsec = DriverManager.getLoginTimeout();

        DriverManager.setLoginTimeout(getLoginTimeout());
        DriverManager.setLogWriter(printWriter);
        final Properties info = props == null ? new Properties() : (Properties) props.clone();
        if (username != null)
            info.put("user", username);
        if (password != null)
            info.put("password", password);

        Connection res = null;
        try {
            if (!clInstance.acceptsURL(dsInfo.getUrl()))
                throw new JdbcException(
                    "url '" + dsInfo.getUrl() + "' not accepted by driver '" + clInstance.getClass().getName() + "'");
            res = clInstance.connect(dsInfo.getUrl(), info);
        } finally {
            DriverManager.setLoginTimeout(oldsec);
            DriverManager.setLogWriter(printWriter);
        }
        if (res == null)
            throw new JdbcException("Connection is null");
        return res;
    }

    /**
     * Gets the log writer.
     *
     * @return the log writer
     * @throws SQLException the SQL exception
     */
    public PrintWriter getLogWriter() throws SQLException {
        return printWriter;
    }

    /**
     * Sets the log writer.
     *
     * @param out the out
     * @throws SQLException the SQL exception
     */
    public void setLogWriter(final PrintWriter out) throws SQLException {

        printWriter = out;
    }

    /**
     * Sets the login timeout.
     *
     * @param seconds the seconds
     * @throws SQLException the SQL exception
     */
    public void setLoginTimeout(final int seconds) throws SQLException {

        loginTimeout = seconds;
    }

    /**
     * Gets the login timeout.
     *
     * @return the login timeout
     * @throws SQLException the SQL exception
     */
    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    /**
     * Gets the property info.
     *
     * @param info the info
     * @param url  the url
     * @return the property info
     * @throws SQLException the SQL exception
     */
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {

        return clInstance.getPropertyInfo(url, info);
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return dsInfo.getUrl();
    }

    /**
     * Sets the url.
     */
    public void setUrl(final String newUrl) {
        dsInfo.setUrl(newUrl);
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public Properties getProperties() {

        return props == null ? new Properties() : (Properties) props.clone();
    }

    /**
     * Sets the properties.
     *
     * @param newProps the new props
     */
    public void setProperties(final Properties newProps) {
        props = newProps;
    }

    /**
     * Creates the instance.
     *
     * @return the driver
     * @throws JdbcException the jdbc exception
     */
    protected Driver createInstance() throws JdbcException {

        clInstance = (Driver) clInfo.getClassInstance();
        return clInstance;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return clInstance.getParentLogger();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(java.lang.Class<T> iface) throws SQLException {
        throw new SQLException("method 'unwrap' not implemented in DataSourceWrapperFromDriverClass");
    }
}
