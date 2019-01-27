/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.io.File;
import java.util.Locale;

//
/**
 * The Class OptionalArgumentFile.
 */
public class OptionalArgumentFile extends OptionBase {

  /** The must exist. */
  private boolean mustExist = false;

  /** The parent must exist. */
  private boolean parentMustExist = false;

  /** The fileMustExist. */
  private boolean fileMustExist = false;

  /** The directoryMustExist. */
  private boolean directoryMustExist = false;

  /**
   * constructor.
   *
   * @param shortNames  the short names
   * @param description the description
   * @param longNames   the long names
   */
  public OptionalArgumentFile(final String shortNames, final String longNames,
                              final String description) {

    super(shortNames, longNames, description, true);
  }

  /**
   * constructor.
   *
   * @param shortNames  the short names
   * @param parser      the parser
   * @param description the description
   * @param longNames   the long names
   */
  public OptionalArgumentFile(final CmdLineParser parser,
                              final String shortNames, final String longNames,
                              final String description) {

    super(parser, shortNames, longNames, description, true);
  }

  /**
   * set the argument for this type of option.
   *
   * @param locale the locale
   * @param arg    the arg
   *
   * @throws CmdLineException the cmd line exception
   */
  @Override
  protected void setArgument(final String arg, final Locale locale)
      throws CmdLineException {

    if (arg == null)
      throw new CmdLineException.MissingOptionArgumentException(this);
    final File f = new File(arg);
    if (mustExist && !f.exists())
      throw new CmdLineException.InvalidFileArgument(this, arg, "not found");
    if (fileMustExist && !f.isFile())
      throw new CmdLineException.InvalidFileArgument(this, arg,
                                                     "is not a file");
    if (directoryMustExist && !f.isDirectory())
      throw new CmdLineException.InvalidFileArgument(this, arg,
                                                     "not a directory");
    if (parentMustExist && !mustExist) {
      final File parent = f.getParentFile();
      if (parent != null && !parent.exists())
        throw new CmdLineException.InvalidFileArgument(
            this, arg, "parent directory not found");
    }
    setArgument(f);
  }

  /**
   * get the option value.
   *
   * @return the value
   */
  public File getValue() { return getValue(null); }

  /**
   * get the option value.
   *
   * @param defValue the def value
   *
   * @return the value
   */
  public File getValue(final File defValue) {

    return getArgument() != null ? (File)getArgument() : defValue;
  }

  /**
   * Checks if the file must exist.
   *
   * @return true, if the file must exist
   */
  public boolean mustFileExist() { return fileMustExist; }

  /**
   * Sets if file must exist.
   *
   * @param file true, if file must exist
   */
  public void setFileMustExist(final boolean file) {

    this.fileMustExist = file;
    setMustExist(file);
  }

  /**
   * Checks if the directory must exist.
   *
   * @return true, if this directory must exist
   */
  public boolean mustDirectoryExist() { return directoryMustExist; }

  /**
   * Sets if directory must exist.
   *
   * @param dir true, if this directory must exist
   */
  public void setDirectoryMustExist(final boolean dir) {

    this.directoryMustExist = dir;
    setMustExist(dir);
  }

  /**
   * Must exist.
   *
   * @return true, if file/directory must exist
   */
  public boolean mustExist() { return mustExist; }

  /**
   * Sets if the file/directory must exist.
   *
   * @param mustexist true, if the file/directory must exist
   */
  public void setMustExist(final boolean mustexist) {

    if (!mustExist) {
      this.directoryMustExist = false;
      this.fileMustExist = false;
    }

    this.mustExist = mustexist;
  }

  /**
   * Gets the parent must exist.
   *
   * @return the parent must exist
   */
  public boolean getParentMustExist() { return parentMustExist; }

  /**
   * Sets the parent must exist.
   *
   * @param parentMustExist true, if the parent must exist
   */
  public void setParentMustExist(final boolean parentMustExist) {

    this.parentMustExist = parentMustExist;
  }
}
