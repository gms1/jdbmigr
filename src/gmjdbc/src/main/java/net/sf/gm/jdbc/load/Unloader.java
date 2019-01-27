/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.load;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.gm.core.io.DataReader;

//
/**
 * The Interface Unloader.
 */
public interface Unloader extends DataReader {

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
   *
   * @throws SQLException the SQL exception
   */
  void setConnection(Connection con) throws SQLException;

  /**
   * Start loading.
   *
   * @param tableName     the table name
   * @param schemaName    the schema name
   * @param catalogName   the catalog name
   * @param statementText the statement text
   *
   * @throws SQLException the SQL exception
   */
  void startUnLoading(String tableName, final String schemaName,
                      final String catalogName, String statementText)
      throws SQLException;

  /**
   * End loading.
   *
   * @throws SQLException the SQL exception
   */
  void endUnLoading() throws SQLException;

  /**
   * Close.
   */
  void close();

  /**
   * Gets the table name.
   *
   * @return the table name
   */
  String getTableName();

  /**
   * Gets the statement text.
   *
   * @return the statement text
   */
  String getStatementText();
}
