/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.gm.core.app.AbstractApplication;
import net.sf.gm.core.properties.AppProperties;

//
/**
 * command line option parser, like getopt, with support for:
 *
 * <pre>
 * short options:                         e.g.: -h
 * long options:                          e.g.: --help
 * options with multiple short names:     e.g.: --help &lt;=&gt; -h or -?
 * options with multiple long names
 * incorrect written options:             e.g.: -help --h
 * option arguments:                      e.g.: -l 3  --loglevel 3 --loglevel=3
 * stop option parsing argument:              : --
 * option bundle:                         e.g.: -x -z -v &lt;=&gt; -xzv
 * </pre>
 */
public class CmdLineParser {

  /**
   * constructor.
   */
  public CmdLineParser() {}

  /** The short options. */
  private Map<String, OptionBase> shortOptions = null;

  /** The long options. */
  private Map<String, OptionBase> longOptions = null;

  /** The all options. */
  private final ArrayList<OptionBase> allOptions = new ArrayList<OptionBase>();

  /** The curr locale. */
  private Locale currLocale = null;

  /** The arg syntax. */
  private String argSyntax;

  /** The minargs. */
  private int minargs;

  /** The maxargs. */
  private int maxargs;

  /** The arg description. */
  private String[] argDescription;

  /**
   * add the specified Option.
   *
   * @param opt the opt
   *
   * @return the option
   */
  public OptionBase add(final OptionBase opt) {

    allOptions.add(opt);
    return opt;
  }

  /**
   * Sets the argument description.
   *
   * @param minargs        the minargs
   * @param maxargs        the maxargs
   * @param argDescription the arg description
   * @param argSyntax      the arg syntax
   */
  public void setArgumentDescription(final String argSyntax, final int minargs,
                                     final int maxargs,
                                     final String[] argDescription) {

    this.argSyntax = argSyntax;
    this.minargs = minargs;
    this.maxargs = maxargs;
    this.argDescription = argDescription;
  }

  /**
   * parse arguments using the default locale print a usage message on error and
   * exit.
   *
   * @param argv the argv
   *
   * @return the options
   */
  public String[] getOptions(final String[] argv) {

    return getOptions(argv, Locale.getDefault());
  }

  /**
   * parse arguments using the specified locale print a usage message if help
   * option is selected and exit.
   *
   * @param locale the locale
   * @param argv   the argv
   *
   * @return the options
   */
  public String[] getOptions(final String[] argv, final Locale locale) {

    String[] res = null;
    try {
      res = parseOptions(argv, locale);
    } catch (final CmdLineException.HelpOptionSelected eHelp) {
      printUsage(locale);
      AbstractApplication.exit();
    } catch (final Exception e) {
      printUsage(locale);
      AbstractApplication.errorln("", e);
      AbstractApplication.exit();
    }
    return res;
  }

  /**
   * parse arguments using the default locale.
   *
   * @param argv the argv
   *
   * @return the string[]
   *
   * @throws CmdLineException the cmd line exception
   */
  public String[] parseOptions(final String[] argv) throws CmdLineException {

    return parseOptions(argv, Locale.getDefault());
  }

  /**
   * parse arguments using the specified locale.
   *
   * @param locale the locale
   * @param argv   the argv
   *
   * @return the string[]
   *
   * @throws CmdLineException the cmd line exception
   */
  public String[] parseOptions(final String[] argv, final Locale locale)
      throws CmdLineException {

    onBeginParsing(locale);
    final ArrayList<String> otherArgs = new ArrayList<String>();
    if (argv == null)
      return onEndParsing(otherArgs, minargs, maxargs);
    int position = 0;
    while (position < argv.length) {
      final String currArg = argv[position];
      if (!currArg.startsWith("-")) {
        // current argument is not an option
        otherArgs.add(argv[position]);
        position += 1;
        continue;
      }
      if (currArg.equals("--")) {
        // current argument is a "stop option parsing" marker
        for (; position < argv.length; ++position)
          otherArgs.add(argv[position]);
        break;
      }
      // we have found an option or option bundle
      OptionBase opt = null;
      String valueArg = null;
      String optName = null;
      final int prefixLen = currArg.startsWith("--") ? 2 : 1;
      final int equalsPos = currArg.indexOf("=");
      if (equalsPos == -1)
        optName = currArg.substring(prefixLen);
      else {
        valueArg = currArg.substring(equalsPos + 1);
        optName = currArg.substring(prefixLen, equalsPos);
      }
      // test if the current option is valid option
      if (prefixLen == 1) {
        // prefer short options
        opt = shortOptions.get(optName);
        if (opt == null)
          opt = longOptions.get(optName);
      } else {
        // prefer long options
        opt = longOptions.get(optName);
        if (opt == null)
          opt = shortOptions.get(optName);
      }
      // test if the current option is an option bundle
      if (opt == null && IsOptionBundle(optName)) {
        // select all options but the last one
        // the last option may have an argument
        parseOptionBundle(optName.substring(0, optName.length() - 1));
        // continue with the last option
        optName = optName.substring(optName.length() - 1);
        opt = shortOptions.get(optName);
      }
      if (opt == null)
        throw new CmdLineException.UnknownOptionException(currArg);
      // we have found a valid option
      if (valueArg == null && opt.isArgumentRequired()) {
        // the next argument should be the argument for the current
        // option
        position += 1;
        if (position != argv.length)
          valueArg = argv[position];
      }
      // set the argument for the current option
      opt.parseOption(valueArg, currLocale, currArg);
      if (opt.isForHelpUsage())
        throw new CmdLineException.HelpOptionSelected();
      // continue with the next argument
      position += 1;
    }
    return onEndParsing(otherArgs, minargs, maxargs);
  }

  /**
   * start parsing.
   *
   * @param locale the locale
   *
   * @throws CmdLineException the cmd line exception
   */
  protected void onBeginParsing(final Locale locale) throws CmdLineException {

    currLocale = locale;
    shortOptions = new HashMap<String, OptionBase>();
    longOptions = new HashMap<String, OptionBase>();
    for (int i = 0; i < allOptions.size(); i++) {
      OptionBase opt = allOptions.get(i);
      opt.reset();
      String[] names;
      names = opt.getShortNames();
      if (names != null) {
        for (int j = 0; j < names.length; j++) {
          if (shortOptions.containsKey(names[j]))
            throw new CmdLineException.DuplicateOptionNameException(names[j]);
          if (longOptions.containsKey(names[j]))
            throw new CmdLineException.DuplicateOptionNameException(names[j]);
          shortOptions.put(names[j], opt);
        }
      }
      names = opt.getLongNames();
      if (names != null) {
        for (int j = 0; j < names.length; j++) {
          if (longOptions.containsKey(names[j]))
            throw new CmdLineException.DuplicateOptionNameException(names[j]);
          if (shortOptions.containsKey(names[j]))
            throw new CmdLineException.DuplicateOptionNameException(names[j]);
          longOptions.put(names[j], opt);
        }
      }
    }
  }

  /**
   * On end parsing.
   *
   * @param minargs the minargs
   * @param maxargs the maxargs
   * @param arglist the arglist
   *
   * @return the string[]
   *
   * @throws CmdLineException the cmd line exception
   */
  protected String[] onEndParsing(final ArrayList<String> arglist,
                                  final int minargs, final int maxargs)
      throws CmdLineException {

    final String[] remainingArgs = arglist.toArray(new String[arglist.size()]);
    if (minargs >= 0 && remainingArgs.length < minargs)
      throw new CmdLineException.NotEnoughArgumentsException();
    if (maxargs >= 0 && remainingArgs.length > maxargs)
      throw new CmdLineException.TooManyArguments();
    return remainingArgs;
  }

  /**
   * test if option argument is a option bundle.
   *
   * @param arg the arg
   *
   * @return true, if is option bundle
   */
  protected boolean IsOptionBundle(final String arg) {

    String s = arg;
    String o = null;
    while (s.length() > 0) {
      o = s.substring(0, 1);
      if (shortOptions.get(o) == null)
        return false;
      s = s.substring(1);
    }
    return true;
  }

  /**
   * parse Option Bundle.
   *
   * @param arg the arg
   *
   * @throws CmdLineException the cmd line exception
   */
  protected void parseOptionBundle(final String arg) throws CmdLineException {

    String nextName = arg;
    while (nextName.length() > 0) {
      final String optName = nextName.substring(0, 1);
      nextName = nextName.substring(1);
      final OptionBase opt = shortOptions.get(optName);
      opt.parseOption(null, currLocale, arg);
    }
  }

  /**
   * print usage using the default locale.
   */
  public void printUsage() {

    printUsage(currLocale != null ? currLocale : Locale.getDefault());
  }

  /**
   * print usage using the specified locale.
   *
   * @param locale the locale
   */
  public void printUsage(final Locale locale) {

    AbstractApplication.messageln("usage: " + AppProperties.getApplication() +
                                  " OPTIONS " + argSyntax);
    AbstractApplication.messageln("");
    printDelimiterUsageLine("OPTIONS:");
    AbstractApplication.messageln("");
    for (int i = 0; i < allOptions.size(); i++) {
      final OptionBase opt = allOptions.get(i);
      printOptionUsage(opt, locale);
    }
    if (argDescription != null) {
      AbstractApplication.messageln("");
      AbstractApplication.messageln("  ARGUMENTS:");
      AbstractApplication.messageln("");
      for (final String element : argDescription) {
        final String[] lines = element.split("\n");
        for (final String cols : lines) {
          final String[] columns = cols.split("\t", 2);
          printArgumentUsageLine(columns.length > 0 ? columns[0] : "",
                                 columns.length > 1 ? columns[1] : "");
        }
      }
    }
  }

  /** The Constant PROP_USAGE_LINE_FORMAT. */
  protected static final String PROP_USAGE_LINE_FORMAT = "%4s%-26s%s";

  /**
   * print the usage information for the specified option.
   *
   * @param locale the locale
   * @param opt    the opt
   */
  public void printOptionUsage(final OptionBase opt, final Locale locale) {

    int linenr = 0;
    final String valueStr = opt.isArgumentRequired() ? "<value>" : "";
    final String description =
        opt.getDescription() == null ? "option" : opt.getDescription();
    final String[] descriptionLines = description.split("\n");
    final String[] shortNames = opt.getShortNames();
    if (shortNames != null)
      for (int i = 0; i < shortNames.length; i++, linenr++)
        printShortOptionUsageLine(
            shortNames[i], valueStr,
            linenr < descriptionLines.length ? descriptionLines[linenr] : "");
    final String[] longNames = opt.getLongNames();
    if (longNames != null)
      for (int i = 0; i < longNames.length; i++, linenr++)
        printLongOptionUsageLine(
            longNames[i], valueStr,
            linenr < descriptionLines.length ? descriptionLines[linenr] : "");
    if (shortNames != null || longNames != null)
      for (; linenr < descriptionLines.length; linenr++)
        printDescriptionUsageLine(descriptionLines[linenr]);
    else
      for (; linenr < descriptionLines.length; linenr++)
        printDelimiterUsageLine(descriptionLines[linenr]);
    AbstractApplication.messageln("");
  }

  /**
   * print a line containing the usage information for a short option.
   *
   * @param valueStr    the value indicator
   * @param description the description
   * @param name        the name
   */
  protected void printShortOptionUsageLine(final String name,
                                           final String valueStr,
                                           final String description) {

    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append(" ");
    sb.append(valueStr);
    AbstractApplication.messageln(String.format(
        CmdLineParser.PROP_USAGE_LINE_FORMAT, "-", sb.toString(), description));
  }

  /**
   * print a line containing the usage information for a long option.
   *
   * @param valueStr    the value indicator
   * @param description the description
   * @param name        the name
   */
  protected void printLongOptionUsageLine(final String name,
                                          final String valueStr,
                                          final String description) {

    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append(valueStr.length() > 0 ? "=" : " ");
    sb.append(valueStr);
    AbstractApplication.messageln(
        String.format(CmdLineParser.PROP_USAGE_LINE_FORMAT, "--", sb.toString(),
                      description));
  }

  /**
   * print a line containing the usage information for an argument.
   *
   * @param description the description
   * @param name        the name
   */
  protected void printArgumentUsageLine(final String name,
                                        final String description) {

    AbstractApplication.messageln(String.format(
        CmdLineParser.PROP_USAGE_LINE_FORMAT, "", name, description));
  }

  /**
   * print a line containig the a description.
   *
   * @param description the description
   */
  protected void printDescriptionUsageLine(final String description) {

    AbstractApplication.messageln(String.format(
        CmdLineParser.PROP_USAGE_LINE_FORMAT, "", "", description));
  }

  /**
   * print a line containig the delimiter description.
   *
   * @param description the description
   */
  protected void printDelimiterUsageLine(final String description) {

    AbstractApplication.messageln("  " + description);
  }
}
