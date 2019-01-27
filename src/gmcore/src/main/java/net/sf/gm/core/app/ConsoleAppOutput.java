/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.app;

import net.sf.gm.core.ui.OutputConsole;
import net.sf.gm.core.ui.OutputTarget;

//
/**
 * The Class ConsoleAppOutput.
 */
public class ConsoleAppOutput extends OutputConsole implements AppOutputTarget {

  /**
   * The Constructor.
   */
  public ConsoleAppOutput() {

    super();
    log = null;
  }

  /** The log. */
  private OutputTarget log;

  /**
   * Gets the log target.
   *
   * @return the log target
   */
  public OutputTarget getLogTarget() {
    return log;
  }

  /**
   * Sets the log target.
   *
   * @param log the log
   */
  public void setLogTarget(final OutputTarget log) {
    this.log = log;
  }

  /**
   * Outputln.
   *
   * @param inputlevel the inputlevel
   * @param message    the message
   * @param e          the e
   */
  @Override
  public void outputln(final int inputlevel, final String message, final Throwable e) {

    if (log != null)
      log.outputln(inputlevel, message, e);
    super.outputln(inputlevel, message, e);
  }
}
