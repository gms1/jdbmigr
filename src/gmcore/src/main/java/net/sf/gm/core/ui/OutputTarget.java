/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.ui;

//
/**
 * The Interface OutputTarget.
 */
public interface OutputTarget {

  /** The LEVE l_ DEBUG. */
  static int LEVEL_DEBUG = 1;

  /** The LEVE l_ VERBOSE. */
  static int LEVEL_VERBOSE = 2;

  /** The LEVE l_ MESSAGE. */
  static int LEVEL_MESSAGE = 3;

  /** The LEVE l_ WARNING. */
  static int LEVEL_WARNING = 4;

  /** The LEVE l_ ERROR. */
  static int LEVEL_ERROR = 5;

  /**
   * Gets the message level.
   *
   * @return the message level
   */
  int getMessageLevel();

  /**
   * Sets the message level.
   *
   * @param level the level
   */
  void setMessageLevel(int level);

  /**
   * Debugln.
   *
   * @param message the message
   */
  void debugln(String message);

  /**
   * Verboseln.
   *
   * @param message the message
   */
  void verboseln(String message);

  /**
   * Messageln.
   *
   * @param message the message
   */
  void messageln(String message);

  /**
   * Warningln.
   *
   * @param message the message
   */
  void warningln(String message);

  /**
   * Warningln.
   *
   * @param message the message
   * @param e       the e
   */
  void warningln(String message, Throwable e);

  /**
   * Errorln.
   *
   * @param message the message
   */
  void errorln(String message);

  /**
   * Errorln.
   *
   * @param message the message
   * @param e       the e
   */
  void errorln(String message, Throwable e);

  /**
   * Outputln.
   *
   * @param level   the level
   * @param message the message
   * @param e       the e
   */
  void outputln(int level, String message, Throwable e);

  /**
   * Formatln.
   *
   * @param inputlevel the inputlevel
   * @param message    the message
   * @param e          the e
   *
   * @return the string
   */
  String formatln(int inputlevel, String message, Throwable e);

  /**
   * Write.
   *
   * @param text the text
   */
  void write(String text);
}
