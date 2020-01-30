/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

//


/**
 * The Interface MetaData.
 */
public interface MetaData {

    /**
     * Gets the command.
     *
     * @return the command
     */
    public String getCommand();

    /**
     * Sets the command.
     *
     * @param command the command
     */
    public void setCommand(String command);

    /**
     * Gets the concurrency.
     *
     * @return the concurrency
     */
    public int getConcurrency();

    /**
     * Sets the concurrency.
     *
     * @param concurrency the concurrency
     */
    public void setConcurrency(int concurrency);

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public String getDataSource();

    /**
     * Sets the data source.
     *
     * @param dataSource the data source
     */
    public void setDataSource(String dataSource);

    /**
     * Checks if is escape processing.
     *
     * @return true, if is escape processing
     */
    public boolean isEscapeProcessing();

    /**
     * Sets the escape processing.
     *
     * @param escape the escape
     */
    public void setEscapeProcessing(boolean escape);

    /**
     * Gets the fetch direction.
     *
     * @return the fetch direction
     */
    public int getFetchDirection();

    /**
     * Sets the fetch direction.
     *
     * @param fetchDirection the fetch direction
     */
    public void setFetchDirection(int fetchDirection);

    /**
     * Gets the fetch size.
     *
     * @return the fetch size
     */
    public int getFetchSize();

    /**
     * Sets the fetch size.
     *
     * @param fetchSize the fetch size
     */
    public void setFetchSize(int fetchSize);

    /**
     * Gets the isolation level.
     *
     * @return the isolation level
     */
    public int getIsolationLevel();

    /**
     * Sets the isolation level.
     *
     * @param transactionIsolation the transaction isolation
     */
    public void setIsolationLevel(int transactionIsolation);

    /**
     * Gets the max field size.
     *
     * @return the max field size
     */
    public int getMaxFieldSize();

    /**
     * Sets the max field size.
     *
     * @param maxFieldSize the max field size
     */
    public void setMaxFieldSize(int maxFieldSize);

    /**
     * Gets the max rows.
     *
     * @return the max rows
     */
    public int getMaxRows();

    /**
     * Sets the max rows.
     *
     * @param maxRows the max rows
     */
    public void setMaxRows(int maxRows);

    /**
     * Gets the query timeout.
     *
     * @return the query timeout
     */
    public int getQueryTimeout();

    /**
     * Sets the query timeout.
     *
     * @param queryTimeout the query timeout
     */
    public void setQueryTimeout(int queryTimeout);

    /**
     * Checks if is read only.
     *
     * @return true, if is read only
     */
    public boolean isReadOnly();

    /**
     * Sets the read only.
     *
     * @param readOnly the read only
     */
    public void setReadOnly(boolean readOnly);

    /**
     * Gets the row set type.
     *
     * @return the row set type
     */
    public int getRowSetType();

    /**
     * Sets the row set type.
     *
     * @param resultSetType the result set type
     */
    public void setRowSetType(int resultSetType);

    /**
     * Checks if is show deleted.
     *
     * @return true, if is show deleted
     */
    public boolean isShowDeleted();

    /**
     * Sets the show deleted.
     *
     * @param showDeleted the show deleted
     */
    public void setShowDeleted(boolean showDeleted);

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getTableName();

    /**
     * Sets the table name.
     *
     * @param tableName the table name
     */
    public void setTableName(String tableName);

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getSchemaName();

    /**
     * Sets the table name.
     *
     * @param schemaName the table name
     */
    public void setSchemaName(String schemaName);

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getCatalogName();

    /**
     * Sets the table name.
     *
     * @param catalogName the table name
     */
    public void setCatalogName(String catalogName);

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl();

    /**
     * Sets the url.
     *
     * @param url the url
     */
    public void setUrl(String url);

    /**
     * Gets the sync provider name.
     *
     * @return the sync provider name
     */
    public String getSyncProviderName();

    /**
     * Sets the sync provider name.
     *
     * @param name the name
     */
    public void setSyncProviderName(String name);

    /**
     * Gets the sync provider vendor.
     *
     * @return the sync provider vendor
     */
    public String getSyncProviderVendor();

    /**
     * Sets the sync provider vendor.
     *
     * @param vendor the vendor
     */
    public void setSyncProviderVendor(String vendor);

    /**
     * Gets the sync provider version.
     *
     * @return the sync provider version
     */
    public String getSyncProviderVersion();

    /**
     * Sets the sync provider version.
     *
     * @param version the version
     */
    public void setSyncProviderVersion(String version);

    /**
     * Gets the sync provider grade.
     *
     * @return the sync provider grade
     */
    public String getSyncProviderGrade();

    /**
     * Sets the sync provider grade.
     *
     * @param grade the grade
     */
    public void setSyncProviderGrade(String grade);

    /**
     * Gets the data source lock.
     *
     * @return the data source lock
     */
    public String getDataSourceLock();

    /**
     * Sets the data source lock.
     *
     * @param lock the lock
     */
    public void setDataSourceLock(String lock);

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    public int getColumnCount();

    /**
     * Sets the column count.
     *
     * @param colCount the col count
     */
    public void setColumnCount(int colCount);

    /**
     * Gets the column catalog name.
     *
     * @param idx the idx
     * @return the column catalog name
     */
    public String getColumnCatalogName(int idx);

    /**
     * Sets the column catalog name.
     *
     * @param columnCatalogName the column catalog name
     * @param idx               the idx
     */
    public void setColumnCatalogName(int idx, String columnCatalogName);

    /**
     * Gets the column display size.
     *
     * @param idx the idx
     * @return the column display size
     */
    public int getColumnDisplaySize(int idx);

    /**
     * Sets the column display size.
     *
     * @param columnDisplaySize the column display size
     * @param idx               the idx
     */
    public void setColumnDisplaySize(int idx, int columnDisplaySize);

    /**
     * Gets the column label.
     *
     * @param idx the idx
     * @return the column label
     */
    public String getColumnLabel(int idx);

    /**
     * Sets the column label.
     *
     * @param columnLabelName the column label name
     * @param idx             the idx
     */
    public void setColumnLabel(int idx, String columnLabelName);

    /**
     * Gets the column name.
     *
     * @param idx the idx
     * @return the column name
     */
    public String getColumnName(int idx);

    /**
     * Sets the column name.
     *
     * @param idx        the idx
     * @param columnName the column name
     */
    public void setColumnName(int idx, String columnName);

    /**
     * Gets the column precision.
     *
     * @param idx the idx
     * @return the column precision
     */
    public int getColumnPrecision(int idx);

    /**
     * Sets the column precision.
     *
     * @param columnPrecision the column precision
     * @param idx             the idx
     */
    public void setColumnPrecision(int idx, int columnPrecision);

    /**
     * Gets the column scale.
     *
     * @param idx the idx
     * @return the column scale
     */
    public int getColumnScale(int idx);

    /**
     * Sets the column scale.
     *
     * @param columnScale the column scale
     * @param idx         the idx
     */
    public void setColumnScale(int idx, int columnScale);

    /**
     * Gets the column schema name.
     *
     * @param idx the idx
     * @return the column schema name
     */
    public String getColumnSchemaName(int idx);

    /**
     * Sets the column schema name.
     *
     * @param columnSchemaName the column schema name
     * @param idx              the idx
     */
    public void setColumnSchemaName(int idx, String columnSchemaName);

    /**
     * Gets the column table name.
     *
     * @param idx the idx
     * @return the column table name
     */
    public String getColumnTableName(int idx);

    /**
     * Sets the column table name.
     *
     * @param columnTableName the column table name
     * @param idx             the idx
     */
    public void setColumnTableName(int idx, String columnTableName);

    /**
     * Gets the column type.
     *
     * @param idx the idx
     * @return the column type
     */
    public int getColumnType(int idx);

    /**
     * Sets the column type.
     *
     * @param columnType the column type
     * @param idx        the idx
     */
    public void setColumnType(int idx, int columnType);

    /**
     * Gets the column type name.
     *
     * @param idx the idx
     * @return the column type name
     */
    public String getColumnTypeName(int idx);

    /**
     * Sets the column type name.
     *
     * @param idx            the idx
     * @param columnTypeName the column type name
     */
    public void setColumnTypeName(int idx, String columnTypeName);

    /**
     * Checks if is column auto increment.
     *
     * @param idx the idx
     * @return true, if is column auto increment
     */
    public boolean isColumnAutoIncrement(int idx);

    /**
     * Sets the column auto increment.
     *
     * @param columnAutoIncrement the column auto increment
     * @param idx                 the idx
     */
    public void setColumnAutoIncrement(int idx, boolean columnAutoIncrement);

    /**
     * Checks if is column case sensitive.
     *
     * @param idx the idx
     * @return true, if is column case sensitive
     */
    public boolean isColumnCaseSensitive(int idx);

    /**
     * Sets the column case sensitive.
     *
     * @param idx                 the idx
     * @param columnCaseSensitive the column case sensitive
     */
    public void setColumnCaseSensitive(int idx, boolean columnCaseSensitive);

    /**
     * Checks if is column currency.
     *
     * @param idx the idx
     * @return true, if is column currency
     */
    public boolean isColumnCurrency(int idx);

    /**
     * Sets the column currency.
     *
     * @param idx            the idx
     * @param columnCurrency the column currency
     */
    public void setColumnCurrency(int idx, boolean columnCurrency);

    /**
     * Checks if is column nullable.
     *
     * @param idx the idx
     * @return the int
     */
    public int isColumnNullable(int idx);

    /**
     * Sets the column nullable.
     *
     * @param idx            the idx
     * @param columnNullable the column nullable
     */
    public void setColumnNullable(int idx, int columnNullable);

    /**
     * Checks if is column searchable.
     *
     * @param idx the idx
     * @return true, if is column searchable
     */
    public boolean isColumnSearchable(int idx);

    /**
     * Sets the column searchable.
     *
     * @param columnSearchable the column searchable
     * @param idx              the idx
     */
    public void setColumnSearchable(int idx, boolean columnSearchable);

    /**
     * Checks if is column signed.
     *
     * @param idx the idx
     * @return true, if is column signed
     */
    public boolean isColumnSigned(int idx);

    /**
     * Sets the column signed.
     *
     * @param columnSigned the column signed
     * @param idx          the idx
     */
    public void setColumnSigned(int idx, boolean columnSigned);

    /**
     * Gets the key columns.
     *
     * @return array of column names
     */
    public String[] getKeyColumns();

    /**
     * Sets the key columns.
     *
     * @param keys the keys
     */
    public void setKeyColumns(String[] keys);
}
