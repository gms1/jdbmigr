/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.util.StringTokenizer;

import net.sf.gm.core.properties.SystemProperties;

//
/**
 * This class contains utility functions for dumping something.
 */
public class DumpUtil {

  /**
   * dump of "java.class.path" system property
   *
   * @return the classpath dump
   */
  public static String getClasspathDump() {

    final StringBuilder buf = new StringBuilder();
    final StringTokenizer paths = new StringTokenizer(
        SystemProperties.getClassPath(), SystemProperties.getPathSeperator());
    while (paths.hasMoreTokens())
      buf.append(paths.nextToken()).append("\n");
    return buf.toString();
  }

  /**
   * dump of the total, free and used memory.
   *
   * @return the memory dump
   */
  public static String getMemoryDump() {

    final Runtime rt = Runtime.getRuntime();
    final long total = rt.totalMemory();
    final long free = rt.freeMemory();
    final long used = total - free;
    return "memory: total = " + total + ", free = " + free + ", used = " + used;
  }

  /**
   * Gets the elapsed time.
   *
   * @param diffTime the diff time
   *
   * @return the elapsed time
   */
  public static String getElapsedTime(final long diffTime) {

    final long milliseconds = diffTime % 1000;
    long seconds = diffTime / 1000;
    long minutes = seconds / 60;
    seconds = seconds - minutes * 60;
    long hours = minutes / 60;
    minutes = minutes - hours * 60;
    final long days = hours / 24;
    hours = hours - days * 24;
    final StringBuilder buf = new StringBuilder();
    boolean printMode = false;
    if (days > 0) {
      printMode = true;
      buf.append(days);
      buf.append(" days ");
    }
    if (printMode || minutes > 0) {
      printMode = true;
      buf.append(minutes);
      buf.append(" minutes ");
    }

    buf.append(seconds);
    buf.append(".");

    buf.append(String.format("%03d", milliseconds));
    buf.append(" seconds ");

    return buf.toString();
  }
}
