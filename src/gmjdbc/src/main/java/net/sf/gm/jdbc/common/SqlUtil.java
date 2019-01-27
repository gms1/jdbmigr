/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//
/**
 * The Class SqlUtil.
 */
public class SqlUtil {

  /**
   * Close connection.
   *
   * @param con the con
   */
  public static void closeConnection(final Connection con) {

    try {
      con.close();
    } catch (final Exception ignore) {
    }
  }

  /**
   * Close result set.
   *
   * @param rs the rs
   */
  public static void closeResultSet(final ResultSet rs) {

    try {
      rs.close();
    } catch (final Exception ignore) {
    }
  }

  /**
   * Close statement.
   *
   * @param stmt the stmt
   */
  public static void closeStatement(final Statement stmt) {

    try {
      stmt.close();
    } catch (final Exception ignore) {
    }
  }

  /**
   * Close prepared statement.
   *
   * @param stmt the stmt
   */
  public static void closePreparedStatement(final PreparedStatement stmt) {

    try {
      stmt.close();
    } catch (final Exception ignore) {
    }
  }
}
