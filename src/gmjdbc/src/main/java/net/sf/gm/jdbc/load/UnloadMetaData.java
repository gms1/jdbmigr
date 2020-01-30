/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.load;

import net.sf.gm.core.io.MetaData;
import net.sf.gm.core.io.MetaDataImpl;
import net.sf.gm.jdbc.common.GMJDBCProperties;
import net.sf.gm.jdbc.io.TableDef;

import java.sql.*;

/**
 * The Class UnloadMetaData.
 */
public class UnloadMetaData extends MetaDataImpl implements MetaData {

    /**
     * The con.
     */
    private final Connection con;

    /**
     * The stmt.
     */
    private final Statement stmt;

    /**
     * The rs.
     */
    private final ResultSet rs;

    /**
     * The rsmd.
     */
    private final ResultSetMetaData rsmd;

    /**
     * The Constructor.
     *
     * @param rsmd          the rsmd
     * @param stmt          the stmt
     * @param con           the con
     * @param tableName     the table name
     * @param rs            the rs
     * @param schemaName    the schema name
     * @param statementText the statement text
     * @param catalogName   the catalog name
     * @throws SQLException the SQL exception
     */
    public UnloadMetaData(final Connection con, final Statement stmt,
        final ResultSet rs, final ResultSetMetaData rsmd,
        String tableName, String schemaName, String catalogName,
        String statementText) throws SQLException {

        super();
        this.con = con;
        this.stmt = stmt;
        this.rs = rs;
        this.rsmd = rsmd;
        setTableName(tableName);
        setSchemaName(schemaName);
        setCatalogName(catalogName);
        this.setColumnCount(rsmd.getColumnCount());
        setCommand(statementText);
    }

    /**
     * Init.
     */
    @Override
    public void init() {

        super.init();
        setConcurrency();
        setFetchDirection();
        setFetchSize();
        setTransactionIsolation();
        setMaxFieldSize();
        setMaxRows();
        setQueryTimeout();
        setReadOnly();
        setResultSetType();
        int colCount = getColumnCount();
        for (int idx = 1; idx <= colCount; idx++) {
            setColumnName(idx);
            setColumnLabelName(idx);
            setColumnTableName(idx);
            setColumnSchemaName(idx);
            setColumnCatalogName(idx);
            setColumnTypeName(idx);
            setColumnType(idx);
            setColumnNullable(idx);
            setColumnDisplaySize(idx);
            setColumnAutoIncrement(idx);
            setColumnCaseSensitive(idx);
            setColumnCurrency(idx);
            setColumnSigned(idx);
            setColumnSearchable(idx);
            setColumnPrecision(idx);
            setColumnScale(idx);
        }

        // depends on column definition
        setKeyColumns();
    }

    /**
     * Sets the column name.
     *
     * @param idx the idx
     */
    public void setColumnName(int idx) {

        try {
            setColumnName(idx, rsmd.getColumnName(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column label name.
     *
     * @param idx the idx
     */
    public void setColumnLabelName(int idx) {

        try {
            setColumnLabel(idx, rsmd.getColumnLabel(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column table name.
     *
     * @param idx the idx
     */
    public void setColumnTableName(int idx) {

        try {
            String name = rsmd.getTableName(idx);
            if (name == null || name.length() == 0)
                name = getTableName();
            setColumnTableName(idx, name);
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column schema name.
     *
     * @param idx the idx
     */
    public void setColumnSchemaName(int idx) {

        try {
            String name = rsmd.getSchemaName(idx);
            if (name == null || name.length() == 0)
                name = getSchemaName();
            setColumnSchemaName(idx, name);
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column catalog name.
     *
     * @param idx the idx
     */
    public void setColumnCatalogName(int idx) {

        try {
            String name = rsmd.getCatalogName(idx);
            if (name == null || name.length() == 0)
                name = getCatalogName();
            setColumnCatalogName(idx, name);
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column type name.
     *
     * @param idx the idx
     */
    public void setColumnTypeName(int idx) {

        try {
            setColumnTypeName(idx, rsmd.getColumnTypeName(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column type.
     *
     * @param idx the idx
     */
    public void setColumnType(int idx) {

        try {
            int jdbctype = rsmd.getColumnType(idx);
            if (jdbctype == Types.NULL)
                jdbctype = GMJDBCProperties.getJdbcTypes().NativeTypetoInt(
                    rsmd.getColumnTypeName(idx));
            setColumnType(idx, jdbctype);
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column nullable.
     *
     * @param idx the idx
     */
    public void setColumnNullable(int idx) {

        try {
            setColumnNullable(idx, rsmd.isNullable(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column display size.
     *
     * @param idx the idx
     */
    public void setColumnDisplaySize(int idx) {

        try {
            setColumnDisplaySize(idx, rsmd.getColumnDisplaySize(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column auto increment.
     *
     * @param idx the idx
     */
    public void setColumnAutoIncrement(int idx) {

        try {
            setColumnAutoIncrement(idx, rsmd.isAutoIncrement(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column case sensitive.
     *
     * @param idx the idx
     */
    public void setColumnCaseSensitive(int idx) {

        try {
            setColumnCaseSensitive(idx, rsmd.isCaseSensitive(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column currency.
     *
     * @param idx the idx
     */
    public void setColumnCurrency(int idx) {

        try {
            setColumnCurrency(idx, rsmd.isCurrency(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column signed.
     *
     * @param idx the idx
     */
    public void setColumnSigned(int idx) {

        try {
            setColumnSigned(idx, rsmd.isSigned(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column searchable.
     *
     * @param idx the idx
     */
    public void setColumnSearchable(int idx) {

        try {
            setColumnSearchable(idx, rsmd.isSearchable(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column precision.
     *
     * @param idx the idx
     */
    public void setColumnPrecision(int idx) {

        try {
            setColumnPrecision(idx, rsmd.getPrecision(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the column scale.
     *
     * @param idx the idx
     */
    public void setColumnScale(int idx) {

        try {
            setColumnScale(idx, rsmd.getScale(idx));
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the concurrency.
     */
    public void setConcurrency() {

        try {
            setConcurrency(rs.getConcurrency());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the fetch direction.
     */
    public void setFetchDirection() {

        try {
            setFetchDirection(rs.getFetchDirection());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the fetch size.
     */
    public void setFetchSize() {

        try {
            setFetchSize(rs.getFetchSize());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the transaction isolation.
     */
    public void setTransactionIsolation() {

        try {
            setIsolationLevel(con.getTransactionIsolation());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the max field size.
     */
    public void setMaxFieldSize() {

        try {
            setMaxFieldSize(stmt.getMaxFieldSize());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the max rows.
     */
    public void setMaxRows() {

        try {
            setMaxRows(stmt.getMaxRows());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the query timeout.
     */
    public void setQueryTimeout() {

        try {
            setQueryTimeout(stmt.getQueryTimeout());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the read only.
     */
    public void setReadOnly() {

        try {
            setReadOnly(con.isReadOnly());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the result set type.
     */
    public void setResultSetType() {

        try {
            setRowSetType(stmt.getResultSetType());
        } catch (SQLException e) {
        }
    }

    /**
     * Sets the key columns.
     */
    public void setKeyColumns() {

        setKeyColumns(TableDef.getPrimaryKey(con, getCatalogName(), getSchemaName(),
            getTableName()));
    }
}
