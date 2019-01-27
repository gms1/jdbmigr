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
 * The Class OptionalCounter.
 */
public class OptionalCounter extends OptionBase {

  /**
   * constructor.
   *
   * @param shortNames  the short names
   * @param description the description
   * @param longNames   the long names
   */
  public OptionalCounter(final String shortNames, final String longNames, final String description) {

    super(shortNames, longNames, description, false);
  }

  /**
   * constructor.
   *
   * @param shortNames  the short names
   * @param parser      the parser
   * @param description the description
   * @param longNames   the long names
   */
  public OptionalCounter(final CmdLineParser parser, final String shortNames, final String longNames,
      final String description) {

    super(parser, shortNames, longNames, description, false);
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
  protected void setArgument(final String arg, final Locale locale) throws CmdLineException {

    if (arg != null)
      throw new CmdLineException.IllegalOptionArgumentException(this, arg);
    final int n = getArgument() != null ? ((Integer) getArgument()).intValue() : 0;
    this.setArgument(n + 1);
  }

  /**
   * get the option value.
   *
   * @return the value
   */
  public int getValue() {

    return getArgument() != null ? ((Integer) getArgument()).intValue() : 0;
  }

  /**
   * get the option value.
   *
   * @param defValue the def value
   *
   * @return the value
   */
  public int getValue(final int defValue) {

    return getArgument() != null ? getValue() : defValue;
  }
}
