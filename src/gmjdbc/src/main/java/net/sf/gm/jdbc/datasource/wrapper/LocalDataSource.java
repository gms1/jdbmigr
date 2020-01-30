/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.datasource.wrapper;

import net.sf.gm.jdbc.common.JdbcException;
import net.sf.gm.jdbc.datasource.config.DataSourceInfo;
import net.sf.gm.jdbc.datasource.config.DriverClassInfo;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

//


/**
 * datasource proxy for local datasource.
 */
public class LocalDataSource implements DataSourceWrapper {

    /**
     * The ds.
     */
    private DataSourceWrapper ds;

    /**
     * The Constructor.
     *
     * @param dsInfo the ds info
     * @param clInfo the cl info
     * @throws JdbcException the jdbc exception
     */
    public LocalDataSource(final DriverClassInfo clInfo, final DataSourceInfo dsInfo) throws JdbcException {

        super();
        this.ds = null;
        final String classType = clInfo.getClassType();
        if (classType.equals("Driver"))
            ds = new DataSourceWrapperFromDriverClass(clInfo, dsInfo);
        else if (classType.equals("DataSource"))
            ds = new DataSourceWrapperFromDataSourceClass(clInfo, dsInfo);
        else
            throw new JdbcException("unknown Interface \"" + classType + "\" of class \"" + clInfo.getClassName() + "\"");
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException the SQL exception
     */
    public Connection getConnection() throws SQLException {

        return ds.getConnection();
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

        return ds.getConnection(username, password);
    }

    /**
     * Gets the log writer.
     *
     * @return the log writer
     * @throws SQLException the SQL exception
     */
    public PrintWriter getLogWriter() throws SQLException {

        return ds.getLogWriter();
    }

    /**
     * Sets the log writer.
     *
     * @param out the out
     * @throws SQLException the SQL exception
     */
    public void setLogWriter(final PrintWriter out) throws SQLException {

        ds.setLogWriter(out);
    }

    /**
     * Sets the login timeout.
     *
     * @param seconds the seconds
     * @throws SQLException the SQL exception
     */
    public void setLoginTimeout(final int seconds) throws SQLException {

        ds.setLoginTimeout(seconds);
    }

    /**
     * Gets the login timeout.
     *
     * @return the login timeout
     * @throws SQLException the SQL exception
     */
    public int getLoginTimeout() throws SQLException {

        return ds.getLoginTimeout();
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public Properties getProperties() {
        return ds.getProperties();
    }

    /**
     * Gets the property info.
     *
     * @param info the info
     * @param url  the url
     * @return the property info
     * @throws SQLException the SQL exception
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {

        return ds.getPropertyInfo(url, info);
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return ds.getUrl();
    }

    /**
     * Sets the url.
     */
    public void setUrl(final String newUrl) {
        ds.setUrl(newUrl);
    }

    /**
     * Sets the properties.
     *
     * @param newProps the new props
     */
    public void setProperties(Properties newProps) {
        ds.setProperties(newProps);
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return ds.getParentLogger();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return ds.isWrapperFor(iface);
    }

    @Override
    public <T> T unwrap(java.lang.Class<T> iface) throws SQLException {
        return ds.unwrap(iface);
    }
}
