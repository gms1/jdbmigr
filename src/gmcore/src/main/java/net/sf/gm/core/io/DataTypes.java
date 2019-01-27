/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.io;

/**
 * The Class DataTypes.
 */
public class DataTypes {

  /** The Constant UnknownType. */
  public final static int UnknownType = -9999;

  /** The Constant UnknownTypeName. */
  public final static String UnknownTypeName = "UNKNOWN";

  /**
   * The Enum rowType.
   */
  public enum rowType {

    /** The UNKNOWN. */
    UNKNOWN,

    /** The CURRENT. */
    CURRENT,

    /** The INSERT. */
    INSERT,

    /** The UPDATE. */
    UPDATE,

    /** The DELETE. */
    DELETE,
  }
  ;
}
