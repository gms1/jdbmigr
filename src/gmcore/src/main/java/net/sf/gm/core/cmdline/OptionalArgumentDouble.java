/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

//
/**
 * The Class OptionalArgumentDouble.
 */
public class OptionalArgumentDouble extends OptionBase {

  /**
   * constructor.
   *
   * @param shortNames  the short names
   * @param description the description
   * @param longNames   the long names
   */
  public OptionalArgumentDouble(final String shortNames, final String longNames, final String description) {

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
  public OptionalArgumentDouble(final CmdLineParser parser, final String shortNames, final String longNames,
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
  protected void setArgument(final String arg, final Locale locale) throws CmdLineException {

    if (arg == null)
      throw new CmdLineException.MissingOptionArgumentException(this);
    try {
      final NumberFormat format = NumberFormat.getNumberInstance(locale);
      final Number num = format.parse(arg);
      setArgument(num.doubleValue());
    } catch (final ParseException e) {
      throw new CmdLineException.IllegalOptionArgumentException(this, arg);
    }
  }

  /**
   * get the option value.
   *
   * @return the value
   */
  public double getValue() {
    return getValue(0.0);
  }

  /**
   * get the option value.
   *
   * @param defValue the def value
   *
   * @return the value
   */
  public double getValue(final double defValue) {

    return getArgument() != null ? ((Double) getArgument()).doubleValue() : defValue;
  }
}
