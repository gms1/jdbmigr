/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.io.csv;

//
/**
 * The Class CSVFormatOptions.
 */
public class CSVFormatOptions {

  /** The row delimiter. */
  private char rowDelimiter;

  /** The col delimiter. */
  private char colDelimiter;

  /** The char delimiter. */
  private char charDelimiter;

  /** The quoting. */
  private boolean quoting;

  /** The double quotes. */
  private boolean doubleQuotes;

  /** The column header. */
  private boolean columnHeader;

  /** The filter empty lines. */
  private boolean filterEmptyLines;

  /**
   * The Constructor.
   */
  public CSVFormatOptions() {

    this.rowDelimiter = CSVFormatOptions.DEF_ROWDELIMITER;
    this.colDelimiter = CSVFormatOptions.DEF_COLDELIMITER;
    this.charDelimiter = CSVFormatOptions.DEF_CHARDELIMITER;
    this.quoting = CSVFormatOptions.DEF_QUOTING;
    this.doubleQuotes = CSVFormatOptions.DEF_DOUBLEQUOTES;
    this.columnHeader = CSVFormatOptions.DEF_COLUMNHEADER;
    this.filterEmptyLines = CSVFormatOptions.DEF_FILTEREMPTYLINES;
  }

  /**
   * Gets the char delimiter.
   *
   * @return the char delimiter
   */
  public char getCharDelimiter() {
    return charDelimiter;
  }

  /**
   * Sets the char delimiter.
   *
   * @param charDelimiter the char delimiter
   */
  public void setCharDelimiter(final char charDelimiter) {

    this.charDelimiter = charDelimiter;
  }

  /**
   * Gets the col delimiter.
   *
   * @return the col delimiter
   */
  public char getColDelimiter() {
    return colDelimiter;
  }

  /**
   * Sets the col delimiter.
   *
   * @param colDelimiter the col delimiter
   */
  public void setColDelimiter(final char colDelimiter) {

    this.colDelimiter = colDelimiter;
  }

  /**
   * Checks if is column header.
   *
   * @return true, if is column header
   */
  public boolean isColumnHeader() {
    return columnHeader;
  }

  /**
   * Sets the column header.
   *
   * @param columnHeader the column header
   */
  public void setColumnHeader(final boolean columnHeader) {

    this.columnHeader = columnHeader;
  }

  /**
   * Checks if is double quotes.
   *
   * @return true, if is double quotes
   */
  public boolean isDoubleQuotes() {
    return doubleQuotes;
  }

  /**
   * Sets the double quotes.
   *
   * @param doubleQuotes the double quotes
   */
  public void setDoubleQuotes(final boolean doubleQuotes) {

    this.doubleQuotes = doubleQuotes;
  }

  /**
   * Checks if is quoting.
   *
   * @return true, if is quoting
   */
  public boolean isQuoting() {
    return quoting;
  }

  /**
   * Sets the quoting.
   *
   * @param quoting the quoting
   */
  public void setQuoting(final boolean quoting) {
    this.quoting = quoting;
  }

  /**
   * Checks if is filter empty lines.
   *
   * @return true, if is filter empty lines
   */
  public boolean isFilterEmptyLines() {
    return filterEmptyLines;
  }

  /**
   * Sets the filter empty lines.
   *
   * @param filterEmptyLines the filter empty lines
   */
  public void setFilterEmptyLines(final boolean filterEmptyLines) {

    this.filterEmptyLines = filterEmptyLines;
  }

  /**
   * Gets the row delimiter.
   *
   * @return the row delimiter
   */
  public char getRowDelimiter() {
    return rowDelimiter;
  }

  /**
   * Sets the row delimiter.
   *
   * @param rowDelimiter the row delimiter
   */
  public void setRowDelimiter(final char rowDelimiter) {

    this.rowDelimiter = rowDelimiter;
  }

  /**
   * Gets the read buffer size.
   *
   * @return the read buffer size
   */
  public int getReadBufferSize() {
    return CSVFormatOptions.DEF_READBUFFERSIZE;
  }

  /**
   * Gets the write buffer size.
   *
   * @return the write buffer size
   */
  public int getWriteBufferSize() {

    return CSVFormatOptions.DEF_WRITEBUFFERSIZE;
  }

  /** The Constant DEF_ROWDELIMITER. */
  public static final char DEF_ROWDELIMITER = '\n';

  /** The Constant DEF_COLDELIMITER. */
  public static final char DEF_COLDELIMITER = ',';

  /** The Constant DEF_CHARDELIMITER. */
  public static final char DEF_CHARDELIMITER = '"';

  /** The Constant DEF_QUOTING. */
  public static final boolean DEF_QUOTING = true;

  /** The Constant DEF_DOUBLEQUOTES. */
  public static final boolean DEF_DOUBLEQUOTES = true;

  /** The Constant DEF_COLUMNHEADER. */
  public static final boolean DEF_COLUMNHEADER = true;

  /** The Constant DEF_FILTEREMPTYLINES. */
  public static final boolean DEF_FILTEREMPTYLINES = true;

  /** The Constant DEF_READBUFFERSIZE. */
  public static final int DEF_READBUFFERSIZE = 1048576; // 1 MB

  /** The Constant DEF_WRITEBUFFERSIZE. */
  public static final int DEF_WRITEBUFFERSIZE = 1048576; // 1 MB
}
