/*
 *
 */
package net.sf.gm.jdbc.datasource.wrapper;

import javax.sql.DataSource;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The Interface DataSourceWrapper.
 */
public interface DataSourceWrapper extends DataSource {

    /**
     * Gets the property info.
     *
     * @param info the info
     * @param url  the url
     * @return the property info
     * @throws SQLException the SQL exception
     */
    DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info)
        throws SQLException;

    /**
     * Gets the url.
     *
     * @return the url
     */
    String getUrl();

    /**
     * Sets the url.
     */
    void setUrl(final String newUrl);

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    Properties getProperties();

    /**
     * Sets the properties.
     *
     * @param newProps the new props
     */
    void setProperties(final Properties newProps);
}
