/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.xml;

import net.sf.gm.core.base64.Base64Base;
import net.sf.gm.core.io.*;
import net.sf.gm.core.io.DataTypes.rowType;
import net.sf.gm.core.ui.Progress;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * The Class XMLWebRowSetWriterBase.
 */
public class XMLWebRowSetWriterBase
    extends DataWriterAbstractStream implements DataWriter {

    /**
     * The xmlwriter.
     */
    private final XMLStreamWriter xmlwriter;

    /**
     * The Constant DEF_READBUFFERSIZE.
     */
    private static final int DEF_READBUFFERSIZE = 1048576; // 1 MB

    /**
     * The Constructor.
     *
     * @param os        the os
     * @param xmlwriter the xmlwriter
     * @param progress  the progress
     */
    public XMLWebRowSetWriterBase(Progress progress, OutputStream os,
        XMLStreamWriter xmlwriter) {

        super(progress, os);
        this.xmlwriter = xmlwriter;
    }

    /**
     * Open data writing.
     *
     * @param metaData the meta data
     * @throws DataIOException the data IO exception
     */
    @Override
    public void openDataWriting(MetaData metaData) throws DataIOException {

        super.openDataWriting(metaData);
        try {
            xmlwriter.writeStartDocument();
            xmlwriter.writeStartElement("webRowSet");
            xmlwriter.writeAttribute("xmlns", "http://java.sun.com/xml/ns/jdbc");
            xmlwriter.writeAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
            xmlwriter.writeAttribute(
                "xsi:schemaLocation",
                XMLWebRowSetSchema.CONFIGURATION_XSD_SCHEMALOCATION);

            writeProperties();
            writeMetaData();
            xmlwriter.writeStartElement("data");
        } catch (XMLStreamException e) {
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
            xmlwriter.writeEndElement(); // data
            xmlwriter.writeEndElement(); // webRowSet
            xmlwriter.writeEndDocument();
            this.xmlwriter.close();
        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
        super.closeDataWriting();
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

        try {
            switch (getCurrentRowType()) {
                case CURRENT:
                    xmlwriter.writeStartElement("currentRow");
                    break;
                case INSERT:
                    xmlwriter.writeStartElement("insertRow");
                    break;
                case UPDATE:
                    xmlwriter.writeStartElement("modifyRow");
                    break;
                case DELETE:
                    xmlwriter.writeStartElement("deleteRow");
                    break;
                default:
                    throw new DataIOException("internal error: unknown row type");
            }

        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * End row writing.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void endRowWriting() throws DataIOException {

        try {
            xmlwriter
                .writeEndElement(); // currentRow, insertRow, modifyRow or deleteRow
        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
        super.endRowWriting();
    }

    /**
     * set colum value.
     *
     * @param idx    the idx
     * @param reader the reader
     * @throws DataIOException the data IO exception
     */
    public void setColumnValue(final int idx, DataReader reader)
        throws DataIOException {

        try {
            xmlwriter.writeStartElement("columnValue");
            MetaData metaData = getMetaData();
            final String sType = metaData.getColumnTypeName(idx);
            final int iType = metaData.getColumnType(idx);
            switch (iType) {
                case Types.BOOLEAN: { // "true" or "false"
                    final boolean value = reader.getColumnValueBoolean(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text(Boolean.toString(value));
                }
                break;
                case Types.BIT: {
                    // "true" or "false"
                    final short value = reader.getColumnValueShort(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text(Boolean.toString(value == 0 ? false : true));
                }
                break;
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER: {
                    final int value = reader.getColumnValueInt(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text("" + value);
                }
                break;
                case Types.BIGINT: {
                    final long value = reader.getColumnValueLong(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text("" + value);
                }
                break;
                case Types.REAL:
                case Types.FLOAT:
                case Types.DOUBLE: {
                    final double value = reader.getColumnValueDouble(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text("" + value);
                }
                break;
                case Types.DECIMAL:
                case Types.NUMERIC: {
                    final BigDecimal value = reader.getColumnValueBigDecimal(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text(value.toString());
                }
                break;
                case Types.TIME: {
                    final Time value = reader.getColumnValueTime(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text("" + value.getTime());
                }
                break;
                case Types.DATE:
                case Types.TIMESTAMP: {
                    // type long number of milliseconds since January 1, 1970 00:00:00
                    final Timestamp value = reader.getColumnValueTimestamp(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text("" + value.getTime());
                }
                break;
                case Types.LONGVARCHAR:
                case Types.VARCHAR:
                case Types.CHAR: {
                    final String value = reader.getColumnValueString(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text(value);
                }
                break;
                case Types.LONGVARBINARY:
                case Types.VARBINARY:
                case Types.BINARY: {
                    final byte[] value = reader.getColumnValueBytes(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else
                        text(Base64Base.encode(value, 0, value.length, 0));
                }
                break;
                case Types.BLOB: {
                    final InputStream value = reader.getColumnValueBinaryStream(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else {
                        final byte[] b = new byte[Base64Base.DECODED_CHUNK_SIZE];
                        int currentLineSize = 0;
                        int readLen;
                        while ((readLen = value.read(b)) != -1) {
                            text(Base64Base.encode(b, 0, readLen, currentLineSize));
                            currentLineSize =
                                (currentLineSize + readLen) % Base64Base.DECODED_CHUNK_SIZE;
                        }
                    }
                }
                break;
                case Types.CLOB: {
                    final Reader stream = reader.getColumnValueCharacterStream(idx);
                    if (reader.wasColumnValueNull())
                        emptyElement("null");
                    else {

                        final char[] chars = new char[DEF_READBUFFERSIZE];
                        int readLen;
                        while ((readLen = stream.read(chars)) != -1) {
                            text(chars, readLen);
                        }
                    }
                }
                break;
                default:
                    throw new UnsupportedOperationException("WebRowSetWriter: type'" + sType + "' is not supported");
                    // todo: implement the following sql types: DATALINK,ARRAY,
                    // DISTINCT, NULL, REF, STRUCT, JAVA_OBJECT, OTHER
            }
            xmlwriter.writeEndElement();
        } catch (final IOException e) {
            throw new DataIOException(e);
        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Element.
     *
     * @param value the value
     * @param tag   the tag
     * @throws DataIOException the sql IO exception
     */
    private void element(final String tag, final String value)
        throws DataIOException {

        try {
            if (value == null)
                emptyElement(tag);
            else {
                xmlwriter.writeStartElement(tag);
                xmlwriter.writeCharacters(value);
                xmlwriter.writeEndElement();
            }
        } catch (final XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Empty element.
     *
     * @param tag the tag
     * @throws DataIOException the sql IO exception
     */
    private void emptyElement(final String tag) throws DataIOException {

        try {
            xmlwriter.writeEmptyElement(tag);
        } catch (final XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Text.
     *
     * @param value the value
     * @throws DataIOException the sql IO exception
     */
    private void text(final String value) throws DataIOException {

        try {
            xmlwriter.writeCharacters(value);
        } catch (final XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Text.
     *
     * @param len   the len
     * @param value the value
     * @throws DataIOException the sql IO exception
     */
    private void text(char[] value, int len) throws DataIOException {

        try {
            xmlwriter.writeCharacters(value, 0, len);
        } catch (final XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Write properties.
     *
     * @throws DataIOException the sql IO exception
     */
    protected void writeProperties() throws DataIOException {

        try {
            MetaData metaData = getMetaData();
            // indent 1
            xmlwriter.writeStartElement("properties");
            // indent 2
            element("command", metaData.getCommand());
            element("concurrency", Integer.toString(metaData.getConcurrency()));
            element("datasource", metaData.getDataSource());
            element("escape-processing",
                Boolean.toString(metaData.isEscapeProcessing()));
            element("fetch-direction",
                Integer.toString(metaData.getFetchDirection()));
            element("fetch-size", Integer.toString(metaData.getFetchSize()));
            element("isolation-level",
                Integer.toString(metaData.getIsolationLevel()));
            xmlwriter.writeStartElement("key-columns");
            String[] keys = metaData.getKeyColumns();
            if (keys != null)
                for (String colName : keys) {
                    element("column", colName);
                }
            xmlwriter.writeEndElement();
            emptyElement("map"); // todo: write map ?
            element("max-field-size", Integer.toString(metaData.getMaxFieldSize()));
            element("max-rows", Integer.toString(metaData.getMaxRows()));
            element("query-timeout", Integer.toString(metaData.getQueryTimeout()));
            element("read-only", Boolean.toString(metaData.isReadOnly()));
            final int itype = metaData.getRowSetType();
            String rowSetType = "";
            if (itype == 1003)
                rowSetType = "ResultSet.TYPE_FORWARD_ONLY";
            else if (itype == 1004)
                rowSetType = "ResultSet.TYPE_SCROLL_INSENSITIVE";
            else if (itype == 1005)
                rowSetType = "ResultSet.TYPE_SCROLL_SENSITIVE";
            element("rowset-type", rowSetType);
            element("show-deleted", Boolean.toString(metaData.isShowDeleted()));
            element("table-name", metaData.getTableName());

            emptyElement("url");
            xmlwriter.writeStartElement("sync-provider");
            element("sync-provider-name", metaData.getSyncProviderName());
            element("sync-provider-vendor", metaData.getSyncProviderVendor());
            element("sync-provider-version", metaData.getSyncProviderVersion());
            element("sync-provider-grade", metaData.getSyncProviderGrade());
            element("data-source-lock", metaData.getDataSourceLock());
            xmlwriter.writeEndElement();

            xmlwriter.writeEndElement();
        } catch (final XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Write meta data.
     *
     * @throws DataIOException the sql IO exception
     */
    protected void writeMetaData() throws DataIOException {

        try {
            MetaData metaData = getMetaData();
            xmlwriter.writeStartElement("metadata");
            final int columnCount = metaData.getColumnCount();

            element("column-count", Integer.toString(columnCount));
            for (int idx = 1; idx <= columnCount; idx++) {
                xmlwriter.writeStartElement("column-definition");
                element("column-index", Integer.toString(idx));
                element("auto-increment",
                    Boolean.toString(metaData.isColumnAutoIncrement(idx)));
                element("case-sensitive",
                    Boolean.toString(metaData.isColumnCaseSensitive(idx)));
                element("currency", Boolean.toString(metaData.isColumnCurrency(idx)));
                element("nullable", Integer.toString(metaData.isColumnNullable(idx)));
                element("signed", Boolean.toString(metaData.isColumnSigned(idx)));
                element("searchable",
                    Boolean.toString(metaData.isColumnSearchable(idx)));
                element("column-display-size",
                    Integer.toString(metaData.getColumnDisplaySize(idx)));
                element("column-label", metaData.getColumnLabel(idx));
                element("column-name", metaData.getColumnName(idx));
                element("schema-name", metaData.getColumnSchemaName(idx));
                element("column-precision",
                    Integer.toString(metaData.getColumnPrecision(idx)));
                element("column-scale", Integer.toString(metaData.getColumnScale(idx)));
                element("table-name", metaData.getColumnTableName(idx));
                element("catalog-name", metaData.getColumnCatalogName(idx));
                element("column-type", Integer.toString(metaData.getColumnType(idx)));
                element("column-type-name", metaData.getColumnTypeName(idx));
                xmlwriter.writeEndElement();
            }

            xmlwriter.writeEndElement();
        } catch (final XMLStreamException e) {
            throw new DataIOException(e);
        }
    }
}
