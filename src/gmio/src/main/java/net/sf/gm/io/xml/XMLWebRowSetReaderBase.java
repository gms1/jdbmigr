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
import net.sf.gm.core.utils.DateTimeUtil;
import net.sf.gm.core.utils.StAXUtil;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

//


/**
 * The Class XMLWebRowSetReaderBase.
 */
public class XMLWebRowSetReaderBase extends DataReaderAbstractStream implements DataReader {

    /**
     * The xmlreader.
     */
    private final XMLStreamReader xmlreader;

    /**
     * The current row type.
     */
    private rowType currentRowType;

    /**
     * The current row's column values.
     */
    private String[] currColumnValues;

    /**
     * The current row's column update indicator.
     */
    private boolean[] currColumnUpdate;

    /**
     * The was null.
     */
    private boolean wasNull;

    /**
     * The meta data.
     */
    private final MetaData metaData;

    /**
     * The Constructor.
     *
     * @param validate  the validate
     * @param is        the is
     * @param progress  the progress
     * @param xmlreader the xmlreader
     */
    public XMLWebRowSetReaderBase(Progress progress, InputStream is, XMLStreamReader xmlreader, boolean validate) {

        super(progress, is);
        this.xmlreader = xmlreader;

        metaData = new MetaDataImpl();
        currColumnValues = null;
        currColumnUpdate = null;
        wasNull = false;
    }

    /**
     * Open data reading.
     *
     * @return the meta data
     * @throws DataIOException the data IO exception
     */
    @Override
    public MetaData openDataReading() throws DataIOException {

        super.openDataReading();
        StAXUtil.downToChild(xmlreader, "webRowSet", "/");
        StAXUtil.downToChild(xmlreader, "properties", "/webRowSet");
        readProperties();
        StAXUtil.upToParent(xmlreader, "properties", "/webRowSet");
        StAXUtil.downToChild(xmlreader, "metadata", "/webRowSet");
        readMetaData();
        StAXUtil.upToParent(xmlreader, "metadata", "/webRowSet");
        StAXUtil.downToChild(xmlreader, "data", "/webRowSet");
        return metaData;
    }

    /**
     * Close data reading.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void closeDataReading() throws DataIOException {

        StAXUtil.upToParent(xmlreader, "data", "/webRowSet");
        StAXUtil.upToParent(xmlreader, "webRowSet", "/");
        super.closeDataReading();
    }

    /**
     * Read properties.
     *
     * @throws DataIOException the data IO exception
     */
    protected void readProperties() throws DataIOException {

        try {
            metaData.setCommand(readTextOnlyChildElement(xmlreader, "command", "/webRowSet/properties"));
            metaData.setConcurrency(
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "concurrency", "/webRowSet/properties")));
            metaData.setDataSource(readTextOnlyChildElement(xmlreader, "datasource", "/webRowSet/properties"));
            metaData.setEscapeProcessing(Boolean
                .parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "escape-processing", "/webRowSet/properties")));
            metaData.setFetchDirection(
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "fetch-direction", "/webRowSet/properties")));
            metaData
                .setFetchSize(Integer.parseInt(readTextOnlyChildElement(xmlreader, "fetch-size", "/webRowSet/properties")));
            metaData.setIsolationLevel(
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "isolation-level", "/webRowSet/properties")));
            StAXUtil.downToChild(xmlreader, "key-columns", "/webRowSet/properties");
            ArrayList<String> list = new ArrayList<String>();
            String elementName = StAXUtil.moveDown(xmlreader);
            while (elementName != null) {
                String column = readWebRowSetElementValue(elementName);
                list.add(column);
                elementName = StAXUtil.moveDown(xmlreader);
            }
            // already consumed:
            // StAXUtil.upToParent( xmlreader, "key-columns", "/webRowSet/properties"
            // );
            metaData.setKeyColumns(list.toArray(new String[0]));

            StAXUtil.downToChild(xmlreader, "map", "/webRowSet/properties");
            // todo read map ?
            StAXUtil.upToParent(xmlreader, "map", "/webRowSet/properties");

            metaData.setMaxFieldSize(
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "max-field-size", "/webRowSet/properties")));
            metaData.setMaxRows(Integer.parseInt(readTextOnlyChildElement(xmlreader, "max-rows", "/webRowSet/properties")));
            metaData.setQueryTimeout(
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "query-timeout", "/webRowSet/properties")));
            metaData
                .setReadOnly(Boolean.parseBoolean(readTextOnlyChildElement(xmlreader, "read-only", "/webRowSet/properties")));
            String s = readTextOnlyChildElement(xmlreader, "rowset-type", "/webRowSet/properties");
            if (s.equalsIgnoreCase("ResultSet.TYPE_FORWARD_ONLY"))
                metaData.setRowSetType(1003);
            else if (s.equalsIgnoreCase("ResultSet.TYPE_SCROLL_INSENSITIVE"))
                metaData.setRowSetType(1004);
            else if (s.equalsIgnoreCase("ResultSet.TYPE_SCROLL_SENSITIVE"))
                metaData.setRowSetType(1005);

            metaData.setShowDeleted(
                Boolean.parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "show-deleted", "/webRowSet/properties")));
            metaData.setTableName(readTextOnlyChildElement(xmlreader, "table-name", "/webRowSet/properties"));
            metaData.setUrl(readTextOnlyChildElement(xmlreader, "url", "/webRowSet/properties"));

            StAXUtil.downToChild(xmlreader, "sync-provider", "/webRowSet/properties");
            metaData.setSyncProviderName(readTextOnlyChildElement(xmlreader, "sync-provider-name", "/webRowSet/properties"));
            metaData
                .setSyncProviderVendor(readTextOnlyChildElement(xmlreader, "sync-provider-vendor", "/webRowSet/properties"));
            metaData.setSyncProviderVersion(
                readTextOnlyChildElement(xmlreader, "sync-provider-version", "/webRowSet/properties"));
            metaData
                .setSyncProviderGrade(readTextOnlyChildElement(xmlreader, "sync-provider-grade", "/webRowSet/properties"));
            metaData.setDataSourceLock(readTextOnlyChildElement(xmlreader, "data-source-lock", "/webRowSet/properties"));
            StAXUtil.upToParent(xmlreader, "sync-provider", "/webRowSet/properties");
        } catch (NumberFormatException | XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Read meta data.
     *
     * @throws DataIOException the data IO exception
     */
    protected void readMetaData() throws DataIOException {

        metaData.setColumnCount(
            Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "column-count", "/webRowSet/metadata")));
        if (metaData.getColumnCount() < 1)
            throw new DataIOException("WebRowSetReader: column count is 0");

        int colCount = metaData.getColumnCount();
        currColumnValues = new String[colCount];
        currColumnUpdate = new boolean[colCount];

        for (int idx = 1; idx <= metaData.getColumnCount(); idx++) {
            StAXUtil.downToChild(xmlreader, "column-definition", "/webRowSet/metadata");

            int colIndex = Integer.parseInt(readTextOnlyChildElement(xmlreader, "column-index", "/webRowSet/metadata"));
            if (colIndex != idx)
                throw new DataIOException("WebRowSetReader: column-definition not found for column " + idx);
            metaData.setColumnAutoIncrement(idx,
                Boolean.parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "auto-increment", "/webRowSet/metadata")));
            metaData.setColumnCaseSensitive(idx,
                Boolean.parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "case-sensitive", "/webRowSet/metadata")));
            metaData.setColumnCurrency(idx,
                Boolean.parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "currency", "/webRowSet/metadata")));
            metaData.setColumnNullable(idx,
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "nullable", "/webRowSet/metadata")));
            metaData.setColumnSigned(idx,
                Boolean.parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "signed", "/webRowSet/metadata")));
            metaData.setColumnSearchable(idx,
                Boolean.parseBoolean(StAXUtil.readTextOnlyChildElement(xmlreader, "searchable", "/webRowSet/metadata")));
            metaData.setColumnDisplaySize(idx,
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "column-display-size", "/webRowSet/metadata")));
            metaData.setColumnLabel(idx, readTextOnlyChildElement(xmlreader, "column-label", "/webRowSet/metadata"));
            metaData.setColumnName(idx, readTextOnlyChildElement(xmlreader, "column-name", "/webRowSet/metadata"));
            metaData.setColumnSchemaName(idx,
                StAXUtil.readTextOnlyChildElement(xmlreader, "schema-name", "/webRowSet/metadata"));
            metaData.setColumnPrecision(idx,
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "column-precision", "/webRowSet/metadata")));
            metaData.setColumnScale(idx,
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "column-scale", "/webRowSet/metadata")));
            metaData.setColumnTableName(idx,
                StAXUtil.readTextOnlyChildElement(xmlreader, "table-name", "/webRowSet/metadata"));
            metaData.setColumnCatalogName(idx,
                StAXUtil.readTextOnlyChildElement(xmlreader, "catalog-name", "/webRowSet/metadata"));
            metaData.setColumnType(idx,
                Integer.parseInt(StAXUtil.readTextOnlyChildElement(xmlreader, "column-type", "/webRowSet/metadata")));
            metaData.setColumnTypeName(idx, readTextOnlyChildElement(xmlreader, "column-type-name", "/webRowSet/metadata"));

            StAXUtil.upToParent(xmlreader, "column-definition", "/webRowSet/metadata");
        }
    }

    /**
     * Gets the column value bytes.
     *
     * @param idx the idx
     * @return the column value bytes
     */
    public byte[] getColumnValueBytes(int idx) {

        if (isColumnValueNull(idx, false))
            return null;
        final char[] cin = currColumnValues[idx - 1].toCharArray();
        return Base64Base.decode(cin, 0, cin.length);
    }

    /**
     * Gets the column value binary stream.
     *
     * @param idx the idx
     * @return the column value binary stream
     */
    public InputStream getColumnValueBinaryStream(int idx) {

        if (isColumnValueNull(idx, false))
            return null;
        final char[] cin = currColumnValues[idx - 1].toCharArray();
        return new ByteArrayInputStream(Base64Base.decode(cin, 0, cin.length));
    }

    /**
     * Gets the column value big decimal.
     *
     * @param idx the idx
     * @return the column value big decimal
     * @throws DataIOException the data IO exception
     */
    public BigDecimal getColumnValueBigDecimal(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return null;
        try {
            return new BigDecimal(currColumnValues[idx - 1]);
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value boolean.
     *
     * @param idx the idx
     * @return the column value boolean
     * @throws DataIOException the data IO exception
     */
    public boolean getColumnValueBoolean(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return false;
        try {
            return Boolean.parseBoolean(currColumnValues[idx - 1]);
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value character stream.
     *
     * @param idx the idx
     * @return the column value character stream
     */
    public Reader getColumnValueCharacterStream(int idx) {

        if (isColumnValueNull(idx, false))
            return null;
        return new StringReader(currColumnValues[idx - 1]);
    }

    /**
     * Gets the column value date.
     *
     * @param idx the idx
     * @return the column value date
     * @throws DataIOException the data IO exception
     */
    public Date getColumnValueDate(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return null;
        String strValue = currColumnValues[idx - 1];
        try {
            return new Date(Long.parseLong(strValue));
        } catch (NumberFormatException ignored) {
        }
        try {
            return DateTimeUtil.isoDateToJavaDate(strValue);
        } catch (ParseException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value double.
     *
     * @param idx the idx
     * @return the column value double
     * @throws DataIOException the data IO exception
     */
    public double getColumnValueDouble(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return 0.0;
        try {
            return Double.parseDouble(currColumnValues[idx - 1]);
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value int.
     *
     * @param idx the idx
     * @return the column value int
     * @throws DataIOException the data IO exception
     */
    public int getColumnValueInt(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return 0;
        try {
            return Integer.parseInt(currColumnValues[idx - 1]);
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value long.
     *
     * @param idx the idx
     * @return the column value long
     * @throws DataIOException the data IO exception
     */
    public long getColumnValueLong(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return 0L;
        try {
            return Long.parseLong(currColumnValues[idx - 1]);
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value short.
     *
     * @param idx the idx
     * @return the column value short
     * @throws DataIOException the data IO exception
     */
    public short getColumnValueShort(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return 0;
        try {
            return Short.parseShort(currColumnValues[idx - 1]);
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value string.
     *
     * @param idx the idx
     * @return the column value string
     */
    public String getColumnValueString(int idx) {

        if (isColumnValueNull(idx, false))
            return null;
        return currColumnValues[idx - 1];
    }

    /**
     * Gets the column value time.
     *
     * @param idx the idx
     * @return the column value time
     * @throws DataIOException the data IO exception
     */
    public Time getColumnValueTime(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return null;
        String strValue = currColumnValues[idx - 1];
        try {
            return new Time(Long.parseLong(strValue));
        } catch (NumberFormatException ignored) {
        }
        try {
            return DateTimeUtil.isoTimeToJavaTime(strValue);
        } catch (ParseException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Gets the column value timestamp.
     *
     * @param idx the idx
     * @return the column value timestamp
     * @throws DataIOException the data IO exception
     */
    public Timestamp getColumnValueTimestamp(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return null;
        String strValue = currColumnValues[idx - 1];
        try {
            // default value is milliseconds since January 1, 1970 00:00:00 GMT.
            return new Timestamp(Long.parseLong(strValue));
        } catch (NumberFormatException ignored) {
        }
        try {
            return new Timestamp(DateTimeUtil.isoDateToCalendar(strValue).getTimeInMillis());
        } catch (ParseException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Read next row.
     *
     * @return true, if read next row
     * @throws DataIOException the data IO exception
     */
    public boolean readNextRow() throws DataIOException {

        try {
            String elementName = StAXUtil.moveDown(xmlreader);
            if (elementName == null)
                return false;
            switch (elementName) {
                case "currentRow":
                    currentRowType = rowType.CURRENT;
                    break;
                case "insertRow":
                    currentRowType = rowType.INSERT;
                    break;
                case "modifyRow":
                    currentRowType = rowType.UPDATE;
                    break;
                case "deleteRow":
                    currentRowType = rowType.DELETE;
                    break;
                default:
                    throw new DataIOException("undefined rowtype: '" + elementName + "'");
            }

            this.incRowReadCount();
            int colCount = metaData.getColumnCount();
            for (int idx = 1; idx <= colCount; idx++) {
                String colElementName = StAXUtil.moveDown(xmlreader);
                if (colElementName == null)
                    throw new DataIOException("parsing row " + this.getAllRowCount() + " failed: column " + idx + " not found");
                if (!colElementName.equals("columnValue") && !colElementName.equals("updateValue"))
                    throw new DataIOException("parsing row " + this.getAllRowCount() + " column " + idx
                        + " failed: found unknown element '" + colElementName + "'");
                if (currentRowType != rowType.UPDATE)
                    currColumnUpdate[idx - 1] = false;
                else
                    currColumnUpdate[idx - 1] = colElementName.equals("updateValue");

                currColumnValues[idx - 1] = readWebRowSetColumnValue(colElementName);
            }
            String endElement = StAXUtil.moveUp(xmlreader);
            if (endElement == null || !endElement.equals(elementName))
                throw new DataIOException("parsing end of row " + this.getAllRowCount() + " failed");
            return true;

        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Read web row set element value.
     *
     * @param endElement the end element
     * @return the string
     * @throws DataIOException the data IO exception
     */
    public String readWebRowSetElementValue(String endElement) throws DataIOException {

        try {
            boolean isNull = false;
            StringBuilder content = new StringBuilder();
            String loc = "" + xmlreader.getLocation();
            int stack = 0;
            while (xmlreader.hasNext()) {
                int event = xmlreader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (!xmlreader.getLocalName().equals("null") || stack != 0)
                            throw new DataIOException("reading '" + endElement + "': unexpected start element '"
                                + xmlreader.getLocalName() + "' at" + xmlreader.getLocation());
                        isNull = true;
                        stack++;
                        break;
                    case XMLStreamConstants.CHARACTERS:
                    case XMLStreamConstants.CDATA:
                    case XMLStreamConstants.SPACE:
                    case XMLStreamConstants.ENTITY_REFERENCE:
                        if (stack == 0)
                            content.append(xmlreader.getText());
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (stack == 0 && xmlreader.getLocalName().equals(endElement))
                            return isNull ? null : content.toString();
                        if (stack == 1 && xmlreader.getLocalName().equals("null")) {
                            stack--;
                            continue;
                        }
                        throw new DataIOException("reading '" + endElement + "': unexpected start element '"
                            + xmlreader.getLocalName() + "' at" + xmlreader.getLocation());
                    default:
                        break;
                }
            }
            throw new DataIOException("reading '" + endElement + "':  starting at" + loc + ": end of element not found");

        } catch (XMLStreamException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * Read web row set column value.
     *
     * @param endElement the end element
     * @return the string
     * @throws DataIOException the data IO exception
     */
    public String readWebRowSetColumnValue(String endElement) throws DataIOException {

        try {
            return readWebRowSetElementValue(endElement);
        } catch (DataIOException e) {
            throw new DataIOException("parsing row " + this.getAllRowCount() + " failed: " + e.getMessage());
        }
    }

    /**
     * Read text only child element.
     *
     * @param parentElement the parent element
     * @param elementName   the element name
     * @param xmlreader     the xmlreader
     * @return the string
     * @throws DataIOException the data IO exception
     */
    public String readTextOnlyChildElement(XMLStreamReader xmlreader, String elementName, String parentElement)
        throws DataIOException {

        StAXUtil.downToChild(xmlreader, elementName, parentElement);
        return readWebRowSetElementValue(elementName);
    }

    /**
     * Was column value null.
     *
     * @return true, if was column value null
     */
    public boolean wasColumnValueNull() {
        return wasNull;
    }

    /**
     * Checks if is column value null.
     *
     * @param idx         the idx
     * @param emptyIsNull true, if the columns empty values should be null values
     * @return true, if is column value null
     */
    protected boolean isColumnValueNull(int idx, boolean emptyIsNull) {

        final int i = idx - 1;
        wasNull = true;
        if (i < 0 || i >= currColumnValues.length)
            return true;

        if (currColumnValues[i] == null)
            return true;

        if (emptyIsNull && currColumnValues[i].length() == 0)
            return true;

        wasNull = false;
        return false;
    }

    /**
     * Gets the current row type.
     *
     * @return the current row type
     */
    public rowType getCurrentRowType() {

        return currentRowType;
    }

    /**
     * Checks if is update column.
     *
     * @param idx the idx
     * @return true if the current row type is UPDATE and the specified column
     * should be updated
     */
    public boolean isUpdateColumn(int idx) {

        return currColumnUpdate[idx - 1];
    }
}
