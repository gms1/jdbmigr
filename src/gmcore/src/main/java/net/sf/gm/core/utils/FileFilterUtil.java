/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import net.sf.gm.core.properties.SystemProperties;

//
/**
 * The Class FileFilterUtil.
 */
public class FileFilterUtil {

  /**
   * Filter.
   *
   * @param recursive            the recursive
   * @param args                 the args
   * @param defaultFilterPattern the default filter pattern
   *
   * @return the array list< file>
   */
  public static ArrayList<File> filter(final String[] args,
                                       final boolean recursive,
                                       final String defaultFilterPattern) {

    final ArrayList<File> resultList = new ArrayList<File>();

    FileFilterUtil.filter(resultList, args, recursive, defaultFilterPattern);
    return resultList;
  }

  /**
   * Filter.
   *
   * @param resultList           the result list
   * @param recursive            the recursive
   * @param args                 the args
   * @param defaultFilterPattern the default filter pattern
   */
  public static void filter(final ArrayList<File> resultList,
                            final String[] args, final boolean recursive,
                            final String defaultFilterPattern) {

    for (final String arg : args)
      FileFilterUtil.filter(resultList, arg, recursive, defaultFilterPattern);
  }

  /**
   * Filter.
   *
   * @param arg                  the arg
   * @param recursive            the recursive
   * @param defaultFilterPattern the default filter pattern
   *
   * @return the array list< file>
   */
  public static ArrayList<File> filter(final String arg,
                                       final boolean recursive,
                                       final String defaultFilterPattern) {

    final ArrayList<File> resultList = new ArrayList<File>();

    FileFilterUtil.filter(resultList, arg, recursive, defaultFilterPattern);
    return resultList;
  }

  /**
   * Filter.
   *
   * @param arg                  the arg
   * @param resultList           the result list
   * @param recursive            the recursive
   * @param defaultFilterPattern the default filter pattern
   */
  public static void filter(final ArrayList<File> resultList, final String arg,
                            final boolean recursive,
                            final String defaultFilterPattern) {

    final File file = new File(arg);
    if (file.exists()) {
      if (file.isFile()) {
        resultList.add(file);
        return;
      }
      final Pattern defaultPattern = Pattern.compile(
          FileFilterUtil.wildcardToRegex(defaultFilterPattern),
          SystemProperties.isWindows() ? Pattern.CASE_INSENSITIVE : 0);
      FileFilterUtil.searchdir(resultList, file, defaultPattern, recursive);
      return;
    }
    File dir = file.getParentFile();
    if (dir == null)
      dir = new File(".").getAbsoluteFile();
    final String name = file.getName();
    final Pattern pattern = Pattern.compile(
        FileFilterUtil.wildcardToRegex(name),
        SystemProperties.isWindows() ? Pattern.CASE_INSENSITIVE : 0);
    FileFilterUtil.searchdir(resultList, dir, pattern, recursive);
  }

  /**
   * Searchdir.
   *
   * @param fileFilterPattern the file filter pattern
   * @param resultList        the result list
   * @param recursive         the recursive
   * @param dir               the dir
   */
  public static void searchdir(final ArrayList<File> resultList, final File dir,
                               final Pattern fileFilterPattern,
                               final boolean recursive) {

    if (recursive) {
      // get all subdirectories
      final File[] dirs = dir.listFiles(new SubDirectoryFilter());
      for (File element : dirs)
        FileFilterUtil.searchdir(resultList, element, fileFilterPattern,
                                 recursive);
    }
    // search this directory for files
    final File[] files = dir.listFiles(new WildcardFilter(fileFilterPattern));
    for (File element : files)
      resultList.add(element);
  }

  /**
   * The Class WildcardFilter.
   */
  public static class WildcardFilter implements FileFilter {

    /** The file filter pattern. */
    Pattern fileFilterPattern;

    /** The Constant hasDotPattern. */
    static final Pattern hasDotPattern = Pattern.compile("\\.");

    /**
     * The Constructor.
     *
     * @param fileFilterPattern the file filter pattern
     */
    public WildcardFilter(final Pattern fileFilterPattern) {

      this.fileFilterPattern = fileFilterPattern;
    }

    /**
     * Accept.
     *
     * @param file the file
     *
     * @return true, if accept
     */
    public boolean accept(final File file) {

      if (!file.isFile())
        return false;
      String fileName = file.getName();

      // each filename should have a dot
      // to match the "*.*" pattern
      if (!WildcardFilter.hasDotPattern.matcher(fileName).matches())
        fileName = fileName + ".";

      return fileFilterPattern.matcher(fileName).matches();
    }
  };

  /**
   * The Class SubDirectoryFilter.
   */
  public static class SubDirectoryFilter implements FileFilter {

    /**
     * The Constructor.
     */
    public SubDirectoryFilter() { super(); }

    /**
     * Accept.
     *
     * @param direntry the direntry
     *
     * @return true, if accept
     */
    public boolean accept(final File direntry) {

      return direntry.isDirectory();
    }
  };

  /**
   * Wildcard to regex.
   *
   * @param wildcard the wildcard
   *
   * @return the string
   */
  public static String wildcardToRegex(final String wildcard) {

    final StringBuffer s = new StringBuffer(wildcard.length());
    s.append('^');
    final int len = wildcard.length();
    for (int i = 0; i < len; i++) {
      final char c = wildcard.charAt(i);
      switch (c) {
      case '*':
        s.append(".*");
        break;

      case '?':
        s.append(".");
        break;

      case '[':
      case ']': // escape special regexp-characters
      case '(':
      case ')':
      case '{':
      case '}':
      case '^':
      case '$':
      case '.':
      case '|':
      case '\\':
        s.append("\\");
        s.append(c);
        break;

      default:
        s.append(c);
        break;
      }
    }
    s.append('$');
    return (s.toString());
  }
}
