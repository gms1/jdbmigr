/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

import net.sf.gm.core.ui.Progress;

/**
 * The Class DataWriterImpl.
 */
public abstract class DataWriterAbstract implements DataWriter {

  /** The progress. */
  private Progress progress;

  /**
   * The Constructor.
   *
   */
  public DataWriterAbstract() { this.progress = null; }

  /**
   * The Constructor.
   *
   * @param progress the progress
   */
  public DataWriterAbstract(Progress progress) { this.progress = progress; }

  /**
   * Gets the progress.
   *
   * @return the progress
   */
  public Progress getProgress() { return progress; }

  /**
   * Sets the progress.
   *
   * @param progress the progress
   */
  public void setProgress(Progress progress) { this.progress = progress; }

  /**
   * Write all data.
   *
   * @param reader the reader
   *
   * @return the long
   *
   * @throws DataIOException the data IO exception
   */
  public long writeAllData(DataReader reader) throws DataIOException {

    return DataWriterAbstract.copyData(reader, this);
  }

  /**
   * Copy data.
   *
   * @param reader the reader
   * @param writer the writer
   *
   * @return row count
   *
   * @throws DataIOException the data IO exception
   */
  protected static long copyData(DataReader reader, DataWriter writer)
      throws DataIOException {

    MetaData metadata = reader.openDataReading();
    writer.openDataWriting(metadata);
    int colCount = metadata.getColumnCount();
    while (reader.readNextRow()) {
      writer.startRowWriting(reader.getCurrentRowType());
      for (int idx = 1; idx <= colCount; idx++) {
        writer.setColumnValue(idx, reader);
      }
      writer.endRowWriting();
    }
    writer.closeDataWriting();
    if (reader.getAllRowCount() != writer.getAllRowCount())
      throw new DataIOException(
          "copy failed: row(s) read:" + reader.getAllRowCount() +
          " written:" + writer.getAllRowCount());
    return writer.getAllRowCount();
  }
}
