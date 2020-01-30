/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.load;

import net.sf.gm.core.io.DataWriter;

import java.sql.Connection;
import java.sql.SQLException;

//


/**
 * The Interface LoadHandler.
 */
public interface Loader extends DataWriter {

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    Connection getConnection();

    /**
     * Sets the connection.
     *
     * @param con the con
     */
    void setConnection(Connection con);

    /**
     * Delete.
     *
     * @param tableName   the table name
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     * @return the long
     * @throws SQLException the SQL exception
     */
    long delete(String tableName, String schemaName, String catalogName)
        throws SQLException;

    /**
     * Start loading.
     *
     * @param tableName   the table name
     * @param schemaName  the schema name
     * @param catalogName the catalog name
     */
    void startLoading(String tableName, String schemaName, String catalogName)
    ;

    /**
     * End loading.
     */
    void endLoading();

    /**
     * Close.
     */
    void close();

    /**
     * Sets the max batch size.
     *
     * @param maxBatchSize the max batch size
     */
    void setMaxBatchSize(int maxBatchSize);

    /**
     * Sets the commit size.
     *
     * @param commitSize the commit size
     */
    void setCommitSize(int commitSize);

    /**
     * Sets the map columns by names.
     *
     * @param mapByName the map by name
     */
    void setMapColumnsByNames(boolean mapByName);

    /**
     * Sets the skip columns not found.
     *
     * @param skipNotFound the skip not found
     */
    void setSkipColumnsNotFound(boolean skipNotFound);

    /**
     * Sets the do import.
     *
     * @param doImport the do import
     */
    void setDoImport(boolean doImport);

    /**
     * Sets the do sync.
     *
     * @param doSync the do sync
     */
    void setDoSync(boolean doSync);

    /**
     * Commit.
     *
     * @throws SQLException the SQL exception
     */
    void commit() throws SQLException;

    /**
     * Rollback.
     *
     * @throws SQLException the SQL exception
     */
    void rollback() throws SQLException;
}
