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
 * The Interface Progress.
 */
public interface Progress extends OutputTarget {

  /**
   * Gets the progress.
   *
   * @return the progress
   */
  int getProgress();

  /**
   * Increment progress.
   *
   * @param amount the amount
   */
  void incrementProgress(int amount);

  /**
   * Sets the progress.
   *
   * @param progress the progress
   */
  void setProgress(int progress);

  /**
   * Gets the cancel.
   *
   * @return the cancel
   */
  boolean getCancel();

  /**
   * Sets the cancel.
   *
   * @param cancel the cancel
   */
  void setCancel(boolean cancel);
}
