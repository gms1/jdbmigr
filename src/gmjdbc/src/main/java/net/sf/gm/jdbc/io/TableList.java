/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.io;

import net.sf.gm.core.io.DataIOException;
import net.sf.gm.jdbc.common.SqlUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//


/**
 * The Class TableList.
 */
public class TableList extends ArrayList<TableDef> {

    /**
     * The Constant serialVersionUID.
     */
    static final long serialVersionUID = 1;

    /**
     * The Constructor.
     */
    public TableList() {
        super();
    }

    /**
     * Creates the list.
     *
     * @param schemaPattern  the schema pattern
     * @param con            the con
     * @param fileExtension  the file extension
     * @param catalogPattern the catalog pattern
     * @param location       the location
     * @param tablePattern   the table pattern
     * @return the table list
     * @throws DataIOException the sql IO exception
     */
    public static TableList
    createList(final Connection con, final String catalogPattern,
        final String schemaPattern, final String tablePattern,
        final File location, final String fileExtension)
        throws DataIOException {

        try {
            final TableList list = new TableList();

            final DatabaseMetaData dmd = con.getMetaData();

            final String[] types = new String[] {"TABLE", "SYSTEM TABLE", "ALIAS", "SYNONYM"};

            final ResultSet rs =
                dmd.getTables(catalogPattern, schemaPattern, tablePattern, types);
            final ResultSetMetaData rsmd = rs.getMetaData();
            final int colCount = rsmd.getColumnCount();

            boolean res;
            do {
                try {
                    // microsoft jdbc driver (2005-1.1):
                    // a Runtimeexception is encountered when searching for a catalog
                    // pattern, which does not specify a valid catalog (database)
                    res = rs.next();
                } catch (RuntimeException e) {
                    return list;
                }
                if (!res || rs.getString(3) == null) {
                    continue;
                }
                final TableDef table =
                    new TableDef(location, fileExtension, rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4), colCount >= 5 ? rs.getString(5) : null);
                if (table.getTableType() != null && table.getTableType().contains("TABLE")) {
                    list.add(table);
                    continue;
                }
                // for "ALIAS" and "SYNONYM" test if this is a table:
                ResultSet rsCols = dmd.getColumns(table.catalog, table.schema, table.table, null);
                boolean resCol = rsCols.next();
                SqlUtil.closeResultSet(rsCols);
                if (resCol) {
                    list.add(table);
                }
            } while (res);
            SqlUtil.closeResultSet(rs);
            return list;
        } catch (final SQLException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Creates the list.
     *
     * @param tableList     the table list
     * @param con           the con
     * @param fileExtension the file extension
     * @param location      the location
     * @return the table list
     * @throws DataIOException the sql IO exception
     */
    public static TableList createList(final Connection con, final File tableList,
        final File location,
        final String fileExtension)
        throws DataIOException {

        try {
            final TableList list = new TableList();

            final BufferedReader reader =
                new BufferedReader(new FileReader(tableList));
            String s;
            while ((s = reader.readLine()) != null) {
                final String[] row = s.split("[,;\t]", 3);
                final TableDef table =
                    new TableDef(location, fileExtension, row[0].trim());
                list.add(table);
                if (row.length > 1) {
                    row[1] = row[1].trim();
                    if (row[1].length() > 0)
                        table.setFileName(row[1].trim());
                    if (row.length > 2) {
                        row[2] = row[2].trim();
                        if (row[2].length() > 0)
                            table.setWhereClause(row[2]);
                    }
                }
            }
            reader.close();
            return list;
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Sort database sequence. parent tables first and then the children tables
     *
     * @param list the list
     * @param con  the con
     * @return the table list
     */
    public static TableList sortDatabaseSequence(final Connection con,
        final TableList list) {

        // create FullTableName -> TableDef map
        final Map<String, TableDef> map =
            new HashMap<String, TableDef>(list.size());
        for (final TableDef table : list)
            map.put(table.getFullTableName(), table);

        for (final TableDef parentTable : list) {
            final ArrayList<String[]> childTables = parentTable.getChildTables(con);

            for (final String[] childTable : childTables) {
                final String fullChildTableName = TableDef.createFullTableName(
                    childTable[0], childTable[1], childTable[2]);
                if (map.containsKey(fullChildTableName))
                    // childTable is in our map ->
                    // add the parentTable to the childTable's list of parent tables
                    map.get(fullChildTableName).addParentTable(parentTable);
            }
        }

        final TableList resultList = new TableList();

        // add those tables to our resultList, which do not have a parent table
        // remove those tables from the map
        for (final TableDef table : list) {
            final TableList parentTables = table.getParentTables();
            if (parentTables == null || parentTables.size() == 0) {
                map.remove(table.getFullTableName());
                resultList.add(table);
            }
        }

        // add those tables to our resultList, which do not have a parent table in
        // our map, and remove them from the map
        Collection<TableDef> values = map.values();
        int newSize = map.size();
        int oldSize;
        do {
            oldSize = newSize;
            for (final TableDef table : list) {
                if (!map.containsKey(table.getFullTableName()))
                    continue;
                final TableList parentTables = table.getParentTables();
                boolean parentTableFound = false;
                for (final TableDef parentTable : parentTables)
                    if (map.containsKey(parentTable.getFullTableName())) {
                        parentTableFound = true;
                        break;
                    }
                if (!parentTableFound) {
                    map.remove(table.getFullTableName());
                    resultList.add(table);
                }
            }
            values = map.values();
            newSize = map.size();
        } while (oldSize > newSize);

        // add all tables from the map
        if (newSize > 0)
            resultList.addAll(values);

        return resultList;
    }
}
