/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.sql;

import net.sf.gm.core.io.*;
import net.sf.gm.core.io.DataTypes.rowType;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.core.utils.DateTimeUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * The Class NEW_CSVWriter.
 */
public class DMLWriter extends DataWriterAbstractStream implements DataWriter {

    /**
     * The writer.
     */
    private OutputStreamWriter writer;

    /**
     * The commit statement.
     */
    private String commitStatement;

    /**
     * The start of insert statement.
     */
    private String insertStatementStart;

    /**
     * The start of insert statement.
     */
    private String insertStatementEnd;

    /**
     * The start of update statement.
     */
    private String updateStatementStart;

    /**
     * The start of update statement.
     */
    private String updateStatementEnd;

    /**
     * The start of delete statement.
     */
    private String deleteStatementStart;

    /**
     * The start of delete statement.
     */
    private String deleteStatementEnd;

    /**
     * The Constant DEF_READBUFFERSIZE.
     */
    private static final int DEF_READBUFFERSIZE = 1048576; // 1 MB

    /**
     * The eol.
     */
    private static String eol = System.getProperty("line.separator");

    /**
     * The column is primary key.
     */
    private boolean[] columnIsPrimaryKey;

    /**
     * The column values.
     */
    private StringBuilder[] columnValues;

    /**
     * The column count.
     */
    private int columnCount;

    /**
     * The do delete.
     */
    private boolean doDelete;

    /**
     * The doImport.
     */
    private boolean doImport;

    /**
     * The doSync.
     */
    private boolean doSync;

    /**
     * The commitCount.
     */
    private int commitCount;

    /**
     * The Constructor.
     *
     * @param commitCount the commit count
     * @param doImport    the do import
     * @param doDelete    the do delete
     * @param doSync      the doSync
     * @param os          the os
     * @param progress    the progress
     */
    public DMLWriter(Progress progress, OutputStream os, boolean doDelete, boolean doImport, boolean doSync,
        int commitCount) {
        super(progress, os);
        this.writer = null;
        this.doDelete = doDelete;
        this.doImport = doImport;
        this.doSync = doSync;
        this.commitCount = commitCount;
    }

    /**
     * Open data writing.
     *
     * @param metaData the meta data
     * @throws DataIOException the data IO exception
     */
    @Override
    public void openDataWriting(final MetaData metaData) throws DataIOException {

        super.openDataWriting(metaData);

        writer = new OutputStreamWriter(getOutputStream());

        columnCount = metaData.getColumnCount();
        columnValues = new StringBuilder[columnCount];
        columnIsPrimaryKey = new boolean[columnCount];

        String[] keys = metaData.getKeyColumns();
        for (int idx = 1; idx <= columnCount; idx++) {
            columnValues[idx - 1] = new StringBuilder();

            String name = metaData.getColumnName(idx);
            columnIsPrimaryKey[idx - 1] = false;
            for (String key : keys) {
                if (name.equals(key))
                    columnIsPrimaryKey[idx - 1] = true;
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("commit");
        sb.append(eol);
        sb.append(";");
        sb.append(eol);
        sb.append(eol);

        commitStatement = sb.toString();

        // build delete statement
        if (doDelete) {
            sb.setLength(0);
            sb.append("delete from ");
            sb.append(metaData.getTableName());
            sb.append(eol);
            sb.append(";");
            sb.append(eol);
            sb.append(eol);
            try {
                writer.write(sb.toString());
                writer.write(commitStatement);
            } catch (IOException e) {
                throw new DataIOException(e);
            }
        }

        // build insert statement
        sb.setLength(0);
        sb.append("insert into ");
        sb.append(metaData.getTableName());
        sb.append(eol);
        sb.append("(");
        sb.append(eol);
        for (int i = 1; i <= getColumnCount(); i++) {
            if (i > 1) {
                sb.append(",");
                sb.append(eol);
            }
            sb.append("  ");
            sb.append(metaData.getColumnName(i));
        }
        sb.append(eol);
        sb.append(")");
        sb.append(eol);
        sb.append("values (");
        sb.append(eol);

        insertStatementStart = sb.toString();

        sb.setLength(0);
        sb.append(eol);
        sb.append(");");
        sb.append(eol);
        sb.append(eol);

        insertStatementEnd = sb.toString();

        // build update statement

        sb.setLength(0);
        sb.append("update ");
        sb.append(metaData.getTableName());
        sb.append(eol);
        sb.append("set");
        sb.append(eol);

        updateStatementStart = sb.toString();

        sb.setLength(0);
        sb.append(eol);
        sb.append(";");
        sb.append(eol);
        sb.append(eol);

        updateStatementEnd = sb.toString();

        // build delete statement

        sb.setLength(0);
        sb.append("delete from ");
        sb.append(metaData.getTableName());
        sb.append(eol);

        deleteStatementStart = sb.toString();

        sb.setLength(0);
        sb.append(eol);
        sb.append(";");
        sb.append(eol);
        sb.append(eol);

        deleteStatementEnd = sb.toString();
    }

    /**
     * Close data writing.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void closeDataWriting() throws DataIOException {

        try {
            writer.write(commitStatement);
            writer.close();
            super.closeDataWriting();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Start row writing.
     *
     * @param type the type
     * @throws DataIOException the data IO exception
     */
    @Override
    public void startRowWriting(rowType type) throws DataIOException {

        super.startRowWriting(type);
    }

    /**
     * End row writing.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void endRowWriting() throws DataIOException {

        try {
            MetaData metaData = getMetaData();

            switch (getCurrentRowType()) {
                case CURRENT:
                    if (!doImport)
                        return;
                    writer.write(insertStatementStart);
                    for (int idx = 1; idx <= columnCount; idx++) {
                        if (idx > 1) {
                            writer.write(",");
                            writer.write(eol);
                        }
                        writer.write("  ");
                        writer.write(columnValues[idx - 1].toString());
                    }
                    writer.write(insertStatementEnd);
                    break;
                case INSERT:
                    if (!doSync)
                        return;
                    writer.write(insertStatementStart);
                    for (int idx = 1; idx <= columnCount; idx++) {
                        if (idx > 1) {
                            writer.write(",");
                            writer.write(eol);
                        }
                        writer.write("  ");
                        writer.write(columnValues[idx - 1].toString());
                    }
                    writer.write(insertStatementEnd);
                    break;
                case UPDATE:
                    if (!doSync)
                        return;
                    writer.write(updateStatementStart);
                    for (int idx = 1, idxStmt = 0; idx <= columnCount; idx++) {
                        if (columnIsPrimaryKey[idx - 1])
                            continue;
                        if (++idxStmt > 1) {
                            writer.write(",");
                            writer.write(eol);
                        }
                        writer.write("  ");
                        writer.write(metaData.getColumnName(idx));
                        writer.write(" = ");
                        writer.write(columnValues[idx - 1].toString());
                    }
                    writer.write(eol);
                    writer.write("where");
                    writer.write(eol);
                    for (int idx = 1, idxStmt = 0; idx <= columnCount; idx++) {
                        if (!columnIsPrimaryKey[idx - 1])
                            continue;
                        if (++idxStmt > 1) {
                            writer.write(" and");
                            writer.write(eol);
                        }
                        writer.write("  ");
                        writer.write(metaData.getColumnName(idx));
                        String v = columnValues[idx - 1].toString();
                        if (v.equals("null"))
                            writer.write(" is null ");
                        else {
                            writer.write(" = ");
                            writer.write(v);
                        }
                    }
                    writer.write(updateStatementEnd);
                    break;
                case DELETE:
                    if (!doSync)
                        return;
                    writer.write(deleteStatementStart);
                    writer.write("where");
                    writer.write(eol);
                    for (int idx = 1, idxStmt = 0; idx <= columnCount; idx++) {
                        if (!columnIsPrimaryKey[idx - 1])
                            continue;
                        if (++idxStmt > 1) {
                            writer.write(" and");
                            writer.write(eol);
                        }
                        writer.write("  ");
                        writer.write(metaData.getColumnName(idx));
                        String v = columnValues[idx - 1].toString();
                        if (v.equals("null"))
                            writer.write(" is null ");
                        else {
                            writer.write(" = ");
                            writer.write(v);
                        }
                    }
                    writer.write(deleteStatementEnd);
                    break;
                case UNKNOWN:
                    throw new DataIOException("current row type is unknown");
            }
            if (commitCount > 0 && this.getAllRowCount() % commitCount == 0)
                writer.write(commitStatement);

        } catch (final IOException e) {
            throw new DataIOException(e);
        }
        super.endRowWriting();
    }

    /**
     * Sets the column value from string.
     *
     * @param idx    the idx
     * @param reader the reader
     * @throws DataIOException the data IO exception
     */
    public void setColumnValue(final int idx, DataReader reader) throws DataIOException {

        if ((!doImport && getCurrentRowType() == rowType.CURRENT) || (!doSync && getCurrentRowType() != rowType.CURRENT))
            return;

        try {

            final String sType = getMetaData().getColumnTypeName(idx);
            final int iType = getMetaData().getColumnType(idx);
            switch (iType) {
                case Types.BOOLEAN: { // "true" or "false"
                    final boolean value = reader.getColumnValueBoolean(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else
                        setValue(idx, Boolean.toString(value));
                }
                break;
                case Types.BIT:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER: {
                    final int value = reader.getColumnValueInt(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else
                        setValue(idx, "" + value);
                }
                break;
                case Types.BIGINT: {
                    final long value = reader.getColumnValueLong(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else
                        setValue(idx, "" + value);
                }
                break;
                case Types.REAL:
                case Types.FLOAT:
                case Types.DOUBLE: {
                    final double value = reader.getColumnValueDouble(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else
                        setValue(idx, "" + value);
                }
                break;
                case Types.DECIMAL:
                case Types.NUMERIC: {
                    final BigDecimal value = reader.getColumnValueBigDecimal(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else
                        setValue(idx, value.toString());
                }
                break;
                case Types.TIME: {
                    // iso:
                    final Time value = reader.getColumnValueTime(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else {
                        setValue(idx, "'");
                        addValue(idx, DateTimeUtil.javaTimeToIsoTime(value));
                        addValue(idx, "'");
                    }
                }
                break;
                case Types.DATE: {
                    // iso:
                    final Timestamp value = reader.getColumnValueTimestamp(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else {
                        setValue(idx, "'");
                        addValue(idx, DateTimeUtil.javaDateToIsoDateTime(value));
                        addValue(idx, "'");
                    }
                }
                break;
                case Types.TIMESTAMP: {
                    // iso:
                    final Timestamp value = reader.getColumnValueTimestamp(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else {
                        setValue(idx, "'");
                        addValue(idx, DateTimeUtil.javaDateToIsoTimestamp(value));
                        addValue(idx, "'");
                    }
                }
                break;
                case Types.LONGVARCHAR:
                case Types.VARCHAR:
                case Types.CHAR: {
                    final String value = reader.getColumnValueString(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else {
                        setValue(idx, "'");
                        addValue(idx, value);
                        addValue(idx, "'");
                    }
                }
                break;
                case Types.CLOB: {
                    final Reader stream = reader.getColumnValueCharacterStream(idx);
                    if (reader.wasColumnValueNull())
                        setValueNull(idx);
                    else {
                        setValue(idx, "'");

                        final char[] chars = new char[DEF_READBUFFERSIZE];
                        int readLen;
                        while ((readLen = stream.read(chars)) != -1) {
                            addValue(idx, chars, 0, readLen);
                        }

                        addValue(idx, "'");
                    }
                }
                break;
                default:
                    throw new UnsupportedOperationException("CSVWriter: type '" + sType + "' is not supported");
                    // notes: not supported types: BINARY, VARBINARY, LONGVARBINARY,
                    // BLOB, DATALINK,ARRAY, DISTINCT, NULL, REF, STRUCT, JAVA_OBJECT,
                    // OTHER, DATALINK
            }
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * set the column value to null.
     *
     * @param idx the idx
     * @throws DataIOException the data IO exception
     */
    public void setValueNull(final int idx) throws DataIOException {

        setValue(idx, "null");
    }

    /**
     * set the column value.
     *
     * @param value the value
     * @param idx   the idx
     * @throws DataIOException the data IO exception
     */
    protected void setValue(final int idx, final String value) throws DataIOException {

        columnValues[idx - 1].setLength(0);
        addValue(idx, value);
    }

    /**
     * add to the column value.
     *
     * @param value the value
     * @param idx   the idx
     * @throws DataIOException the data IO exception
     */
    protected void addValue(final int idx, final String value) throws DataIOException {

        columnValues[idx - 1].append(value);
    }

    /**
     * add to the column value.
     *
     * @param len    the len
     * @param chars  the chars
     * @param idx    the idx
     * @param offset the offset
     * @throws DataIOException the data IO exception
     */
    protected void addValue(final int idx, char[] chars, int offset, int len) throws DataIOException {

        columnValues[idx - 1].append(chars, offset, len);
    }
}
