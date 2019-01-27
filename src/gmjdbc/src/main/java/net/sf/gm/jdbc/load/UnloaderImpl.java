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
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.core.io.DataReaderAbstract;
import net.sf.gm.core.io.MetaData;
import net.sf.gm.core.io.DataTypes.rowType;
import net.sf.gm.jdbc.common.SqlUtil;

/**
 * The Class UnloaderImpl.
 */
public class UnloaderImpl extends DataReaderAbstract implements Unloader {

  /** The con. */
  private Connection con;

  /** The stmt. */
  private Statement stmt;

  /** The rs. */
  private ResultSet rs;

  /** The rsmd. */
  private ResultSetMetaData rsmd;

  /** The processed row count. */
  private long allRowCount;

  /** The full table name. */
  private String tableName;

  /** The schema name. */
  private String schemaName;

  /** The catalog name. */
  private String catalogName;

  /** The statement text. */
  private String statementText;

  /**
   * The Constructor.
   *
   * @param con the con
   *
   * @throws SQLException the SQL exception
   */
  public UnloaderImpl(final Connection con) throws SQLException {

    super();
    rs = null;
    rsmd = null;
    stmt = null;
    setConnection(con);
  }

  /**
   * Gets the connection.
   *
   * @return the connection
   */
  public Connection getConnection() { return con; }

  /**
   * Sets the connection.
   *
   * @param con the con
   *
   * @throws SQLException the SQL exception
   */
  public void setConnection(Connection con) throws SQLException {

    this.con = con;
    if (stmt != null)
      SqlUtil.closeStatement(stmt);
    this.stmt = con.createStatement();
    allRowCount = 0;
  }

  /**
   * Close.
   */
  public void close() {

    rsmd = null;
    if (rs != null)
      SqlUtil.closeResultSet(rs);
    rs = null;
    SqlUtil.closeStatement(stmt);
    stmt = null;
    con = null;
  }

  /**
   * Gets the row read count.
   *
   * @return the row read count
   */
  public long getAllRowCount() { return allRowCount; }

  /**
   * Start un loading.
   *
   * @param tableName     the table name
   * @param schemaName    the schema name
   * @param catalogName   the catalog name
   * @param statementText the statement text
   *
   * @throws SQLException the SQL exception
   */
  public void startUnLoading(String tableName, final String schemaName,
                             final String catalogName, String statementText)
      throws SQLException {

    if (rs != null)
      SqlUtil.closeResultSet(rs);
    rs = null;
    rsmd = null;
    this.tableName = tableName;
    this.schemaName = schemaName;
    this.catalogName = catalogName;
    this.statementText = statementText;
  }

  /**
   * End un loading.
   *
   * @throws SQLException the SQL exception
   */
  public void endUnLoading() throws SQLException {

    if (rs != null)
      SqlUtil.closeResultSet(rs);
    rsmd = null;
    rs = null;
  }

  /**
   * Open data reading.
   *
   * @return the meta data
   *
   * @throws DataIOException the data IO exception
   */
  public MetaData openDataReading() throws DataIOException {

    try {
      rs = stmt.executeQuery(statementText);
      rsmd = rs.getMetaData();
      allRowCount = 0;
      return new UnloadMetaData(con, stmt, rs, rsmd, tableName, schemaName,
                                catalogName, statementText);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Close data reading.
   *
   * @throws DataIOException the data IO exception
   */
  public void closeDataReading() throws DataIOException {

    if (rs != null)
      SqlUtil.closeResultSet(rs);
    rsmd = null;
    rs = null;
  }

  /**
   * Gets the column value big decimal.
   *
   * @param idx the idx
   *
   * @return the column value big decimal
   *
   * @throws DataIOException the data IO exception
   */
  public BigDecimal getColumnValueBigDecimal(int idx) throws DataIOException {

    try {
      return rs.getBigDecimal(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value binary stream.
   *
   * @param idx the idx
   *
   * @return the column value binary stream
   *
   * @throws DataIOException the data IO exception
   */
  public InputStream getColumnValueBinaryStream(int idx)
      throws DataIOException {

    try {
      return rs.getBinaryStream(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value boolean.
   *
   * @param idx the idx
   *
   * @return the column value boolean
   *
   * @throws DataIOException the data IO exception
   */
  public boolean getColumnValueBoolean(int idx) throws DataIOException {

    try {
      return rs.getBoolean(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value clob.
   *
   * @param idx the idx
   *
   * @return the column value clob
   *
   * @throws DataIOException the data IO exception
   */
  public Clob getColumnValueClob(int idx) throws DataIOException {

    try {
      return rs.getClob(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value date.
   *
   * @param idx the idx
   *
   * @return the column value date
   *
   * @throws DataIOException the data IO exception
   */
  public Date getColumnValueDate(int idx) throws DataIOException {

    try {
      return rs.getDate(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value double.
   *
   * @param idx the idx
   *
   * @return the column value double
   *
   * @throws DataIOException the data IO exception
   */
  public double getColumnValueDouble(int idx) throws DataIOException {

    try {
      return rs.getDouble(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value int.
   *
   * @param idx the idx
   *
   * @return the column value int
   *
   * @throws DataIOException the data IO exception
   */
  public int getColumnValueInt(int idx) throws DataIOException {

    try {
      return rs.getInt(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value long.
   *
   * @param idx the idx
   *
   * @return the column value long
   *
   * @throws DataIOException the data IO exception
   */
  public long getColumnValueLong(int idx) throws DataIOException {

    try {
      return rs.getLong(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value short.
   *
   * @param idx the idx
   *
   * @return the column value short
   *
   * @throws DataIOException the data IO exception
   */
  public short getColumnValueShort(int idx) throws DataIOException {

    try {
      return rs.getShort(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value string.
   *
   * @param idx the idx
   *
   * @return the column value string
   *
   * @throws DataIOException the data IO exception
   */
  public String getColumnValueString(int idx) throws DataIOException {

    try {
      return rs.getString(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value time.
   *
   * @param idx the idx
   *
   * @return the column value time
   *
   * @throws DataIOException the data IO exception
   */
  public Time getColumnValueTime(int idx) throws DataIOException {

    try {
      return rs.getTime(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value timestamp.
   *
   * @param idx the idx
   *
   * @return the column value timestamp
   *
   * @throws DataIOException the data IO exception
   */
  public Timestamp getColumnValueTimestamp(int idx) throws DataIOException {

    try {
      return rs.getTimestamp(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value bytes.
   *
   * @param idx the idx
   *
   * @return the column value bytes
   *
   * @throws DataIOException the data IO exception
   */
  public byte[] getColumnValueBytes(int idx) throws DataIOException {

    try {
      return rs.getBytes(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the column value character stream.
   *
   * @param idx the idx
   *
   * @return the column value character stream
   *
   * @throws DataIOException the data IO exception
   */
  public Reader getColumnValueCharacterStream(int idx) throws DataIOException {

    try {
      return rs.getCharacterStream(idx);
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Was column value null.
   *
   * @return true, if was column value null
   *
   * @throws DataIOException the data IO exception
   */
  public boolean wasColumnValueNull() throws DataIOException {

    try {
      return rs.wasNull();
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the current row type.
   *
   * @return the current row type
   *
   * @throws DataIOException the data IO exception
   */
  public rowType getCurrentRowType() throws DataIOException {

    return rowType.CURRENT;
  }

  /**
   * Read next row.
   *
   * @return true, if read next row
   *
   * @throws DataIOException the data IO exception
   */
  public boolean readNextRow() throws DataIOException {

    try {
      boolean res = rs.next();
      if (res)
        allRowCount++;
      return res;
    } catch (SQLException e) {
      throw new DataIOException(e);
    }
  }

  /**
   * Gets the statement text.
   *
   * @return the statement text
   */
  public String getStatementText() { return tableName; }

  /**
   * Gets the table name.
   *
   * @return the table name
   */
  public String getTableName() { return statementText; }

  /**
   * Checks if is update column.
   *
   * @param idx the idx
   *
   * @return true if the current row type is UPDATE and the specified column
   *         should be updated
   *
   * @throws DataIOException the data IO exception
   */
  public boolean isUpdateColumn(int idx) throws DataIOException {

    // row type is always CURRENT
    return false;
  }

  /**
   * Finalize.
   */
  @Override
  protected void finalize() {

    close();
  }
}
