/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import java.io.InputStream;
import java.util.Properties;

//
/**
 * The Interface Configuration.
 */
public interface Configuration {

  /**
   * Load.
   *
   * @return true, if load
   */
  boolean load();

  /**
   * Gets the stored data.
   *
   * @param node the node
   *
   * @return the stored data
   */
  ConfigurationData getStoredData(String node);

  /**
   * Gets the keys.
   *
   * @param node the node
   *
   * @return the keys
   */
  String[] getKeys(String node);

  /**
   * Gets the properties.
   *
   * @param node the node
   *
   * @return the properties
   */
  Properties getProperties(String node);

  /**
   * Gets the child nodes.
   *
   * @param node the node
   *
   * @return the child nodes
   */
  String[] getChildNodes(String node);

  /**
   * Removes the node.
   *
   * @param node the node
   */
  void removeNode(String node);

  /**
   * Gets the properties stream.
   *
   * @param node the node
   *
   * @return the properties stream
   */
  InputStream getPropertiesStream(String node);

  // Property
  /**
   * Gets the stored property.
   *
   * @param node the node
   * @param name the name
   *
   * @return the stored property
   */
  String getStoredProperty(String node, String name);

  /**
   * Removes the property.
   *
   * @param node the node
   * @param name the name
   */
  void removeProperty(String node, String name);

  /**
   * Gets the property.
   *
   * @param node the node
   * @param name the name
   *
   * @return the property
   */
  String getProperty(String node, String name);

  /**
   * Gets the property.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property
   */
  String getProperty(String node, String name, String defValue);

  /**
   * Sets the property.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property
   */
  boolean setProperty(String node, String name, String value);

  /**
   * Gets the property bool.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property bool
   */
  boolean getPropertyBool(String node, String name, boolean defValue);

  /**
   * Sets the property bool.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property bool
   */
  boolean setPropertyBool(String node, String name, boolean value);

  /**
   * Gets the property integer.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property integer
   */
  int getPropertyInteger(String node, String name, int defValue);

  /**
   * Sets the property integer.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property integer
   */
  boolean setPropertyInteger(String node, String name, int value);

  /**
   * Gets the property double.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property double
   */
  double getPropertyDouble(String node, String name, double defValue);

  /**
   * Sets the property double.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property double
   */
  boolean setPropertyDouble(String node, String name, double value);
}
