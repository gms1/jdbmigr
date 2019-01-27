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
 * The Class MetaDataImpl.
 */
public class MetaDataImpl implements MetaData {

  /** The command. */
  private String command = null;

  /** The concurrency. */
  private int concurrency = 1007;

  /** The data source. */
  private String dataSource;

  /** The escape processing. */
  private boolean escapeProcessing = true;

  /** The fetch direction. */
  private int fetchDirection = 1000;

  /** The fetch size. */
  private int fetchSize = 0;

  /** The isolation level. */
  private int isolationLevel = 2;

  /** The max field size. */
  private int maxFieldSize = 0;

  /** The max rows. */
  private int maxRows = 0;

  /** The query timeout. */
  private int queryTimeout = 0;

  /** The read only. */
  private boolean readOnly = false;

  /** The row set type. */
  private int rowSetType = 1003;

  /** The show deleted. */
  private boolean showDeleted = true;

  /** The table name. */
  private String tableName = null;

  /** The schema name. */
  private String schemaName = null;

  /** The catalog name. */
  private String catalogName = null;

  /** The url. */
  private String url = null;

  /** The sync provider name. */
  private String syncProviderName =
      "com.sun.rowset.providers.RIOptimisticProvider";

  /** The sync provider vendor. */
  private String syncProviderVendor = "Sun Microsystems Inc.";

  /** The sync provider version. */
  private String syncProviderVersion = "1.0";

  /** The sync provider grade. */
  private String syncProviderGrade = "2";

  /** The data source lock. */
  private String dataSourceLock = "1";

  /** The column count. */
  private int columnCount;

  /** The column display size. */
  private int[] columnDisplaySize;

  /** The column label name. */
  private String[] columnLabelName;

  /** The column name. */
  private String[] columnName;

  /** The column table name. */
  private String[] columnTableName;

  /** The column schema name. */
  private String[] columnSchemaName;

  /** The column catalog name. */
  private String[] columnCatalogName;

  /** The column type. */
  private int[] columnType;

  /** The column type name. */
  private String[] columnTypeName;

  /** The column precision. */
  private int[] columnPrecision;

  /** The column scale. */
  private int[] columnScale;

  /** The column auto increment. */
  private boolean[] columnAutoIncrement;

  /** The column case sensitive. */
  private boolean[] columnCaseSensitive;

  /** The column currency. */
  private boolean[] columnCurrency;

  /** The column nullable. */
  private int[] columnNullable;

  /** The column searchable. */
  private boolean[] columnSearchable;

  /** The column signed. */
  private boolean[] columnSigned;

  /** The keys. */
  private String[] keys;

  /**
   * The Constructor.
   */
  public MetaDataImpl() {}

  /**
   * Init.
   */
  public void init() {

    columnDisplaySize = new int[columnCount];
    columnLabelName = new String[columnCount];
    columnName = new String[columnCount];
    columnTableName = new String[columnCount];
    columnSchemaName = new String[columnCount];
    columnCatalogName = new String[columnCount];
    columnType = new int[columnCount];
    columnTypeName = new String[columnCount];
    columnPrecision = new int[columnCount];
    columnScale = new int[columnCount];
    columnAutoIncrement = new boolean[columnCount];
    columnCaseSensitive = new boolean[columnCount];
    columnCurrency = new boolean[columnCount];
    columnNullable = new int[columnCount];
    columnSearchable = new boolean[columnCount];
    columnSigned = new boolean[columnCount];

    for (int i = 0; i < columnCount; i++) {
      columnDisplaySize[i] = 0;
      columnLabelName[i] = null;
      columnName[i] = null;
      columnTableName[i] = null;
      columnSchemaName[i] = null;
      columnCatalogName[i] = null;
      columnType[i] = DataTypes.UnknownType;
      columnTypeName[i] = DataTypes.UnknownTypeName;
      columnPrecision[i] = 0;
      columnScale[i] = 0;
      columnAutoIncrement[i] = false;
      columnCaseSensitive[i] = false;
      columnCurrency[i] = false;
      columnNullable[i] = 1;
      columnSearchable[i] = true;
      columnSigned[i] = true;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getConcurrency()
   */
  /**
   * Gets the concurrency.
   *
   * @return the concurrency
   */
  public final int getConcurrency() { return concurrency; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setConcurrency(int)
   */
  /**
   * Sets the concurrency.
   *
   * @param concurrency the concurrency
   */
  public final void setConcurrency(int concurrency) {

    this.concurrency = concurrency;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getFetchDirection()
   */
  /**
   * Gets the fetch direction.
   *
   * @return the fetch direction
   */
  public final int getFetchDirection() { return fetchDirection; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setFetchDirection(int)
   */
  /**
   * Sets the fetch direction.
   *
   * @param fetchDirection the fetch direction
   */
  public final void setFetchDirection(int fetchDirection) {

    this.fetchDirection = fetchDirection;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getFetchSize()
   */
  /**
   * Gets the fetch size.
   *
   * @return the fetch size
   */
  public final int getFetchSize() { return fetchSize; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setFetchSize(int)
   */
  /**
   * Sets the fetch size.
   *
   * @param fetchSize the fetch size
   */
  public final void setFetchSize(int fetchSize) { this.fetchSize = fetchSize; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getMaxFieldSize()
   */
  /**
   * Gets the max field size.
   *
   * @return the max field size
   */
  public final int getMaxFieldSize() { return maxFieldSize; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setMaxFieldSize(int)
   */
  /**
   * Sets the max field size.
   *
   * @param maxFieldSize the max field size
   */
  public final void setMaxFieldSize(int maxFieldSize) {

    this.maxFieldSize = maxFieldSize;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getMaxRows()
   */
  /**
   * Gets the max rows.
   *
   * @return the max rows
   */
  public final int getMaxRows() { return maxRows; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setMaxRows(int)
   */
  /**
   * Sets the max rows.
   *
   * @param maxRows the max rows
   */
  public final void setMaxRows(int maxRows) { this.maxRows = maxRows; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getQueryTimeout()
   */
  /**
   * Gets the query timeout.
   *
   * @return the query timeout
   */
  public final int getQueryTimeout() { return queryTimeout; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setQueryTimeout(int)
   */
  /**
   * Sets the query timeout.
   *
   * @param queryTimeout the query timeout
   */
  public final void setQueryTimeout(int queryTimeout) {

    this.queryTimeout = queryTimeout;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getResultSetType()
   */
  /**
   * Gets the row set type.
   *
   * @return the row set type
   */
  public final int getRowSetType() { return rowSetType; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setResultSetType(int)
   */
  /**
   * Sets the row set type.
   *
   * @param resultSetType the result set type
   */
  public final void setRowSetType(int resultSetType) {

    this.rowSetType = resultSetType;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getTransactionIsolation()
   */
  /**
   * Gets the isolation level.
   *
   * @return the isolation level
   */
  public final int getIsolationLevel() { return isolationLevel; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setTransactionIsolation(int)
   */
  /**
   * Sets the isolation level.
   *
   * @param transactionIsolation the transaction isolation
   */
  public final void setIsolationLevel(int transactionIsolation) {

    this.isolationLevel = transactionIsolation;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isReadOnly()
   */
  /**
   * Checks if is read only.
   *
   * @return true, if is read only
   */
  public final boolean isReadOnly() { return readOnly; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setReadOnly(boolean)
   */
  /**
   * Sets the read only.
   *
   * @param readOnly the read only
   */
  public final void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isShowDeleted()
   */
  /**
   * Checks if is show deleted.
   *
   * @return true, if is show deleted
   */
  public final boolean isShowDeleted() { return showDeleted; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setShowDeleted(boolean)
   */
  /**
   * Sets the show deleted.
   *
   * @param showDeleted the show deleted
   */
  public final void setShowDeleted(boolean showDeleted) {

    this.showDeleted = showDeleted;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getTableName()
   */
  /**
   * Gets the table name.
   *
   * @return the table name
   */
  public final String getTableName() { return tableName; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setTableName(java.lang.String)
   */
  /**
   * Sets the table name.
   *
   * @param tableName the table name
   */
  public final void setTableName(String tableName) {

    this.tableName = tableName;
  }

  /**
   * Gets the command.
   *
   * @return the command
   */
  public String getCommand() { return command; }

  /**
   * Sets the command.
   *
   * @param command the command
   */
  public void setCommand(String command) { this.command = command; }

  /**
   * Gets the data source.
   *
   * @return the data source
   */
  public String getDataSource() { return dataSource; }

  /**
   * Sets the data source.
   *
   * @param dataSource the data source
   */
  public void setDataSource(String dataSource) { this.dataSource = dataSource; }

  /**
   * Gets the sync provider name.
   *
   * @return the sync provider name
   */
  public String getSyncProviderName() { return syncProviderName; }

  /**
   * Sets the sync provider name.
   *
   * @param name the name
   */
  public void setSyncProviderName(String name) { this.syncProviderName = name; }

  /**
   * Gets the sync provider vendor.
   *
   * @return the sync provider vendor
   */
  public String getSyncProviderVendor() { return syncProviderVendor; }

  /**
   * Sets the sync provider vendor.
   *
   * @param vendor the vendor
   */
  public void setSyncProviderVendor(String vendor) {

    this.syncProviderVendor = vendor;
  }

  /**
   * Gets the sync provider version.
   *
   * @return the sync provider version
   */
  public String getSyncProviderVersion() { return syncProviderVersion; }

  /**
   * Sets the sync provider version.
   *
   * @param version the version
   */
  public void setSyncProviderVersion(String version) {

    this.syncProviderVersion = version;
  }

  /**
   * Gets the sync provider grade.
   *
   * @return the sync provider grade
   */
  public String getSyncProviderGrade() { return syncProviderGrade; }

  /**
   * Sets the sync provider grade.
   *
   * @param grade the grade
   */
  public void setSyncProviderGrade(String grade) {

    this.syncProviderGrade = grade;
  }

  /**
   * Gets the data source lock.
   *
   * @return the data source lock
   */
  public String getDataSourceLock() { return dataSourceLock; }

  /**
   * Sets the data source lock.
   *
   * @param lock the lock
   */
  public void setDataSourceLock(String lock) { this.dataSourceLock = lock; }

  /**
   * Gets the url.
   *
   * @return the url
   */
  public String getUrl() { return url; }

  /**
   * Sets the url.
   *
   * @param url the url
   */
  public void setUrl(String url) { this.url = url; }

  /**
   * Checks if is escape processing.
   *
   * @return true, if is escape processing
   */
  public boolean isEscapeProcessing() { return escapeProcessing; }

  /**
   * Sets the escape processing.
   *
   * @param escape the escape
   */
  public void setEscapeProcessing(boolean escape) {

    this.escapeProcessing = escape;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnCount()
   */
  /**
   * Gets the column count.
   *
   * @return the column count
   */
  public final int getColumnCount() { return columnCount; }

  /**
   * Sets the column count.
   *
   * @param colCount the col count
   */
  public void setColumnCount(int colCount) {

    this.columnCount = colCount;
    this.init();
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnCatalogName(int)
   */
  /**
   * Gets the column catalog name.
   *
   * @param idx the idx
   *
   * @return the column catalog name
   */
  public final String getColumnCatalogName(int idx) {

    return columnCatalogName[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnCatalogName(int,
   *     java.lang.String)
   */
  /**
   * Sets the column catalog name.
   *
   * @param columnCatalogName the column catalog name
   * @param idx               the idx
   */
  public final void setColumnCatalogName(int idx, String columnCatalogName) {

    this.columnCatalogName[idx - 1] = columnCatalogName;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnDisplaySize(int)
   */
  /**
   * Gets the column display size.
   *
   * @param idx the idx
   *
   * @return the column display size
   */
  public final int getColumnDisplaySize(int idx) {

    return columnDisplaySize[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnDisplaySize(int, int)
   */
  /**
   * Sets the column display size.
   *
   * @param columnDisplaySize the column display size
   * @param idx               the idx
   */
  public final void setColumnDisplaySize(int idx, int columnDisplaySize) {

    this.columnDisplaySize[idx - 1] = columnDisplaySize;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnLabel(int)
   */
  /**
   * Gets the column label.
   *
   * @param idx the idx
   *
   * @return the column label
   */
  public final String getColumnLabel(int idx) {

    return columnLabelName[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnLabelName(int, java.lang.String)
   */
  /**
   * Sets the column label.
   *
   * @param columnLabelName the column label name
   * @param idx             the idx
   */
  public final void setColumnLabel(int idx, String columnLabelName) {

    this.columnLabelName[idx - 1] = columnLabelName;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnName(int)
   */
  /**
   * Gets the column name.
   *
   * @param idx the idx
   *
   * @return the column name
   */
  public final String getColumnName(int idx) { return columnName[idx - 1]; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnName(int, java.lang.String)
   */
  /**
   * Sets the column name.
   *
   * @param idx        the idx
   * @param columnName the column name
   */
  public final void setColumnName(int idx, String columnName) {

    this.columnName[idx - 1] = columnName;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnPrecision(int)
   */
  /**
   * Gets the column precision.
   *
   * @param idx the idx
   *
   * @return the column precision
   */
  public final int getColumnPrecision(int idx) {

    return columnPrecision[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnPrecision(int, int)
   */
  /**
   * Sets the column precision.
   *
   * @param columnPrecision the column precision
   * @param idx             the idx
   */
  public final void setColumnPrecision(int idx, int columnPrecision) {

    this.columnPrecision[idx - 1] = columnPrecision;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnScale(int)
   */
  /**
   * Gets the column scale.
   *
   * @param idx the idx
   *
   * @return the column scale
   */
  public final int getColumnScale(int idx) { return columnScale[idx - 1]; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnScale(int, int)
   */
  /**
   * Sets the column scale.
   *
   * @param columnScale the column scale
   * @param idx         the idx
   */
  public final void setColumnScale(int idx, int columnScale) {

    this.columnScale[idx - 1] = columnScale;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnSchemaName(int)
   */
  /**
   * Gets the column schema name.
   *
   * @param idx the idx
   *
   * @return the column schema name
   */
  public final String getColumnSchemaName(int idx) {

    return columnSchemaName[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnSchemaName(int, java.lang.String)
   */
  /**
   * Sets the column schema name.
   *
   * @param columnSchemaName the column schema name
   * @param idx              the idx
   */
  public final void setColumnSchemaName(int idx, String columnSchemaName) {

    this.columnSchemaName[idx - 1] = columnSchemaName;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnTableName(int)
   */
  /**
   * Gets the column table name.
   *
   * @param idx the idx
   *
   * @return the column table name
   */
  public final String getColumnTableName(int idx) {

    return columnTableName[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnTableName(int, java.lang.String)
   */
  /**
   * Sets the column table name.
   *
   * @param columnTableName the column table name
   * @param idx             the idx
   */
  public final void setColumnTableName(int idx, String columnTableName) {

    this.columnTableName[idx - 1] = columnTableName;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnType(int)
   */
  /**
   * Gets the column type.
   *
   * @param idx the idx
   *
   * @return the column type
   */
  public final int getColumnType(int idx) { return columnType[idx - 1]; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnType(int, int)
   */
  /**
   * Sets the column type.
   *
   * @param columnType the column type
   * @param idx        the idx
   */
  public final void setColumnType(int idx, int columnType) {

    this.columnType[idx - 1] = columnType;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#getColumnTypeName(int)
   */
  /**
   * Gets the column type name.
   *
   * @param idx the idx
   *
   * @return the column type name
   */
  public final String getColumnTypeName(int idx) {

    return columnTypeName[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnTypeName(int, java.lang.String)
   */
  /**
   * Sets the column type name.
   *
   * @param idx            the idx
   * @param columnTypeName the column type name
   */
  public final void setColumnTypeName(int idx, String columnTypeName) {

    this.columnTypeName[idx - 1] = columnTypeName;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isColumnAutoIncrement(int)
   */
  /**
   * Checks if is column auto increment.
   *
   * @param idx the idx
   *
   * @return true, if is column auto increment
   */
  public final boolean isColumnAutoIncrement(int idx) {

    return columnAutoIncrement[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnAutoIncrement(int, boolean)
   */
  /**
   * Sets the column auto increment.
   *
   * @param columnAutoIncrement the column auto increment
   * @param idx                 the idx
   */
  public final void setColumnAutoIncrement(int idx,
                                           boolean columnAutoIncrement) {

    this.columnAutoIncrement[idx - 1] = columnAutoIncrement;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isColumnCaseSensitive(int)
   */
  /**
   * Checks if is column case sensitive.
   *
   * @param idx the idx
   *
   * @return true, if is column case sensitive
   */
  public final boolean isColumnCaseSensitive(int idx) {

    return columnCaseSensitive[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnCaseSensitive(int, boolean)
   */
  /**
   * Sets the column case sensitive.
   *
   * @param idx                 the idx
   * @param columnCaseSensitive the column case sensitive
   */
  public final void setColumnCaseSensitive(int idx,
                                           boolean columnCaseSensitive) {

    this.columnCaseSensitive[idx - 1] = columnCaseSensitive;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isColumnCurrency(int)
   */
  /**
   * Checks if is column currency.
   *
   * @param idx the idx
   *
   * @return true, if is column currency
   */
  public final boolean isColumnCurrency(int idx) {

    return columnCurrency[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnCurrency(int, boolean)
   */
  /**
   * Sets the column currency.
   *
   * @param idx            the idx
   * @param columnCurrency the column currency
   */
  public final void setColumnCurrency(int idx, boolean columnCurrency) {

    this.columnCurrency[idx - 1] = columnCurrency;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isColumnNullable(int)
   */
  /**
   * Checks if is column nullable.
   *
   * @param idx the idx
   *
   * @return the int
   */
  public final int isColumnNullable(int idx) { return 0; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnNullable(int, int)
   */
  /**
   * Sets the column nullable.
   *
   * @param idx            the idx
   * @param columnNullable the column nullable
   */
  public final void setColumnNullable(int idx, int columnNullable) {

    this.columnNullable[idx - 1] = columnNullable;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isColumnSearchable(int)
   */
  /**
   * Checks if is column searchable.
   *
   * @param idx the idx
   *
   * @return true, if is column searchable
   */
  public final boolean isColumnSearchable(int idx) {

    return columnSearchable[idx - 1];
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnSearchable(int, boolean)
   */
  /**
   * Sets the column searchable.
   *
   * @param columnSearchable the column searchable
   * @param idx              the idx
   */
  public final void setColumnSearchable(int idx, boolean columnSearchable) {

    this.columnSearchable[idx - 1] = columnSearchable;
  }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#isColumnSigned(int)
   */
  /**
   * Checks if is column signed.
   *
   * @param idx the idx
   *
   * @return true, if is column signed
   */
  public final boolean isColumnSigned(int idx) { return columnSigned[idx - 1]; }

  /*
   * (non-Javadoc)
   *
   * @see net.sf.gm.core.io.MetaDataX#setColumnSigned(int, boolean)
   */
  /**
   * Sets the column signed.
   *
   * @param columnSigned the column signed
   * @param idx          the idx
   */
  public final void setColumnSigned(int idx, boolean columnSigned) {

    this.columnSigned[idx - 1] = columnSigned;
  }

  /**
   * Gets the key columns.
   *
   * @return the key columns
   */
  public String[] getKeyColumns() { return keys; }

  /**
   * Sets the key columns.
   *
   * @param keys the keys
   */
  public void setKeyColumns(String[] keys) { this.keys = keys; }

  /**
   * Gets the schema name.
   *
   * @return the schema name
   */
  public String getSchemaName() { return schemaName; }

  /**
   * Sets the schema name.
   *
   * @param schemaName the schema name
   */
  public void setSchemaName(String schemaName) { this.schemaName = schemaName; }

  /**
   * Gets the catalog name.
   *
   * @return the catalog name
   */
  public String getCatalogName() { return catalogName; }

  /**
   * Sets the catalog name.
   *
   * @param catalogName the catalog name
   */
  public void setCatalogName(String catalogName) {

    this.catalogName = catalogName;
  }
}
