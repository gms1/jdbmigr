/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.common;

import java.util.Hashtable;

//


/**
 * The Class JdbcTypes.
 */
public class JdbcTypes {

    /**
     * The int to string.
     */
    private final Hashtable<Integer, String> intToString;

    /**
     * The string to int.
     */
    private final Hashtable<String, Integer> stringToInt;

    /**
     * The native string to int.
     */
    private final Hashtable<String, Integer> nativeStringToInt;

    /**
     * The Constructor.
     */
    public JdbcTypes() {

        intToString = new Hashtable<Integer, String>();
        stringToInt = new Hashtable<String, Integer>();

        nativeStringToInt = new Hashtable<String, Integer>();

        intToString.put(java.sql.Types.ARRAY, "ARRAY");
        stringToInt.put("ARRAY", java.sql.Types.ARRAY);

        intToString.put(java.sql.Types.BIGINT, "BIGINT");
        stringToInt.put("BIGINT", java.sql.Types.BIGINT);

        intToString.put(java.sql.Types.BINARY, "BINARY");
        stringToInt.put("BINARY", java.sql.Types.BINARY);

        intToString.put(java.sql.Types.BIT, "BIT");
        stringToInt.put("BIT", java.sql.Types.BIT);

        intToString.put(java.sql.Types.BLOB, "BLOB");
        stringToInt.put("BLOB", java.sql.Types.BLOB);

        intToString.put(java.sql.Types.CHAR, "CHAR");
        stringToInt.put("CHAR", java.sql.Types.CHAR);

        intToString.put(java.sql.Types.CLOB, "CLOB");
        stringToInt.put("CLOB", java.sql.Types.CLOB);

        intToString.put(java.sql.Types.DATE, "DATE");
        stringToInt.put("DATE", java.sql.Types.DATE);

        intToString.put(java.sql.Types.DECIMAL, "DECIMAL");
        stringToInt.put("DECIMAL", java.sql.Types.DECIMAL);

        intToString.put(java.sql.Types.DISTINCT, "DISTINCT");
        stringToInt.put("DISTINCT", java.sql.Types.DISTINCT);

        intToString.put(java.sql.Types.DOUBLE, "DOUBLE");
        stringToInt.put("DOUBLE", java.sql.Types.DOUBLE);

        intToString.put(java.sql.Types.FLOAT, "FLOAT");
        stringToInt.put("FLOAT", java.sql.Types.FLOAT);

        intToString.put(java.sql.Types.INTEGER, "INTEGER");
        stringToInt.put("INTEGER", java.sql.Types.INTEGER);

        intToString.put(java.sql.Types.JAVA_OBJECT, "JAVA_OBJECT");
        stringToInt.put("JAVA_OBJECT", java.sql.Types.JAVA_OBJECT);

        intToString.put(java.sql.Types.LONGVARBINARY, "LONGVARBINARY");
        stringToInt.put("LONGVARBINARY", java.sql.Types.LONGVARBINARY);

        intToString.put(java.sql.Types.LONGVARCHAR, "LONGVARCHAR");
        stringToInt.put("LONGVARCHAR", java.sql.Types.LONGVARCHAR);

        intToString.put(java.sql.Types.NULL, "NULL");
        stringToInt.put("NULL", java.sql.Types.NULL);

        intToString.put(java.sql.Types.NUMERIC, "NUMERIC");
        stringToInt.put("NUMERIC", java.sql.Types.NUMERIC);

        intToString.put(java.sql.Types.OTHER, "OTHER");
        stringToInt.put("OTHER", java.sql.Types.OTHER);

        intToString.put(java.sql.Types.REAL, "REAL");
        stringToInt.put("REAL", java.sql.Types.REAL);

        intToString.put(java.sql.Types.REF, "REF");
        stringToInt.put("REF", java.sql.Types.REF);

        intToString.put(java.sql.Types.SMALLINT, "SMALLINT");
        stringToInt.put("SMALLINT", java.sql.Types.SMALLINT);

        intToString.put(java.sql.Types.STRUCT, "STRUCT");
        stringToInt.put("STRUCT", java.sql.Types.STRUCT);

        intToString.put(java.sql.Types.TIME, "TIME");
        stringToInt.put("TIME", java.sql.Types.TIME);

        intToString.put(java.sql.Types.TIMESTAMP, "TIMESTAMP");
        stringToInt.put("TIMESTAMP", java.sql.Types.TIMESTAMP);

        intToString.put(java.sql.Types.TINYINT, "TINYINT");
        stringToInt.put("TINYINT", java.sql.Types.TINYINT);

        intToString.put(java.sql.Types.VARBINARY, "VARBINARY");
        stringToInt.put("VARBINARY", java.sql.Types.VARBINARY);

        intToString.put(java.sql.Types.VARCHAR, "VARCHAR");
        stringToInt.put("VARCHAR", java.sql.Types.VARCHAR);

        // todo thanks to oracle 8O
        // tested on oracle 10gR2: binary_float and binary_double are mapped to
        // java.sql.Type.NULL
        nativeStringToInt.put("BINARY_FLOAT", java.sql.Types.FLOAT);
        nativeStringToInt.put("BINARY_DOUBLE", java.sql.Types.DOUBLE);
    }

    /**
     * To string.
     *
     * @param type the type
     * @return the string
     */
    public String toString(int type) {
        return intToString.get(type);
    }

    /**
     * To int.
     *
     * @param type the type
     * @return the int
     */
    public int toInt(String type) {
        return stringToInt.get(type);
    }

    /**
     * Native typeto int.
     *
     * @param type the type
     * @return the int
     */
    public int NativeTypetoInt(String type) {

        return nativeStringToInt.getOrDefault(type, java.sql.Types.NULL);
    }
}
