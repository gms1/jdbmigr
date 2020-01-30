/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.csv;

import net.sf.gm.core.base64.Base64Base;
import net.sf.gm.core.io.*;
import net.sf.gm.core.io.DataTypes.rowType;
import net.sf.gm.core.ui.Progress;
import net.sf.gm.core.utils.DateTimeUtil;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * The Class NEW_CSVWriter.
 */
public class CSVWriter extends DataWriterAbstractStream implements DataWriter {

    /**
     * The writer.
     */
    private OutputStreamWriter writer;

    /**
     * The options.
     */
    private final CSVFormatOptions options;

    /**
     * The Constructor.
     *
     * @param os       the os
     * @param progress the progress
     */
    public CSVWriter(Progress progress, OutputStream os) {

        this(progress, os, new CSVFormatOptions());
    }

    /**
     * The Constructor.
     *
     * @param os       the os
     * @param progress the progress
     * @param options  the options
     */
    public CSVWriter(Progress progress, OutputStream os,
        final CSVFormatOptions options) {

        super(progress, os);
        this.options = options;
        this.writer = null;
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

        try {
            writer = new OutputStreamWriter(getOutputStream());

            if (options.isColumnHeader()) {
                for (int idx = 1; idx <= getColumnCount(); idx++) {
                    if (idx > 1)
                        writer.write(options.getColDelimiter());
                    writeValue(metaData.getColumnName(idx));
                }
                writeEndOfRow();
            }
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Close data writing.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void closeDataWriting() throws DataIOException {

        try {
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
        if (getCurrentRowType() != rowType.CURRENT) {
            getProgress().warningln("row " + this.getAllRowCount() +
                " ignored: row type '" + rowType.CURRENT.name() +
                "' is not supported");
            return;
        }
    }

    /**
     * End row writing.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void endRowWriting() throws DataIOException {

        if (getCurrentRowType() != rowType.CURRENT)
            // do not call super.endRowWriting(), this would increment the
            // processedRowCount
            return;
        writeEndOfRow();
        super.endRowWriting();
    }

    /**
     * Sets the column value from string.
     *
     * @param idx    the idx
     * @param reader the reader
     * @throws DataIOException the data IO exception
     */
    public void setColumnValue(final int idx, DataReader reader)
        throws DataIOException {

        if (getCurrentRowType() != rowType.CURRENT)
            return;

        try {
            if (idx > 1)
                writer.write(options.getColDelimiter());
            MetaData metaData = getMetaData();
            final String sType = metaData.getColumnTypeName(idx);
            final int iType = metaData.getColumnType(idx);
            switch (iType) {
                case Types.BOOLEAN: { // "true" or "false"
                    final boolean value = reader.getColumnValueBoolean(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue(Boolean.toString(value));
                }
                break;
                case Types.BIT:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER: {
                    final int value = reader.getColumnValueInt(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue("" + value);
                }
                break;
                case Types.BIGINT: {
                    final long value = reader.getColumnValueLong(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue("" + value);
                }
                break;
                case Types.REAL:
                case Types.FLOAT:
                case Types.DOUBLE: {
                    final double value = reader.getColumnValueDouble(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue("" + value);
                }
                break;
                case Types.DECIMAL:
                case Types.NUMERIC: {
                    final BigDecimal value = reader.getColumnValueBigDecimal(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue(value.toString());
                }
                break;
                case Types.TIME: {
                    // iso:
                    final Time value = reader.getColumnValueTime(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue(DateTimeUtil.javaTimeToIsoTime(value));
                }
                break;
                case Types.DATE: {
                    // iso:
                    final Timestamp value = reader.getColumnValueTimestamp(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue(DateTimeUtil.javaDateToIsoDateTime(value));
                }
                break;
                case Types.TIMESTAMP: {
                    // iso:
                    final Timestamp value = reader.getColumnValueTimestamp(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else
                        writeValue(DateTimeUtil.javaDateToIsoTimestamp(value));
                }
                break;
                case Types.LONGVARCHAR:
                case Types.VARCHAR:
                case Types.CHAR: {
                    final String value = reader.getColumnValueString(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else {
                        final StringBuilder sb = new StringBuilder();
                        if (options.isQuoting())
                            sb.append(options.getCharDelimiter());
                        if (options.isQuoting() && options.isDoubleQuotes())
                            sb.append(value.replaceAll("" + options.getCharDelimiter(),
                                "" + options.getCharDelimiter() +
                                    options.getCharDelimiter()));
                        else
                            sb.append(value);
                        if (options.isQuoting())
                            sb.append(options.getCharDelimiter());
                        writeValue(sb.toString());
                    }
                }
                break;
                case Types.CLOB: {
                    final Reader stream = reader.getColumnValueCharacterStream(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else {
                        if (options.isQuoting())
                            writeValue("" + options.getCharDelimiter());

                        final char[] chars = new char[options.getReadBufferSize()];
                        int readLen;
                        while ((readLen = stream.read(chars)) != -1) {
                            String strValue = new String(chars, 0, readLen);
                            if (options.isQuoting() && options.isDoubleQuotes())
                                strValue = strValue.replaceAll("" + options.getCharDelimiter(),
                                    "" + options.getCharDelimiter() +
                                        options.getCharDelimiter());
                            writeValue(strValue);
                        }

                        if (options.isQuoting())
                            writeValue("" + options.getCharDelimiter());
                    }
                }
                break;
                case Types.LONGVARBINARY:
                case Types.VARBINARY:
                case Types.BINARY: {
                    final byte[] value = reader.getColumnValueBytes(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else {
                        final StringBuilder sb = new StringBuilder();
                        if (options.isQuoting())
                            sb.append(options.getCharDelimiter());
                        sb.append(Base64Base.encode(value, 0, value.length, 0));
                        if (options.isQuoting())
                            sb.append(options.getCharDelimiter());
                        writeValue(sb.toString());
                    }
                }
                break;
                case Types.BLOB: {
                    final InputStream value = reader.getColumnValueBinaryStream(idx);
                    if (reader.wasColumnValueNull())
                        writeNullValue();
                    else {
                        if (options.isQuoting())
                            writeValue("" + options.getCharDelimiter());

                        final byte[] b = new byte[Base64Base.DECODED_CHUNK_SIZE];
                        int currentLineSize = 0;
                        int readLen;
                        while ((readLen = value.read(b)) != -1) {
                            writeValue(Base64Base.encode(b, 0, readLen, currentLineSize));
                            currentLineSize =
                                (currentLineSize + readLen) % Base64Base.DECODED_CHUNK_SIZE;
                        }

                        if (options.isQuoting())
                            writeValue("" + options.getCharDelimiter());
                    }
                }
                break;
                default:
                    throw new UnsupportedOperationException("CSVWriter: type'" + sType + "' is not supported");
                    // todo: implement the following sql types: DATALINK,ARRAY,
                    // DISTINCT, NULL, REF, STRUCT, JAVA_OBJECT, OTHER
            }
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Sets the column value null.
     *
     * @throws DataIOException the data IO exception
     */
    protected void writeNullValue() throws DataIOException {
    }

    /**
     * writes the value.
     *
     * @param value the value
     * @throws DataIOException the data IO exception
     */
    protected void writeValue(final String value) throws DataIOException {

        try {
            if (value != null)
                writer.write(value);
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Write row.
     *
     * @throws DataIOException the data IO exception
     */
    protected void writeEndOfRow() throws DataIOException {

        try {
            writer.write(options.getRowDelimiter());
        } catch (final IOException e) {
            throw new DataIOException(e);
        }
    }
}
