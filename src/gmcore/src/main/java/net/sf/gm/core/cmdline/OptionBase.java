/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.util.Locale;

import net.sf.gm.core.utils.StringUtil;

//
/**
 * abstract base class for all options.
 */
public abstract class OptionBase {

  /** The mandatory argument. */
  private boolean mandatoryArgument;

  /** The short names. */
  private String[] shortNames;

  /** The long names. */
  private String[] longNames;

  /** The description. */
  private String description;

  /** The argument. */
  private Object argument = null;

  /** The display name. */
  private String displayName = null;

  /** The is help option. */
  private boolean isHelpOption = false;

  /** The unescape. */
  private boolean unescape = false;

  /**
   * constructor.
   *
   * @param shortNames        the short names
   * @param description       the description
   * @param longNames         the long names
   * @param mandatoryArgument the mandatory argument
   */
  protected OptionBase(final String shortNames, final String longNames, final String description,
      final boolean mandatoryArgument) {

    init(shortNames, longNames, description, mandatoryArgument);
  }

  /**
   * constructor.
   *
   * @param shortNames        the short names
   * @param parser            the parser
   * @param description       the description
   * @param longNames         the long names
   * @param mandatoryArgument the mandatory argument
   */
  protected OptionBase(final CmdLineParser parser, final String shortNames, final String longNames,
      final String description, final boolean mandatoryArgument) {

    init(shortNames, longNames, description, mandatoryArgument);
    // add this option to the parser
    parser.add(this);
  }

  /**
   * initialize option.
   *
   * @param shortNames        the short names
   * @param description       the description
   * @param longNames         the long names
   * @param mandatoryArgument the mandatory argument
   */
  protected void init(final String shortNames, final String longNames, final String description,
      final boolean mandatoryArgument) {

    this.description = description;
    this.mandatoryArgument = mandatoryArgument;
    this.shortNames = null;
    if (shortNames != null && shortNames.length() > 0) {
      final int shortNameCount = shortNames.length();
      this.shortNames = new String[shortNameCount];
      for (int i = 0; i < shortNameCount; i++)
        this.shortNames[i] = shortNames.substring(i, i + 1);
    }
    this.longNames = null;
    if (longNames != null && longNames.length() > 0)
      this.longNames = longNames.split(",");
  }

  /**
   * Gets the unescape.
   *
   * @return the unescape
   */
  public boolean getUnescape() {
    return unescape;
  }

  /**
   * Sets the unescape.
   *
   * @param unescape the unescape
   */
  public void setUnescape(final boolean unescape) {
    this.unescape = unescape;
  }

  /**
   * if this option is selected, the usage description should be provided.
   */
  public void forHelpUsage() {
    this.isHelpOption = true;
  }

  /**
   * Checks if is for help usage.
   *
   * @return true, if is for help usage
   */
  public boolean isForHelpUsage() {
    return this.isHelpOption;
  }

  /**
   * reset this option.
   */
  protected void reset() {

    displayName = null;
    argument = null;
  }

  /**
   * get all long option names.
   *
   * @return the long names
   */
  public String[] getLongNames() {
    return longNames;
  }

  /**
   * get all short option names.
   *
   * @return the short names
   */
  public String[] getShortNames() {
    return shortNames;
  }

  /**
   * get the mandatoryArgument value.
   *
   * @return true, if is argument required
   */
  public boolean isArgumentRequired() {
    return mandatoryArgument;
  }

  /**
   * get the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * get the argument.
   *
   * @return the argument
   */
  public Object getArgument() {
    return argument;
  }

  /**
   * set the argument.
   *
   * @param argument the argument
   */
  protected void setArgument(final Object argument) {

    this.argument = argument;
  }

  /**
   * get the argument.
   *
   * @return true, if is selected
   */
  public boolean isSelected() {
    return argument != null;
  }

  /**
   * set the argument for this option using the string value found by the parser.
   *
   * @param locale the locale
   * @param arg    the arg
   *
   * @throws CmdLineException the cmd line exception
   */
  abstract protected void setArgument(String arg, Locale locale) throws CmdLineException;

  /**
   * get the display name.
   *
   * @return the display name
   */
  public String getDisplayName() {

    if (displayName != null)
      return displayName;
    if (longNames != null && longNames.length > 0)
      return longNames[0];
    if (shortNames != null && shortNames.length > 0)
      return shortNames[0];
    return "unknown option";
  }

  /**
   * set the display name.
   *
   * @param displayName the display name
   */
  protected void setDisplayName(final String displayName) {

    this.displayName = displayName;
  }

  /**
   * parse the argument string found by the parser.
   *
   * @param locale   the locale
   * @param optName  the opt name
   * @param argument the argument
   *
   * @throws CmdLineException the cmd line exception
   */
  protected void parseOption(final String argument, final Locale locale, final String optName) throws CmdLineException {

    String arg = argument;
    setDisplayName(optName);
    if (!mandatoryArgument && arg != null)
      throw new CmdLineException.IllegalOptionArgumentException(this, arg);
    if (mandatoryArgument && arg == null)
      throw new CmdLineException.MissingOptionArgumentException(this);
    if (unescape && arg != null)
      arg = StringUtil.unescapeCString(arg);
    this.setArgument(arg, locale);
  }
}
