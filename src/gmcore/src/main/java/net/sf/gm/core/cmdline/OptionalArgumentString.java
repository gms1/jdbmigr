/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.util.Locale;

//
/**
 * The Class OptionalArgumentString.
 */
public class OptionalArgumentString extends OptionBase {

  /**
   * constructor.
   *
   * @param shortNames  the short names
   * @param description the description
   * @param longNames   the long names
   */
  public OptionalArgumentString(final String shortNames, final String longNames,
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
  public OptionalArgumentString(final CmdLineParser parser,
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
    setArgument(arg);
  }

  /**
   * get the option value.
   *
   * @return the value
   */
  public String getValue() { return getValue(null); }

  /**
   * get the option value.
   *
   * @param defValue the def value
   *
   * @return the value
   */
  public String getValue(final String defValue) {

    return getArgument() != null ? getArgument().toString() : defValue;
  }
}
