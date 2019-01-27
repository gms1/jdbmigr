/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.app;

import net.sf.gm.core.ui.OutputTarget;

//
/**
 * The Interface AppOutputTarget.
 */
public interface AppOutputTarget extends OutputTarget {

  /**
   * Gets the log target.
   *
   * @return the log target
   */
  public OutputTarget getLogTarget();

  /**
   * Sets the log target.
   *
   * @param log the log
   */
  public void setLogTarget(OutputTarget log);
}
