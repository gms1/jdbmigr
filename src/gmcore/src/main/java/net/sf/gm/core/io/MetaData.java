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
    String getCommand();

    /**
     * Sets the command.
     *
     * @param command the command
     */
    void setCommand(String command);

    /**
     * Gets the concurrency.
     *
     * @return the concurrency
     */
    int getConcurrency();

    /**
     * Sets the concurrency.
     *
     * @param concurrency the concurrency
     */
    void setConcurrency(int concurrency);

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    String getDataSource();

    /**
     * Sets the data source.
     *
     * @param dataSource the data source
     */
    void setDataSource(String dataSource);

    /**
     * Checks if is escape processing.
     *
     * @return true, if is escape processing
     */
    boolean isEscapeProcessing();

    /**
     * Sets the escape processing.
     *
     * @param escape the escape
     */
    void setEscapeProcessing(boolean escape);

    /**
     * Gets the fetch direction.
     *
     * @return the fetch direction
     */
    int getFetchDirection();

    /**
     * Sets the fetch direction.
     *
     * @param fetchDirection the fetch direction
     */
    void setFetchDirection(int fetchDirection);

    /**
     * Gets the fetch size.
     *
     * @return the fetch size
     */
    int getFetchSize();

    /**
     * Sets the fetch size.
     *
     * @param fetchSize the fetch size
     */
    void setFetchSize(int fetchSize);

    /**
     * Gets the isolation level.
     *
     * @return the isolation level
     */
    int getIsolationLevel();

    /**
     * Sets the isolation level.
     *
     * @param transactionIsolation the transaction isolation
     */
    void setIsolationLevel(int transactionIsolation);

    /**
     * Gets the max field size.
     *
     * @return the max field size
     */
    int getMaxFieldSize();

    /**
     * Sets the max field size.
     *
     * @param maxFieldSize the max field size
     */
    void setMaxFieldSize(int maxFieldSize);

    /**
     * Gets the max rows.
     *
     * @return the max rows
     */
    int getMaxRows();

    /**
     * Sets the max rows.
     *
     * @param maxRows the max rows
     */
    void setMaxRows(int maxRows);

    /**
     * Gets the query timeout.
     *
     * @return the query timeout
     */
    int getQueryTimeout();

    /**
     * Sets the query timeout.
     *
     * @param queryTimeout the query timeout
     */
    void setQueryTimeout(int queryTimeout);

    /**
     * Checks if is read only.
     *
     * @return true, if is read only
     */
    boolean isReadOnly();

    /**
     * Sets the read only.
     *
     * @param readOnly the read only
     */
    void setReadOnly(boolean readOnly);

    /**
     * Gets the row set type.
     *
     * @return the row set type
     */
    int getRowSetType();

    /**
     * Sets the row set type.
     *
     * @param resultSetType the result set type
     */
    void setRowSetType(int resultSetType);

    /**
     * Checks if is show deleted.
     *
     * @return true, if is show deleted
     */
    boolean isShowDeleted();

    /**
     * Sets the show deleted.
     *
     * @param showDeleted the show deleted
     */
    void setShowDeleted(boolean showDeleted);

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    String getTableName();

    /**
     * Sets the table name.
     *
     * @param tableName the table name
     */
    void setTableName(String tableName);

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    String getSchemaName();

    /**
     * Sets the table name.
     *
     * @param schemaName the table name
     */
    void setSchemaName(String schemaName);

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    String getCatalogName();

    /**
     * Sets the table name.
     *
     * @param catalogName the table name
     */
    void setCatalogName(String catalogName);

    /**
     * Gets the url.
     *
     * @return the url
     */
    String getUrl();

    /**
     * Sets the url.
     *
     * @param url the url
     */
    void setUrl(String url);

    /**
     * Gets the sync provider name.
     *
     * @return the sync provider name
     */
    String getSyncProviderName();

    /**
     * Sets the sync provider name.
     *
     * @param name the name
     */
    void setSyncProviderName(String name);

    /**
     * Gets the sync provider vendor.
     *
     * @return the sync provider vendor
     */
    String getSyncProviderVendor();

    /**
     * Sets the sync provider vendor.
     *
     * @param vendor the vendor
     */
    void setSyncProviderVendor(String vendor);

    /**
     * Gets the sync provider version.
     *
     * @return the sync provider version
     */
    String getSyncProviderVersion();

    /**
     * Sets the sync provider version.
     *
     * @param version the version
     */
    void setSyncProviderVersion(String version);

    /**
     * Gets the sync provider grade.
     *
     * @return the sync provider grade
     */
    String getSyncProviderGrade();

    /**
     * Sets the sync provider grade.
     *
     * @param grade the grade
     */
    void setSyncProviderGrade(String grade);

    /**
     * Gets the data source lock.
     *
     * @return the data source lock
     */
    String getDataSourceLock();

    /**
     * Sets the data source lock.
     *
     * @param lock the lock
     */
    void setDataSourceLock(String lock);

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    int getColumnCount();

    /**
     * Sets the column count.
     *
     * @param colCount the col count
     */
    void setColumnCount(int colCount);

    /**
     * Gets the column catalog name.
     *
     * @param idx the idx
     * @return the column catalog name
     */
    String getColumnCatalogName(int idx);

    /**
     * Sets the column catalog name.
     *
     * @param columnCatalogName the column catalog name
     * @param idx               the idx
     */
    void setColumnCatalogName(int idx, String columnCatalogName);

    /**
     * Gets the column display size.
     *
     * @param idx the idx
     * @return the column display size
     */
    int getColumnDisplaySize(int idx);

    /**
     * Sets the column display size.
     *
     * @param columnDisplaySize the column display size
     * @param idx               the idx
     */
    void setColumnDisplaySize(int idx, int columnDisplaySize);

    /**
     * Gets the column label.
     *
     * @param idx the idx
     * @return the column label
     */
    String getColumnLabel(int idx);

    /**
     * Sets the column label.
     *
     * @param columnLabelName the column label name
     * @param idx             the idx
     */
    void setColumnLabel(int idx, String columnLabelName);

    /**
     * Gets the column name.
     *
     * @param idx the idx
     * @return the column name
     */
    String getColumnName(int idx);

    /**
     * Sets the column name.
     *
     * @param idx        the idx
     * @param columnName the column name
     */
    void setColumnName(int idx, String columnName);

    /**
     * Gets the column precision.
     *
     * @param idx the idx
     * @return the column precision
     */
    int getColumnPrecision(int idx);

    /**
     * Sets the column precision.
     *
     * @param columnPrecision the column precision
     * @param idx             the idx
     */
    void setColumnPrecision(int idx, int columnPrecision);

    /**
     * Gets the column scale.
     *
     * @param idx the idx
     * @return the column scale
     */
    int getColumnScale(int idx);

    /**
     * Sets the column scale.
     *
     * @param columnScale the column scale
     * @param idx         the idx
     */
    void setColumnScale(int idx, int columnScale);

    /**
     * Gets the column schema name.
     *
     * @param idx the idx
     * @return the column schema name
     */
    String getColumnSchemaName(int idx);

    /**
     * Sets the column schema name.
     *
     * @param columnSchemaName the column schema name
     * @param idx              the idx
     */
    void setColumnSchemaName(int idx, String columnSchemaName);

    /**
     * Gets the column table name.
     *
     * @param idx the idx
     * @return the column table name
     */
    String getColumnTableName(int idx);

    /**
     * Sets the column table name.
     *
     * @param columnTableName the column table name
     * @param idx             the idx
     */
    void setColumnTableName(int idx, String columnTableName);

    /**
     * Gets the column type.
     *
     * @param idx the idx
     * @return the column type
     */
    int getColumnType(int idx);

    /**
     * Sets the column type.
     *
     * @param columnType the column type
     * @param idx        the idx
     */
    void setColumnType(int idx, int columnType);

    /**
     * Gets the column type name.
     *
     * @param idx the idx
     * @return the column type name
     */
    String getColumnTypeName(int idx);

    /**
     * Sets the column type name.
     *
     * @param idx            the idx
     * @param columnTypeName the column type name
     */
    void setColumnTypeName(int idx, String columnTypeName);

    /**
     * Checks if is column auto increment.
     *
     * @param idx the idx
     * @return true, if is column auto increment
     */
    boolean isColumnAutoIncrement(int idx);

    /**
     * Sets the column auto increment.
     *
     * @param columnAutoIncrement the column auto increment
     * @param idx                 the idx
     */
    void setColumnAutoIncrement(int idx, boolean columnAutoIncrement);

    /**
     * Checks if is column case sensitive.
     *
     * @param idx the idx
     * @return true, if is column case sensitive
     */
    boolean isColumnCaseSensitive(int idx);

    /**
     * Sets the column case sensitive.
     *
     * @param idx                 the idx
     * @param columnCaseSensitive the column case sensitive
     */
    void setColumnCaseSensitive(int idx, boolean columnCaseSensitive);

    /**
     * Checks if is column currency.
     *
     * @param idx the idx
     * @return true, if is column currency
     */
    boolean isColumnCurrency(int idx);

    /**
     * Sets the column currency.
     *
     * @param idx            the idx
     * @param columnCurrency the column currency
     */
    void setColumnCurrency(int idx, boolean columnCurrency);

    /**
     * Checks if is column nullable.
     *
     * @param idx the idx
     * @return the int
     */
    int isColumnNullable(int idx);

    /**
     * Sets the column nullable.
     *
     * @param idx            the idx
     * @param columnNullable the column nullable
     */
    void setColumnNullable(int idx, int columnNullable);

    /**
     * Checks if is column searchable.
     *
     * @param idx the idx
     * @return true, if is column searchable
     */
    boolean isColumnSearchable(int idx);

    /**
     * Sets the column searchable.
     *
     * @param columnSearchable the column searchable
     * @param idx              the idx
     */
    void setColumnSearchable(int idx, boolean columnSearchable);

    /**
     * Checks if is column signed.
     *
     * @param idx the idx
     * @return true, if is column signed
     */
    boolean isColumnSigned(int idx);

    /**
     * Sets the column signed.
     *
     * @param columnSigned the column signed
     * @param idx          the idx
     */
    void setColumnSigned(int idx, boolean columnSigned);

    /**
     * Gets the key columns.
     *
     * @return array of column names
     */
    String[] getKeyColumns();

    /**
     * Sets the key columns.
     *
     * @param keys the keys
     */
    void setKeyColumns(String[] keys);
}
