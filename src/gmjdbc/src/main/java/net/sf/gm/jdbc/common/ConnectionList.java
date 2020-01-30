/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.common;

import net.sf.gm.core.model.ModelServer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

//


/**
 * The Class ConnectionList.
 */
public class ConnectionList extends ModelServer<ConnectionList> {

    /**
     * The list.
     */
    private final SortedMap<String, ConnectionInfo> list = new TreeMap<String, ConnectionInfo>();

    /**
     * The Constructor.
     */
    public ConnectionList() {

        super();
    }

    /**
     * Add.
     *
     * @param userName       the user name
     * @param dataSourceName the data source name
     * @param conn           the conn
     * @return the string
     */
    public String add(final Connection conn, final String dataSourceName,
        final String userName) {

        final StringBuilder newName = new StringBuilder(dataSourceName);
        synchronized (list) {
            newName.append("/");
            newName.append(userName);
            final String[] names = list.keySet().toArray(new String[0]);
            int connId = 1;
            for (final String name : names) {
                final ConnectionInfo info = list.get(name);
                if (info.dataSourceName.equals(dataSourceName) &&
                    info.userName.equals(userName) && info.connId >= connId)
                    connId = info.getConnId() + 1;
            }
            newName.append(" (");
            if (connId < 10)
                newName.append("0");
            newName.append(connId);
            newName.append(")");
            final ConnectionInfo ci = new ConnectionInfo(
                conn, dataSourceName, userName, newName.toString());
            ci.setConnId(connId);
            list.put(newName.toString(), ci);
        }
        notifyClients();
        return newName.toString();
    }

    /**
     * Remove.
     *
     * @param name the name
     * @throws SQLException the SQL exception
     */
    public void remove(final String name) throws SQLException {

        synchronized (list) {
            final ConnectionInfo i = list.get(name);
            if (i == null)
                return;
            i.getConn().close();
            list.remove(name);
        }
        notifyClients();
    }

    /**
     * Get.
     *
     * @param name the name
     * @return the connection
     */
    public Connection get(final String name) {

        final ConnectionInfo i = list.get(name);
        if (i == null)
            return null;
        final Connection c = i.getConn();
        try {
            if (c.isClosed()) {
                remove(name);
                return null;
            }
        } catch (final Exception e) {
            return null;
        }
        return c;
    }

    /**
     * Gets the names.
     *
     * @return the names
     */
    public Set<String> getNames() {
        return list.keySet();
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    @Override
    protected ConnectionList getModel() {

        return this;
    }

    /**
     * The Class ConnectionInfo.
     */
    public static class ConnectionInfo {

        /**
         * The display name.
         */
        String displayName = null;

        /**
         * The data source name.
         */
        String dataSourceName = null;

        /**
         * The user name.
         */
        String userName = null;

        /**
         * The conn.
         */
        Connection conn = null;

        /**
         * The conn id.
         */
        int connId;

        /**
         * The Constructor.
         *
         * @param userName       the user name
         * @param dataSourceName the data source name
         * @param conn           the conn
         * @param displayName    the display name
         */
        public ConnectionInfo(final Connection conn, final String dataSourceName,
            final String userName, final String displayName) {

            this.conn = conn;
            this.dataSourceName = dataSourceName;
            this.userName = userName;
            this.displayName = displayName;
        }

        /**
         * Compare to.
         *
         * @param obj the obj
         * @return the int
         */
        public int compareTo(final Object obj) {

            if (!obj.getClass().isInstance(this))
                throw new RuntimeException("cannot compare \"" +
                    this.getClass().getName() + "\" with \"" +
                    obj.getClass().getName() + "\"");
            return this.displayName.compareTo(((ConnectionInfo) obj).getDisplayName());
        }

        /**
         * Gets the conn.
         *
         * @return the conn
         */
        public Connection getConn() {
            return conn;
        }

        /**
         * Gets the conn id.
         *
         * @return the conn id
         */
        public int getConnId() {
            return connId;
        }

        /**
         * Sets the conn id.
         *
         * @param connId the conn id
         */
        public void setConnId(final int connId) {
            this.connId = connId;
        }

        /**
         * Gets the data source name.
         *
         * @return the data source name
         */
        public String getDataSourceName() {
            return dataSourceName;
        }

        /**
         * Gets the display name.
         *
         * @return the display name
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Gets the user name.
         *
         * @return the user name
         */
        public String getUserName() {
            return userName;
        }
    }
}
