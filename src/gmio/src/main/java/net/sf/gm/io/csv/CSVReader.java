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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class CSVReader.
 */
public class CSVReader extends DataReaderAbstractStream implements DataReader {

    /**
     * The options.
     */
    private final CSVFormatOptions options;

    /**
     * The parser.
     */
    private final CSVParser parser;

    /**
     * The curr row.
     */
    private CSVCell[] currColumnValues;

    /**
     * The next row.
     */
    private CSVCell[] nextRow;

    /**
     * The was null.
     */
    private boolean wasNull;

    /**
     * The column count.
     */
    private int columnCount;

    /**
     * The Constructor.
     *
     * @param is       the is
     * @param progress the progress
     */
    public CSVReader(Progress progress, InputStream is) {

        this(progress, is, new CSVFormatOptions());
    }

    /**
     * The Constructor.
     *
     * @param is       the is
     * @param progress the progress
     * @param options  the options
     */
    public CSVReader(Progress progress, InputStream is, CSVFormatOptions options) {

        super(progress, is);
        this.options = options;
        this.parser = new CSVParser(getInputStream(), options);
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
        nextRow = null;

        MetaData metaData = new MetaDataImpl();
        // get column count
        if (options.isColumnHeader()) {
            CSVCell[] columnHeader = parser.parse();
            if (columnHeader == null)
                throw new DataIOException("CSVReader: column header not found");
            columnCount = columnHeader.length;
            if (columnCount <= 0)
                throw new DataIOException("CSVReader: no column found");

            metaData.setColumnCount(columnCount);
            for (int idx = 1; idx <= columnCount; idx++)
                metaData.setColumnName(idx, columnHeader[idx - 1].GetValue());
        } else {
            nextRow = parser.parse();
            if (nextRow == null)
                throw new DataIOException("CSVReader: no row found");
            columnCount = nextRow.length;
            if (columnCount <= 0)
                throw new DataIOException("CSVReader: no column found");

            metaData.setColumnCount(columnCount);
        }
        return metaData;
    }

    /**
     * Close data reading.
     *
     * @throws DataIOException the data IO exception
     */
    @Override
    public void closeDataReading() throws DataIOException {

        this.parser.close();
        super.closeDataReading();
    }

    /**
     * Gets the current row type.
     *
     * @return the current row type
     */
    public rowType getCurrentRowType() {

        return rowType.CURRENT;
    }

    /**
     * Read next row.
     *
     * @return true, if read next row
     * @throws DataIOException the data IO exception
     */
    public boolean readNextRow() throws DataIOException {

        if (nextRow != null) {
            currColumnValues = nextRow;
            nextRow = null;
        } else {
            currColumnValues = parser.parse();
        }
        boolean res = currColumnValues != null;
        if (res) {
            this.incRowReadCount();
            if (currColumnValues.length != columnCount)
                throw new DataIOException("CSVReader: row: " + this.getAllRowCount() + ": wrong column count: "
                    + currColumnValues.length + " (expected: " + columnCount + ")");
        }
        return res;
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
            return new BigDecimal(currColumnValues[idx - 1].GetValue());
        } catch (NumberFormatException e) {
            throw new DataIOException(e);
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

        final char[] cin = currColumnValues[idx - 1].GetValue().toCharArray();
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

        final char[] cin = currColumnValues[idx - 1].GetValue().toCharArray();
        return new ByteArrayInputStream(Base64Base.decode(cin, 0, cin.length));
    }

    /**
     * Gets the column value character stream.
     *
     * @param idx the idx
     * @return the column value character stream
     */
    public Reader getColumnValueCharacterStream(int idx) {

        if (isColumnValueNull(idx, options.isQuoting()))
            return null;
        return new StringReader(currColumnValues[idx - 1].GetValue());
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
        String s = currColumnValues[idx - 1].GetValue();
        if (s.equals("0"))
            return false;
        else if (s.equals("1"))
            return true;
        else if (s.equalsIgnoreCase("false"))
            return false;
        else if (s.equalsIgnoreCase("true"))
            return false;
        throw new DataIOException("not a boolean: '" + s + "'");
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
            return Double.parseDouble(currColumnValues[idx - 1].GetValue());
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
            return Integer.parseInt(currColumnValues[idx - 1].GetValue());
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
            return Long.parseLong(currColumnValues[idx - 1].GetValue());
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
            return Short.parseShort(currColumnValues[idx - 1].GetValue());
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

        if (isColumnValueNull(idx, options.isQuoting()))
            return null;
        return currColumnValues[idx - 1].GetValue();
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
        String strValue = currColumnValues[idx - 1].GetValue();
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
     * Gets the column value time.
     *
     * @param idx the idx
     * @return the column value time
     * @throws DataIOException the data IO exception
     */
    public Time getColumnValueTime(int idx) throws DataIOException {

        if (isColumnValueNull(idx, true))
            return null;
        String strValue = currColumnValues[idx - 1].GetValue();
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
        String strValue = currColumnValues[idx - 1].GetValue();
        try {
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
     * @param emptyIsNull true, if the columns empty values should be null values
     * @param idx         the idx
     * @return true, if is column value null
     */
    protected boolean isColumnValueNull(int idx, boolean emptyIsNull) {

        final int i = idx - 1;
        wasNull = true;
        if (i < 0 || i >= currColumnValues.length)
            return true;

        if (currColumnValues[i] == null)
            return true;

        if (currColumnValues[i].GetInputLength() == 0 && emptyIsNull)
            return true;

        wasNull = false;
        return false;
    }

    /**
     * Checks if is update column.
     *
     * @param idx the idx
     * @return true if the current row type is UPDATE and the specified column
     * should be updated
     */
    public boolean isUpdateColumn(int idx) {

        // row type is always CURRENT
        return false;
    }

    /**
     * The Class CSVCell.
     */
    protected static class CSVCell {

        /**
         * The value.
         */
        private final String value;

        /**
         * The inputlen.
         */
        private final int inputlen;

        /**
         * The Constructor.
         *
         * @param inputlen the inputlen
         * @param value    the value
         */
        public CSVCell(String value, int inputlen) {
            this.value = value;
            this.inputlen = inputlen;
        }

        /**
         * Gets the value.
         *
         * @return the string
         */
        public String GetValue() {
            return value;
        }

        /**
         * Gets the input length.
         *
         * @return the int
         */
        public int GetInputLength() {
            return inputlen;
        }
    }


    /**
     * The Class CSVParser.
     */
    protected static class CSVParser {

        /**
         * The read buffer.
         */
        final char[] readBuffer;
        /**
         * The reader.
         */
        private final InputStreamReader reader;
        /**
         * The options.
         */
        private final CSVFormatOptions options;
        /**
         * The current token.
         */
        private StringBuffer currentToken;
        /**
         * The current token input length.
         */
        private int currentTokenInputLength;
        /**
         * The read len.
         */
        private int readLen;

        /**
         * The cur pos.
         */
        private int curPos;

        /**
         * The in quote.
         */
        private boolean inQuotes;

        /**
         * The quote count.
         */
        private int quoteCount;

        /**
         * The Constructor.
         *
         * @param is      the is
         * @param options the options
         */
        public CSVParser(InputStream is, CSVFormatOptions options) {

            this.reader = new InputStreamReader(is);
            this.options = options;
            inQuotes = false;
            currentToken = new StringBuffer();
            currentTokenInputLength = 0;
            readBuffer = new char[options.getReadBufferSize()];
            readLen = 0;
            curPos = 0;
        }

        /**
         * Close.
         *
         * @throws DataIOException the data IO exception
         */
        public void close() throws DataIOException {

            try {
                reader.close();
            } catch (IOException e) {
                throw new DataIOException(e);
            }
        }

        /**
         * Parse.
         *
         * @return the CSVCell[]
         * @throws DataIOException the data IO exception
         */
        public CSVCell[] parse() throws DataIOException {

            try {
                // allocate new row
                /**
                 * The current row.
                 */
                List<CSVCell> currentRow = new ArrayList<CSVCell>();
                while (true) {
                    if (curPos == readLen) {
                        readLen = reader.read(readBuffer);
                        curPos = 0;
                        if (readLen <= 0)
                            return null;
                    }

                    // quote handling:
                    // if doublequote is false
                    // ,"a"b", => quoted string => a"b
                    // ,a"b", => not a quoted string => a"b"
                    // if doublequote is true
                    // ,"a""b", => quoted string => a"b
                    // ,"a"b", => quoted string => ab (ERROR ?)
                    // ,a"b", => not a quoted string => a"b"

                    for (; curPos < readLen; curPos++) {
                        final char c = readBuffer[curPos];
                        if (inQuotes)
                            if ((c == options.getColDelimiter() || c == options.getRowDelimiter())
                                && ((!options.isDoubleQuotes() && quoteCount > 0)
                                || (options.isDoubleQuotes() && quoteCount % 2 == 1))) {
                                quoteCount -= 1; // the last quote terminates the quoting
                                appendQuotes();
                                inQuotes = !inQuotes;
                            } else {
                                currentTokenInputLength++;

                                if (c == options.getCharDelimiter()) {
                                    quoteCount++;
                                    continue;
                                }
                                if (quoteCount > 0)
                                    appendQuotes();

                                currentToken.append(c);
                                continue;
                            }
                        if (c == options.getColDelimiter() || c == options.getRowDelimiter()) {
                            // add currentToken to the list
                            currentRow.add(new CSVCell(currentToken.toString(), currentTokenInputLength));
                            currentToken = new StringBuffer();
                            currentTokenInputLength = 0;
                            if (c == options.getRowDelimiter()) {

                                if (!options.isFilterEmptyLines() || currentRow.size() != 1
                                    || currentRow.get(0).GetInputLength() != 0) {
                                    final CSVCell[] rowInfo = currentRow.toArray(new CSVCell[0]);
                                    currentRow = null;
                                    curPos++;
                                    return rowInfo;
                                }
                                currentRow = new ArrayList<CSVCell>();
                                continue;
                            }
                            continue;
                        }
                        if (options.isQuoting() && c == options.getCharDelimiter() && currentToken.length() == 0) {
                            quoteCount = 0;
                            inQuotes = true;
                            currentTokenInputLength++;
                            continue;
                        }
                        if (c == '\r' && curPos + 1 < readLen && readBuffer[curPos + 1] == '\n') // ignore '\r'
                            continue;
                        currentToken.append(c);
                        currentTokenInputLength++;
                    }
                }

            } catch (final IOException e) {
                throw new DataIOException(e);
            }
        }

        /**
         * Append quotes.
         */
        private void appendQuotes() {

            if (options.isDoubleQuotes())
                for (int j = 0; j < quoteCount / 2; j++)
                    currentToken.append(options.getCharDelimiter());
            else
                for (int j = 0; j < quoteCount - 1; j++)
                    currentToken.append(options.getCharDelimiter());
            quoteCount = 0;
        }
    }
}
