/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.io;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.gm.core.utils.StringUtil;
import net.sf.gm.jdbc.common.SqlUtil;

//
/**
 * The Class TableDef.
 */
public class TableDef {

  /** The catalog. */
  String catalog;

  /** The schema. */
  String schema;

  /** The table. */
  String table;

  /** The table type. */
  String tableType;

  /** The comment. */
  String comment;

  /** The full table name. */
  String fullTableName;

  /** The file name. */
  String fileName;

  /** The file extension. */
  String fileExtension;

  /** The where clause. */
  String whereClause;

  /** The parent tables. */
  TableList parentTables;

  /** The column names. */
  String[] columnNames;

  /** The column types. */
  int[] columnTypes;

  /**
   * The Constructor.
   *
   * @param location      the location
   * @param fileExtension the file extension
   * @param tabCatalog    the tab catalog
   * @param tabSchema     the tab schema
   * @param tabName       the tab name
   * @param tabType       the table type
   * @param comment       the comment
   */
  public TableDef(final File location, final String fileExtension,
                  final String tabCatalog, final String tabSchema,
                  final String tabName, final String tableType, final String comment) {

    this(tabCatalog, tabSchema, tabName, tableType, comment);
    this.fileExtension = fileExtension;
    if (fileExtension == null) {
      if (location == null) 
        fileName = null;
      else
        fileName = location.getPath();
    } else {
      if (location == null)
        fileName = this.table.toLowerCase().concat(fileExtension);
      else if (location.isDirectory())
        fileName = location.getPath() + File.separator +
                 this.table.toLowerCase().concat(fileExtension);
      else
      fileName = location.getPath();
    }
  }

  /**
   * The Constructor.
   *
   * @param fullTableName the full table name
   * @param fileExtension the file extension
   * @param location      the location
   */
  public TableDef(final File location, final String fileExtension,
                  final String fullTableName) {

    this(fullTableName);
    this.fileExtension = fileExtension;
    if (location == null)
      fileName = this.table.toLowerCase().concat(fileExtension);
    else if (location.isDirectory())
      fileName = location.getPath() + File.separator +
                 this.table.toLowerCase().concat(fileExtension);
    else
      fileName = location.getPath();
  }

  /**
   * The Constructor.
   *
   * @param fullTableName the full table name
   */
  public TableDef(final String fullTableName) {

    final String[] names = fullTableName.split("\\.", 3);
    if (names.length < 3) {
      catalog = null;
      if (names.length < 2) {
        table = names[0];
        schema = null;
      } else {
        table = names[1];
        schema = names[0];
      }
    } else {
      catalog = names[0];
      schema = names[1];
      table = names[2];
    }
    this.fullTableName = fullTableName;
  }

  /**
   * The Constructor.
   *
   * @param tabName    the tab name
   * @param tabCatalog the tab catalog
   * @param tabSchema  the tab schema
   * @param tabType    the table type
   * @param comment    the comment
   */
  public TableDef(final String tabCatalog, final String tabSchema,
                  final String tabName, final String tableType, final String comment) {

                    
    this.fileExtension = null;

    this.catalog = tabCatalog == null ? null : StringUtil.rtrim(tabCatalog);
    this.schema = tabSchema == null ? null : StringUtil.rtrim(tabSchema);
    this.table = tabName == null ? null : StringUtil.rtrim(tabName);
    this.tableType = tableType == null ? null : StringUtil.rtrim(tableType);
    this.comment = comment == null ? null : StringUtil.rtrim(comment);

    this.fullTableName =
        TableDef.createFullTableName(tabCatalog, tabSchema, tabName);
  }

  /**
   * Gets the catalog.
   *
   * @return the catalog
   */
  public String getCatalog() { return catalog; }

  /**
   * Gets the file name.
   *
   * @return the file name
   */
  public String getFileName() { return fileName; }

  /**
   * Sets the file name.
   *
   * @param fileName the file name
   */
  public void setFileName(final String fileName) { this.fileName = fileName; }

  /**
   * Gets the full table name.
   *
   * @return the full table name
   */
  public String getFullTableName() { return fullTableName; }

  /**
   * Gets the table.
   *
   * @return the table
   */
  public String getTable() { return table; }

  /**
   * Gets the table type.
   *
   * @return the table type
   */
  public String getTableType() { return tableType; }

  /**
   * Gets the comment.
   *
   * @return the comment
   */
  public String getComment() { return comment; }  

  /**
   * Gets the schema.
   *
   * @return the schema
   */
  public String getSchema() { return schema; }

  /**
   * Gets the where clause.
   *
   * @return the where clause
   */
  public String getWhereClause() { return whereClause; }

  /**
   * Sets the where clause.
   *
   * @param whereClause the where clause
   */
  public void setWhereClause(final String whereClause) {

    this.whereClause = whereClause;
  }

  /**
   * Adds the parent table.
   *
   * @param pkTable the pk table
   */
  public void addParentTable(final TableDef pkTable) {

    if (parentTables == null)
      parentTables = new TableList();
    parentTables.add(pkTable);
  }

  /**
   * Gets the parent tables.
   *
   * @return the parent tables
   */
  public TableList getParentTables() { return parentTables; }

  /**
   * Gets the foreign key tables.
   *
   * @param con the con
   *
   * @return the foreign key tables
   */
  public ArrayList<String[]> getChildTables(final Connection con) {

    return TableDef.getForeignKeyTables(con, catalog, schema, table);
  }

  /**
   * Gets the primary keys.
   *
   * @param con the con
   *
   * @return the foreign key tables
   */
  public String[] getPrimaryKeys(final Connection con) {

    return TableDef.getPrimaryKey(con, catalog, schema, table);
  }

  /**
   * Init column names and column types
   *
   * @param con the con
   *
   */
  protected void initColumns(final Connection con) {
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Integer> types = new ArrayList<Integer>();

    try {
      DatabaseMetaData dsmd = con.getMetaData();
      ResultSet rs = dsmd.getColumns(catalog, schema, table, "%");
      while (rs.next()) {
        names.add(rs.getString(4));
        types.add(rs.getInt(5));
      }
      columnNames = names.toArray(new String[names.size()]);
      columnTypes = new int[types.size()];
      for (int i = 0; i < columnTypes.length; i++)
        columnTypes[i] = types.get(i);
      SqlUtil.closeResultSet(rs);
    } catch (final SQLException ignore) {
    }
  }

  /**
   * Gets the column names.
   *
   * @param con the con
   *
   * @return the column names
   */
  public String[] getColumnNames(final Connection con) {

    if (this.columnNames == null)
      initColumns(con);
    return columnNames;
  }

  /**
   * Gets the column types.
   *
   * @param con the con
   *
   * @return the column types
   */
  public int[] getColumnTypes(final Connection con) {

    if (this.columnTypes == null)
      initColumns(con);
    return columnTypes;
  }

  /**
   * Gets the foreign key tables.
   *
   * @param table   the table
   * @param con     the con
   * @param catalog the catalog
   * @param schema  the schema
   *
   * @return the foreign key tables
   */
  public static ArrayList<String[]> getForeignKeyTables(final Connection con,
                                                        final String catalog,
                                                        final String schema,
                                                        final String table) {

    final ArrayList<String[]> foreignTableSet = new ArrayList<String[]>();
    try {
      final DatabaseMetaData dmd = con.getMetaData();
      final ResultSet rs = dmd.getExportedKeys(catalog, schema, table);
      while (rs.next()) {
        final String[] fktable = new String[3];
        fktable[0] = StringUtil.rtrim(rs.getString(5));
        fktable[1] = StringUtil.rtrim(rs.getString(6));
        fktable[2] = StringUtil.rtrim(rs.getString(7));
        foreignTableSet.add(fktable);
      }
      SqlUtil.closeResultSet(rs);
    } catch (final SQLException ignore) {
    }
    return foreignTableSet;
  }

  /**
   * Gets the primary key.
   *
   * @param table   the table
   * @param con     the con
   * @param catalog the catalog
   * @param schema  the schema
   *
   * @return the primary key columns
   */
  public static String[] getPrimaryKey(final Connection con,
                                       final String catalog,
                                       final String schema,
                                       final String table) {

    ArrayList<String> keys = new ArrayList<String>();
    try {
      DatabaseMetaData dsmd = con.getMetaData();
      ResultSet rs = dsmd.getPrimaryKeys(catalog, schema, table);
      while (rs.next()) {
        keys.add(rs.getString(4));
      }
      SqlUtil.closeResultSet(rs);
    } catch (final SQLException ignore) {
    }
    return keys.toArray(new String[keys.size()]);
  }

  /**
   * Gets the full table name.
   *
   * @param table   the table
   * @param catalog the catalog
   * @param schema  the schema
   *
   * @return the full table name
   */
  public static String createFullTableName(final String catalog,
                                           final String schema,
                                           final String table) {

    final StringBuffer tabFullName = new StringBuffer();
    if (catalog != null) {
      tabFullName.append(catalog);
      tabFullName.append(".");
    }
    if (schema != null) {
      tabFullName.append(schema);
      tabFullName.append(".");
    }
    tabFullName.append(table);
    return tabFullName.toString();
  }
}
