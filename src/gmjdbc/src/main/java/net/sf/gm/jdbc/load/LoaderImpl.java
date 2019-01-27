/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.load;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReader;
import net.sf.gm.core.io.DataTypes;
import net.sf.gm.core.io.DataWriterAbstract;
import net.sf.gm.core.io.MetaData;
import net.sf.gm.core.io.DataTypes.rowType;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.jdbc.common.GMJDBCProperties;
import net.sf.gm.jdbc.common.SQLState;
import net.sf.gm.jdbc.common.SqlUtil;
import net.sf.gm.jdbc.io.TableDef;

//
/**
 * The Class LoaderImpl.
 */
public class LoaderImpl extends DataWriterAbstract implements Loader {

  /** The con. */
  private Connection con;

  /** The all row count. */
  private long allRowCount;

  /** The ignored row count. */
  private long ignoredRowCount;

  /** The failed row count. */
  private long failedRowCount;

  /** The written row count. */
  private long writtenRowCount;

  /** The full table name. */
  private String fullTableName;

  /** The max batch size. */
  private int maxBatchSize;

  /** The max commit size. */
  private int maxCommitSize;

  /** The map columns by names. */
  private boolean mapColumnsByNames;

  /** The skip columns not found. */
  private boolean skipColumnsNotFound;

  /** The do import. */
  private boolean doImport;

  /** The doSync. */
  private boolean doSync;

  /** The meta data. */
  private MetaData metaData;

  /** The db column names. */
  private String[] dbColumnNames;

  /** The db column types. */
  private int[] dbColumnTypes;

  /** The db primary key column count. */
  private int dbPrimaryKeyColumnCount;

  /** The db column is primary key. */
  private boolean[] dbColumnIsPrimaryKey;

  /** The current commit size. */
  private int currentCommitSize;

  String syncErrorCause;

  // current operation

  /** The current row type. */
  private rowType currentRowType;

  /** The current operation. */
  private String currentOperation;

  /** The current batch size. */
  private int currentBatchSize;

  /** The load statement. */
  private PreparedStatement currentRowStmt;

  /** The current column map. */
  private int[] currentColumnMap;

  // import/insert row operation

  /** The import statement. */
  private PreparedStatement insertRowStmt;

  /** The insert column map. */
  private int[] insertColumnMap;

  // update/modify row operation

  /** The modify row statement. */
  private PreparedStatement updateRowStmt;

  /** The update column map. */
  private int[] updateColumnMap;

  // delete row operation

  /** The delete row statement. */
  private PreparedStatement deleteRowStmt;

  /** The delete column map. */
  private int[] deleteColumnMap;

  // delete all operation

  /** The delete statement. */
  private Statement deleteAllStmt;

  /**
   * The Constructor.
   *
   * @param doImport            the do import
   * @param doSync              the do sync
   * @param maxCommitSize       the max commit size
   * @param mapColumnsByNames   the map columns by names
   * @param con                 the con
   * @param skipColumnsNotFound the skip columns not found
   * @param progress            the progress
   * @param maxBatchSize        the max batch size
   *
   * @throws SQLException the SQL exception
   */
  public LoaderImpl(final Progress progress, final Connection con,
                    final int maxBatchSize, final int maxCommitSize,
                    final boolean mapColumnsByNames,
                    final boolean skipColumnsNotFound, final boolean doImport,
                    final boolean doSync) throws SQLException {

    super(progress);
    currentRowStmt = null;
    currentColumnMap = null;
    insertRowStmt = null;
    insertColumnMap = null;
    updateRowStmt = null;
    updateColumnMap = null;
    deleteRowStmt = null;
    deleteColumnMap = null;
    deleteAllStmt = null;
    setConnection(con);
    setMaxBatchSize(maxBatchSize);
    setCommitSize(maxCommitSize);
    setMapColumnsByNames(mapColumnsByNames);
    setSkipColumnsNotFound(skipColumnsNotFound);
    setDoImport(doImport);
    setDoSync(doSync);
    if (!con.getMetaData().supportsBatchUpdates())
      throw new SQLException(
          "batch processing is not supported by the selected driver");
  }

  /**
   * close load statements.
   */
  protected void closeLoadStatements() {

    currentRowStmt = null;
    currentColumnMap = null;
    insertColumnMap = null;
    updateColumnMap = null;
    deleteColumnMap = null;
    if (insertRowStmt != null) {
      SqlUtil.closePreparedStatement(insertRowStmt);
      insertRowStmt = null;
    }
    if (updateRowStmt != null) {
      SqlUtil.closePreparedStatement(updateRowStmt);
      updateRowStmt = null;
    }
    if (deleteRowStmt != null) {
      SqlUtil.closePreparedStatement(deleteRowStmt);
      deleteRowStmt = null;
    }
  }

  /**
   * close delete-all statements.
   */
  protected void closeDeleteAllStatement() {

    if (deleteAllStmt != null) {
      SqlUtil.closeStatement(deleteAllStmt);
      deleteAllStmt = null;
    }
  }

  /**
   * Start loading.
   *
   * @param tableName   the table name
   * @param schemaName  the schema name
   * @param catalogName the catalog name
   */
  public void startLoading(String tableName, String schemaName,
                           String catalogName) {

    closeLoadStatements();
    allRowCount = 0;
    ignoredRowCount = 0;
    failedRowCount = 0;
    writtenRowCount = 0;
    currentBatchSize = 0;
    currentCommitSize = 0;
    currentRowType = rowType.UNKNOWN;
    setFullTableName(tableName, schemaName, catalogName);
  }

  /**
   * End loading.
   */
  public void endLoading() { closeLoadStatements(); }

  /**
   * Close.
   */
  public void close() {

    closeLoadStatements();
    closeDeleteAllStatement();
    con = null;
  }

  /**
   * Delete.
   *
   * @param tableName   the table name
   * @param schemaName  the schema name
   * @param catalogName the catalog name
   *
   * @return the long
   *
   * @throws SQLException the SQL exception
   */
  public long delete(String tableName, String schemaName, String catalogName)
      throws SQLException {

    if (deleteAllStmt == null)
      deleteAllStmt = con.createStatement();
    setFullTableName(tableName, schemaName, catalogName);
    StringBuilder sb = new StringBuilder();
    sb.append("delete from ");
    sb.append(fullTableName);
    deleteAllStmt.executeUpdate(sb.toString());
    final long res = deleteAllStmt.getUpdateCount();
    con.commit();
    return res;
  }

  /**
   * Gets the column info from database.
   *
   * @throws SQLException    the SQL exception
   * @throws DataIOException the data IO exception
   */
  protected void getColumnInfoFromDatabase()
      throws SQLException, DataIOException {

    // use select statement to get column names and types
    // final StringBuilder selectStmtSB = new StringBuilder();
    // selectStmtSB.append( "select * from " );
    // selectStmtSB.append( fullTableName );
    // selectStmtSB.append( " where 1=0" );
    // final Statement selStmt = con.createStatement();
    // final ResultSet selRS = selStmt.executeQuery( selectStmtSB.toString() );
    // final ResultSetMetaData selRSMD = selRS.getMetaData();
    // int dbColumnCount = selRSMD.getColumnCount();
    // dbColumnNames = new String[dbColumnCount];
    // dbColumnTypes = new int[dbColumnCount];
    // for ( int idx = 1; i <= dbColumnCount; idx++ ) {
    // dbColumnNames[idx - 1] = selRSMD.getColumnName( idx );
    // dbColumnTypes[idx - 1] = selRSMD.getColumnType( idx );
    // }
    // selRS.close();
    // selStmt.close();

    // use DatabaseMetaData to get column names and types

    TableDef td = new TableDef(fullTableName);
    dbColumnNames = td.getColumnNames(con);
    dbColumnTypes = td.getColumnTypes(con);

    // required for update/delete
    String[] dbPrimaryKeyColumns = td.getPrimaryKeys(con);
    dbPrimaryKeyColumnCount = dbPrimaryKeyColumns.length;

    dbColumnIsPrimaryKey = new boolean[dbColumnNames.length];

    int i;
    for (i = 0; i < dbColumnIsPrimaryKey.length; i++)
      dbColumnIsPrimaryKey[i] = false;

    for (String key : dbPrimaryKeyColumns) {

      for (i = 0; i < dbColumnNames.length; i++) {
        if (key.equals(dbColumnNames[i])) {
          dbColumnIsPrimaryKey[i] = true;
          break;
        }
      }
      if (i == dbColumnNames.length)
        throw new DataIOException("primary key column '" + key +
                                  "' is not a database column");
    }

    con.commit();
  }

  /**
   * Open data writing.
   *
   * @param metaData the meta data
   *
   * @throws DataIOException the data IO exception
   */
  public void openDataWriting(MetaData metaData) throws DataIOException {

    this.metaData = metaData;
    try {
      final int readColumnCount = metaData.getColumnCount();

      // get column info from database
      getColumnInfoFromDatabase();
      final Map<String, Integer> mapDbColumns =
          new HashMap<String, Integer>(dbColumnNames.length);

      // map the column name to the db column index
      if (dbColumnNames.length == 0)
        throw new DataIOException("table '" + fullTableName + "' not found");

      Integer dbColIdx;
      for (int idx = 1; idx <= dbColumnNames.length; idx++) {
        final String colName = dbColumnNames[idx - 1].toUpperCase();
        dbColIdx = idx;
        mapDbColumns.put(colName, dbColIdx);
      }

      int foundPrimaryKeyColumnCount = 0;
      int foundNonPrimaryKeyColumnCount = 0;
      boolean[] inputColumnIsPrimaryKey = new boolean[readColumnCount];

      // set column mapping for insert statemet
      int insertColumnCount = readColumnCount;
      insertColumnMap = new int[readColumnCount];
      if (mapColumnsByNames) {
        insertColumnCount = 0;

        for (int idx = 1; idx <= readColumnCount; idx++) {
          inputColumnIsPrimaryKey[idx - 1] = false;

          final String colName = metaData.getColumnName(idx);
          if (colName == null || colName.length() == 0)
            throw new DataIOException(
                "column name not found for input column " + idx);
          dbColIdx = mapDbColumns.get(colName.toUpperCase());
          if (dbColIdx != null) {
            insertColumnMap[idx - 1] = ++insertColumnCount;
            if (metaData.getColumnType(idx) == DataTypes.UnknownType) {
              metaData.setColumnType(idx, dbColumnTypes[dbColIdx - 1]);
              metaData.setColumnTypeName(idx, dbColumnNames[dbColIdx - 1]);
            }
            if (dbColumnIsPrimaryKey[dbColIdx - 1]) {
              foundPrimaryKeyColumnCount++;
              inputColumnIsPrimaryKey[idx - 1] = true;
            } else
              foundNonPrimaryKeyColumnCount++;
          } else {
            if (!skipColumnsNotFound)
              throw new DataIOException("column " + idx + " (name='" + colName +
                                        "') not found in database ");
            insertColumnMap[idx - 1] = 0;
          }
        }

      } else {
        if (!skipColumnsNotFound && readColumnCount > dbColumnNames.length)
          throw new DataIOException("column " + (dbColumnNames.length + 1) +
                                    " not found in database ");
        insertColumnCount = Math.min(dbColumnNames.length, readColumnCount);
        int idx;
        for (idx = 1; idx <= insertColumnCount; idx++) {
          insertColumnMap[idx - 1] = idx;
          inputColumnIsPrimaryKey[idx - 1] = false;
          if (metaData.getColumnType(idx) == DataTypes.UnknownType) {
            metaData.setColumnType(idx, dbColumnTypes[idx - 1]);
            metaData.setColumnTypeName(idx, dbColumnNames[idx - 1]);
          }
          if (dbColumnIsPrimaryKey[idx - 1]) {
            foundPrimaryKeyColumnCount++;
            inputColumnIsPrimaryKey[idx - 1] = true;
          } else
            foundNonPrimaryKeyColumnCount++;
        }
        for (; idx <= readColumnCount; idx++) {
          insertColumnMap[idx - 1] = 0;
        }
      }

      // build the insert statement text
      final StringBuilder insertStmtSB = new StringBuilder();
      insertStmtSB.append("insert into ");
      insertStmtSB.append(fullTableName);

      if (mapColumnsByNames) {
        insertStmtSB.append(" ( ");
        for (int idxStmt = 0, idx = 1; idx <= readColumnCount; idx++) {
          if (insertColumnMap[idx - 1] <= 0)
            continue;
          if (++idxStmt > 1)
            insertStmtSB.append(", ");
          insertStmtSB.append(metaData.getColumnName(idx));
        }
        insertStmtSB.append(" ) ");
      }
      insertStmtSB.append(" values (");
      for (int idxStmt = 0, idx = 1; idx <= insertColumnCount; idx++) {
        if (++idxStmt > 1)
          insertStmtSB.append(", ");
        insertStmtSB.append("?");
      }
      insertStmtSB.append(")");

      insertRowStmt = con.prepareStatement(insertStmtSB.toString());

      syncErrorCause = null;
      if (!doSync)
        return;

      if (!mapColumnsByNames) {
        syncErrorCause =
            "synchronize not supported using 'map columns by column id'";
        return;
      }
      if (dbPrimaryKeyColumnCount != foundPrimaryKeyColumnCount) {
        syncErrorCause =
            "primary key is not included in the set of input columns";
        return;
      }

      // build the delete statement
      deleteColumnMap = new int[readColumnCount];
      int deleteColumnCount = 0;
      for (int idx = 1; idx <= readColumnCount; idx++) {
        if (insertColumnMap[idx - 1] <= 0)
          continue;
        if (inputColumnIsPrimaryKey[idx - 1])
          deleteColumnMap[idx - 1] = ++deleteColumnCount;
        else
          deleteColumnMap[idx - 1] = 0;
      }
      if (deleteColumnCount != foundPrimaryKeyColumnCount) {
        syncErrorCause = "internal error creating delete-statement";
        return;
      }

      final StringBuilder deleteStmtSB = new StringBuilder();
      deleteStmtSB.append("delete from ");
      deleteStmtSB.append(fullTableName);
      deleteStmtSB.append(" where ");
      for (int idxStmt = 0, idx = 1; idx <= readColumnCount; idx++) {
        if (deleteColumnMap[idx - 1] <= 0)
          continue;
        if (++idxStmt > 1)
          deleteStmtSB.append("and ");
        deleteStmtSB.append(metaData.getColumnName(idx));
        deleteStmtSB.append(" = ? ");
      }
      deleteRowStmt = con.prepareStatement(deleteStmtSB.toString());

      if (foundNonPrimaryKeyColumnCount == 0) {
        syncErrorCause = "no columns selected for update";
        return;
      }

      // build the update statement
      updateColumnMap = new int[readColumnCount];
      int updateColumnCount = 0;
      for (int idx = 1; idx <= readColumnCount; idx++) {
        if (insertColumnMap[idx - 1] <= 0)
          continue;
        if (!inputColumnIsPrimaryKey[idx - 1])
          updateColumnMap[idx - 1] = ++updateColumnCount;
      }
      if (updateColumnCount != foundNonPrimaryKeyColumnCount) {
        syncErrorCause =
            "internal error creating set-clause for update-statement";
        return;
      }
      for (int idx = 1; idx <= readColumnCount; idx++) {
        if (insertColumnMap[idx - 1] <= 0)
          continue;
        if (inputColumnIsPrimaryKey[idx - 1])
          updateColumnMap[idx - 1] = ++updateColumnCount;
      }
      if (updateColumnCount !=
          foundPrimaryKeyColumnCount + foundNonPrimaryKeyColumnCount) {
        syncErrorCause =
            "internal error creating where-clause for update-statement";
        return;
      }
      final StringBuilder updateStmtSB = new StringBuilder();
      updateStmtSB.append("update ");
      updateStmtSB.append(fullTableName);
      updateStmtSB.append(" set ");
      for (int idxStmt = 0, idx = 1; idx <= readColumnCount; idx++) {
        if (updateColumnMap[idx - 1] <= 0 ||
            updateColumnMap[idx - 1] > foundNonPrimaryKeyColumnCount)
          continue;
        if (++idxStmt > 1)
          updateStmtSB.append(", ");
        updateStmtSB.append(metaData.getColumnName(idx));
        updateStmtSB.append(" = ? ");
      }
      updateStmtSB.append(" where ");
      for (int idxStmt = 0, idx = 1; idx <= readColumnCount; idx++) {
        if (updateColumnMap[idx - 1] <= 0 ||
            updateColumnMap[idx - 1] <= foundNonPrimaryKeyColumnCount)
          continue;
        if (++idxStmt > 1)
          updateStmtSB.append("and ");
        updateStmtSB.append(metaData.getColumnName(idx));
        updateStmtSB.append(" = ? ");
      }
      updateRowStmt = con.prepareStatement(updateStmtSB.toString());
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Close data writing.
   *
   * @throws DataIOException the data IO exception
   */
  public void closeDataWriting() throws DataIOException {

    try {
      doExecuteCurrentBatch();
      commit();
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Sets the full table name.
   *
   * @param tableName   the table name
   * @param schemaName  the schema name
   * @param catalogName the catalog name
   */
  protected void setFullTableName(String tableName, String schemaName,
                                  String catalogName) {

    StringBuilder sb = new StringBuilder();

    if (catalogName != null) {
      sb.append(catalogName);
      sb.append(".");
    }
    if (schemaName != null) {
      sb.append(schemaName);
      sb.append(".");
    }
    sb.append(tableName);
    this.fullTableName = sb.toString();
  }

  /**
   * Gets the row failed count.
   *
   * @return the row failed count
   */
  public long getRowFailedCount() { return failedRowCount; }

  /**
   * Gets the row written count.
   *
   * @return the row written count
   */
  public long getRowWrittenCount() { return writtenRowCount; }

  /**
   * Gets the row ignored count.
   *
   * @return the row ignored count
   */
  public long getRowIgnoredCount() { return ignoredRowCount; }

  /**
   * Gets the row processed count.
   *
   * @return the row processed count
   */
  public long getRowProcessedCount() { return allRowCount - ignoredRowCount; }

  /**
   * Gets the all row count.
   *
   * @return the all row count
   */
  public long getAllRowCount() { return allRowCount; }

  /**
   * Rollback.
   *
   * @throws SQLException the SQL exception
   */
  public void rollback() throws SQLException {

    con.rollback();
    currentCommitSize = 0;
  }

  /**
   * Commit.
   *
   * @throws SQLException the SQL exception
   */
  public void commit() throws SQLException {

    con.commit();
    writtenRowCount += currentCommitSize;
    currentCommitSize = 0;
  }

  /**
   * Sets the commit size.
   *
   * @param commitSize the commit size
   */
  public void setCommitSize(int commitSize) { this.maxCommitSize = commitSize; }

  /**
   * Sets the connection.
   *
   * @param con the con
   *
   * @throws SQLException the SQL exception
   */
  public void setConnection(Connection con) throws SQLException {

    this.con = con;
  }

  /**
   * Sets the map columns by names.
   *
   * @param mapByName the map by name
   */
  public void setMapColumnsByNames(boolean mapByName) {

    this.mapColumnsByNames = mapByName;
  }

  /**
   * Sets the max batch size.
   *
   * @param maxBatchSize the max batch size
   */
  public void setMaxBatchSize(int maxBatchSize) {

    this.maxBatchSize = maxBatchSize;
  }

  /**
   * Sets the skip columns not found.
   *
   * @param skipNotFound the skip not found
   */
  public void setSkipColumnsNotFound(boolean skipNotFound) {

    this.skipColumnsNotFound = skipNotFound;
  }

  /**
   * Sets the do import.
   *
   * @param doImport the do import
   */
  public void setDoImport(boolean doImport) { this.doImport = doImport; }

  /**
   * Sets the do sync.
   *
   * @param doSync the do sync
   */
  public void setDoSync(boolean doSync) { this.doSync = doSync; }

  /**
   * Start row writing.
   *
   * @param type the type
   *
   * @throws DataIOException the data IO exception
   */
  public void startRowWriting(rowType type) throws DataIOException {

    allRowCount++;
    if (this.currentRowType == type)
      return;

    try {
      // to keep the right order we have to execute the current batch
      doExecuteCurrentBatch();
      this.currentRowType = type;
      currentRowStmt = null;
      currentColumnMap = null;
      switch (currentRowType) {
      case CURRENT:
        if (doImport) {
          currentRowStmt = insertRowStmt;
          currentColumnMap = insertColumnMap;
        }
        currentOperation = "import";
        break;
      case INSERT:
        if (doSync) {
          currentRowStmt = doSync ? insertRowStmt : null;
          currentColumnMap = insertColumnMap;
        }
        currentOperation = "insert";
        break;
      case UPDATE:
        if (doSync) {
          currentRowStmt = updateRowStmt;
          currentColumnMap = updateColumnMap;
        }
        currentOperation = "update";
        break;
      case DELETE:
        if (doSync) {
          currentRowStmt = deleteRowStmt;
          currentColumnMap = deleteColumnMap;
        }
        currentOperation = "delete";
        break;
      default:
        throw new DataIOException("internal error: unknown row type");
      }
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * End row writing.
   *
   * @throws DataIOException the data IO exception
   */
  public void endRowWriting() throws DataIOException { executeRow(); }

  /**
   * Sets the column value.
   *
   * @param idx    the idx
   * @param reader the reader
   *
   * @throws DataIOException the data IO exception
   */
  public void setColumnValue(int idx, DataReader reader)
      throws DataIOException {

    if ((!doImport && currentRowType == rowType.CURRENT) ||
        (!doSync && currentRowType != rowType.CURRENT))
      return; // ignored

    if (currentRowStmt == null)
      return;

    final int idxLoad = currentColumnMap[idx - 1];

    if (idxLoad <= 0)
      return;

    try {
      int iType = metaData.getColumnType(idx);
      String sType = metaData.getColumnTypeName(idx);
      if (iType == DataTypes.UnknownType) {
        try {
          ParameterMetaData pmd = currentRowStmt.getParameterMetaData();
          iType = pmd.getParameterType(idxLoad);
          sType = pmd.getParameterTypeName(idxLoad);
        } catch (SQLException e) {
          // Oracle 10.2: getParameterType is not supported
          iType = Types.VARCHAR;
          sType = "VARCHAR";
        }
      }
      if (iType == Types.NULL) {
        iType = GMJDBCProperties.getJdbcTypes().NativeTypetoInt(sType);
        if (iType != Types.NULL)
          sType = GMJDBCProperties.getJdbcTypes().toString(iType);
      }
      switch (iType) {
      case Types.BOOLEAN: { // "true" or "false"
        final boolean value = reader.getColumnValueBoolean(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setBoolean(idxLoad, value);
      } break;
      case Types.BIT: {
        // "true" or "false"
        final short value = reader.getColumnValueShort(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setShort(idxLoad, value);
      } break;
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER: {
        final int value = reader.getColumnValueInt(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setInt(idxLoad, value);
      } break;
      case Types.BIGINT: {
        final long value = reader.getColumnValueLong(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setLong(idxLoad, value);
      } break;
      case Types.REAL:
      case Types.FLOAT:
      case Types.DOUBLE: {
        final double value = reader.getColumnValueDouble(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setDouble(idxLoad, value);
      } break;
      case Types.DECIMAL:
      case Types.NUMERIC: {
        final BigDecimal value = reader.getColumnValueBigDecimal(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setBigDecimal(idxLoad, value);
      } break;
      case Types.DATE:
      case Types.TIME:
      case Types.TIMESTAMP: {
        // type long number of milliseconds since January 1, 1970 00:00:00
        final Timestamp value = reader.getColumnValueTimestamp(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setTimestamp(idxLoad, value);
      } break;
      case Types.LONGVARCHAR:
      case Types.VARCHAR: {
        final String value = reader.getColumnValueString(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setString(idxLoad, value);
      } break;
      case Types.CHAR: {
        final String value = reader.getColumnValueString(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setString(
              idxLoad, value.length() == 0 ? " " : value); // oracle: insert " "
                                                           // instead of ""
      } break;
      case Types.LONGVARBINARY:
      case Types.VARBINARY:
      case Types.BINARY: {
        final byte[] value = reader.getColumnValueBytes(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setBytes(idxLoad, value);
      } break;
      case Types.BLOB: {
        final InputStream value = reader.getColumnValueBinaryStream(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setBinaryStream(idxLoad, value, -1);
      } break;
      case Types.CLOB: {
        final Reader value = reader.getColumnValueCharacterStream(idx);
        if (reader.wasColumnValueNull())
          setColumnNull(idxLoad, iType);
        else
          currentRowStmt.setCharacterStream(idxLoad, value, -1);
      } break;
      default:
        throw new UnsupportedOperationException("WebRowSetWriter: type'" +
                                                sType + "' is not supported");
        // todo: implement the following sql types: DATALINK,ARRAY,
        // DISTINCT, NULL, REF, STRUCT, JAVA_OBJECT, OTHER
      }

    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Sets the column null.
   *
   * @param type the type
   * @param idx  the idx
   *
   * @throws SQLException the SQL exception
   */
  private void setColumnNull(int idx, int type) throws SQLException {

    if ((!doImport && currentRowType == rowType.CURRENT) ||
        (!doSync && currentRowType != rowType.CURRENT))
      return; // ignored
    currentRowStmt.setNull(idx, type);
  }

  /**
   * execute the row. (batch)
   *
   * @throws DataIOException the data IO exception
   */
  public void executeRow() throws DataIOException {

    try {

      // increment always
      // used to calculate the current row in doExecuteCurrentBatch
      ++currentBatchSize;

      if (currentRowStmt != null)
        currentRowStmt.addBatch();

      if (currentBatchSize >= maxBatchSize)
        doExecuteCurrentBatch();
      if (currentCommitSize >= maxCommitSize)
        commit();
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Do execute the current batch.
   *
   * @throws SQLException    the SQL exception
   * @throws DataIOException the data IO exception
   */
  protected void doExecuteCurrentBatch() throws SQLException, DataIOException {

    if (currentBatchSize < 1)
      return;

    if ((!doImport && currentRowType == rowType.CURRENT) ||
        (!doSync && currentRowType != rowType.CURRENT)) {
      // ignore
      ignoredRowCount += currentBatchSize;
      currentBatchSize = 0;
      return;
    }

    long currentRow = allRowCount - currentBatchSize + 1;

    if (currentRowStmt == null) {
      // operation not supported

      if (currentBatchSize == 1)
        getProgress().errorln(" row " + currentRow +
                              ": failed: " + currentOperation +
                              " not supported: " + syncErrorCause);
      else
        getProgress().errorln(" row " + currentRow + ": " + currentBatchSize +
                              " row(s) failed: " + currentOperation +
                              " not supported: " + syncErrorCause);

      failedRowCount += currentBatchSize;
      currentBatchSize = 0;
      return;
    }

    // final int actBatchSize = currentBatchSize;
    currentBatchSize = 0;
    RuntimeException rte = null;

    int[] updateCounts = null;
    final ArrayList<SQLException> exceptions = new ArrayList<SQLException>();
    try {
      updateCounts = currentRowStmt.executeBatch();
    } catch (final BatchUpdateException e) {
      updateCounts = e.getUpdateCounts();
      SQLException sqle = e.getNextException();
      while (sqle != null) {
        exceptions.add(sqle);
        sqle = sqle.getNextException();
      }
    } catch (final RuntimeException x) {
      rte = x;
    }
    if (updateCounts != null) {
      final int currentException = 0;
      for (int i = 0; i < updateCounts.length; i++, currentRow++) {
        if (updateCounts[i] < 0 &&
            updateCounts[i] != Statement.SUCCESS_NO_INFO) {
          failedRowCount++;
          final SQLException e = currentException < exceptions.size()
                                     ? exceptions.get(currentException)
                                     : null;
          getProgress().errorln(
              " row " + currentRow + ": failed to " + currentOperation + ": " +
              SQLState.getMessage(e.getSQLState(), e.getErrorCode(),
                                  e.getMessage()));
        } else if (updateCounts[i] > 1) {
          throw new DataIOException(
              "row " + currentRow +
              ": update count is greater than 1: " + updateCounts[i]);
        } else {
          currentCommitSize++;
        }
      }
    }
    if (currentRow <= allRowCount) {
      long currentFailedRowCount = allRowCount - currentRow + 1;
      failedRowCount += currentFailedRowCount;
      if (rte != null)
        getProgress().errorln(
            " row " + currentRow + ": failed to " + currentOperation + " " +
                currentFailedRowCount + " row(s): " + rte.getClass().getName(),
            rte);
      else
        getProgress().errorln(" row " + currentRow + ": failed to " +
                              currentOperation + " " + currentFailedRowCount +
                              " row(s)");
    }

    currentRowStmt.clearBatch();
  }

  /**
   * Gets the connection.
   *
   * @return the connection
   */
  public Connection getConnection() { return con; }

  /**
   * Finalize.
   */
  @Override
  protected void finalize() {

    close();
  }
}
