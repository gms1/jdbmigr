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
     * The cl info.
     */
    protected final DriverClassInfo clInfo;
    /**
     * The ds info.
     */
    protected final DataSourceInfo dsInfo;
    /**
     * The props.
     */
    Properties props = null;
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
     */
    public PrintWriter getLogWriter() {
        return printWriter;
    }

    /**
     * Sets the log writer.
     *
     * @param out the out
     */
    public void setLogWriter(final PrintWriter out) {

        printWriter = out;
    }

    /**
     * Gets the login timeout.
     *
     * @return the login timeout
     */
    public int getLoginTimeout() {
        return loginTimeout;
    }

    /**
     * Sets the login timeout.
     *
     * @param seconds the seconds
     */
    public void setLoginTimeout(final int seconds) {

        loginTimeout = seconds;
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
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override
    public <T> T unwrap(java.lang.Class<T> iface) throws SQLException {
        throw new SQLException("method 'unwrap' not implemented in DataSourceWrapperFromDriverClass");
    }
}
