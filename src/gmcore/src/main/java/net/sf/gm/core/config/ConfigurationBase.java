/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.config;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import net.sf.gm.core.properties.AppProperties;
import net.sf.gm.core.utils.ReaderInputStream;

//
/**
 * The Class ConfigurationImpl.
 */
public abstract class ConfigurationBase implements Configuration {

  /** The data. */
  private ConfigurationData data;

  /** The modified. */
  private boolean modified;

  /** The url. */
  private URL url;

  /** The default configuration. */
  private Configuration defaultConfiguration;

  /**
   * The Constructor.
   */
  public ConfigurationBase() {

    data = null;
    modified = false;
    this.url = null;
    this.defaultConfiguration = null;
  }

  /**
   * The Constructor.
   *
   * @param url the url
   */
  public ConfigurationBase(final URL url) {

    data = null;
    modified = false;
    this.url = url;
    this.defaultConfiguration = null;
  }

  /**
   * The Constructor.
   *
   * @param url                  the url
   * @param defaultConfiguration the default configuration
   */
  public ConfigurationBase(final URL url, final Configuration defaultConfiguration) {

    data = null;
    modified = false;
    this.url = url;
    this.defaultConfiguration = defaultConfiguration;
  }

  /**
   * Load.
   *
   * @param data the data
   *
   * @return true, if load
   */
  abstract protected boolean load(ConfigurationData data);

  /**
   * Load.
   *
   * @return true, if load
   */
  public synchronized boolean load() {

    data = new ConfigurationData("ROOT");
    boolean res = this.load(data);
    if (!res && defaultConfiguration != null)
      if (defaultConfiguration.load()) {
        data = defaultConfiguration.getStoredData("/");
        res = true;
      }
    modified = false;
    return res;
  }

  /**
   * Gets the stored data.
   *
   * @param node the node
   *
   * @return the stored data
   */
  public ConfigurationData getStoredData(final String node) {

    return data.findNode(node);
  }

  /**
   * Gets the properties.
   *
   * @param node the node
   *
   * @return the properties
   */
  public Properties getProperties(final String node) {

    final Properties props = data.findNodeProperties(node);
    if (props == null)
      return null;
    final Properties resProps = new Properties();
    final Set<Object> keys = resProps.keySet();
    final Iterator<Object> i = keys.iterator();
    while (i.hasNext()) {
      final String key = (String) i.next();
      resProps.put(key, props.getProperty(key));
    }
    return resProps;
  }

  /**
   * Gets the properties stream.
   *
   * @param node the node
   *
   * @return the properties stream
   */
  public InputStream getPropertiesStream(final String node) {

    Properties props = data.findNodeProperties(node);
    if (props == null)
      props = new Properties();
    return new ReaderInputStream(new ConfigurationReader((Properties) props.clone()));
  }

  /**
   * Removes the property.
   *
   * @param node the node
   * @param name the name
   */
  public synchronized void removeProperty(final String node, final String name) {

    final Properties props = data.findNodeProperties(node);
    if (props == null)
      return;
    final Object value = props.getProperty(name);
    if (value == null)
      return;
    props.remove(name);
    modified = true;
  }

  /**
   * Gets the stored property.
   *
   * @param node the node
   * @param name the name
   *
   * @return the stored property
   */
  public String getStoredProperty(final String node, final String name) {

    final Properties props = data.findNodeProperties(node);
    if (props == null)
      return null;
    return props.getProperty(name);
  }

  /**
   * Gets the property.
   *
   * @param node the node
   * @param name the name
   *
   * @return the property
   */
  public String getProperty(final String node, final String name) {

    return getProperty(node, name, null);
  }

  /**
   * Gets the property.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property
   */
  public String getProperty(final String node, final String name, final String defValue) {

    final Properties props = data.findNodeProperties(node);
    if (props == null)
      return defValue;
    return AppProperties.expandApplicationProperties(props.getProperty(name, defValue));
  }

  /**
   * Sets the property.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property
   */
  public synchronized boolean setProperty(final String node, final String name, final String value) {

    final Properties props = data.addNodeProprties(node);
    final String oldValue = props.getProperty(name);
    if (value == null) {
      if (oldValue != null)
        props.remove(name);
      return true;
    }
    if (value.equals(oldValue))
      return false;
    modified = true;
    props.setProperty(name, value);
    return true;
  }

  /**
   * Gets the property bool.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property bool
   */
  public boolean getPropertyBool(final String node, final String name, final boolean defValue) {

    final String v = getProperty(node, name);
    if (v == null)
      return defValue;
    if (v.equalsIgnoreCase("true") || v.equals("1"))
      return true;
    if (v.equalsIgnoreCase("false") || v.equals("0"))
      return false;
    return defValue;
  }

  /**
   * Sets the property bool.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property bool
   */
  public boolean setPropertyBool(final String node, final String name, final boolean value) {

    final String strValue = value ? "true" : "false";
    return setProperty(node, name, strValue);
  }

  /**
   * Gets the property integer.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property integer
   */
  public int getPropertyInteger(final String node, final String name, final int defValue) {

    final String v = getProperty(node, name);
    if (v == null)
      return defValue;
    try {
      return Integer.parseInt(v);
    } catch (final Exception e) {
    }
    return defValue;
  }

  /**
   * Sets the property integer.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property integer
   */
  public boolean setPropertyInteger(final String node, final String name, final int value) {

    final String strValue = Integer.toString(value);
    return setProperty(node, name, strValue);
  }

  /**
   * Gets the property double.
   *
   * @param node     the node
   * @param defValue the def value
   * @param name     the name
   *
   * @return the property double
   */
  public double getPropertyDouble(final String node, final String name, final double defValue) {

    final String v = getProperty(node, name);
    if (v == null)
      return defValue;
    try {
      return Double.parseDouble(v);
    } catch (final Exception e) {
    }
    return defValue;
  }

  /**
   * Sets the property double.
   *
   * @param value the value
   * @param node  the node
   * @param name  the name
   *
   * @return true, if set property double
   */
  public boolean setPropertyDouble(final String node, final String name, final double value) {

    final String strValue = Double.toString(value);
    return setProperty(node, name, strValue);
  }

  /**
   * Gets the keys.
   *
   * @param node the node
   *
   * @return the keys
   */
  public String[] getKeys(final String node) {

    final Properties props = data.findNodeProperties(node);
    if (props == null)
      return null;
    return props.keySet().toArray(new String[0]);
  }

  /**
   * Gets the child nodes.
   *
   * @param node the node
   *
   * @return the child nodes
   */
  public String[] getChildNodes(final String node) {

    return data.findNodeChildNodes(node);
  }

  /**
   * Removes the node.
   *
   * @param node the node
   */
  public void removeNode(final String node) {

    final ConfigurationData remData = data.findNode(node);
    remData.props = new Properties();
    remData.childNodes = new HashMap<String, ConfigurationData>();
  }

  /*
   * @return true if the properties have been modified
   */
  /**
   * Gets the modified.
   *
   * @return the modified
   */
  protected boolean getModified() {
    return modified;
  }

  /*
   * @param modified
   */
  /**
   * Sets the modified.
   *
   * @param modified the modified
   */
  protected void setModified(final boolean modified) {

    this.modified = modified;
  }

  /*
   * @return url
   */
  /**
   * Gets the URL.
   *
   * @return the URL
   */
  protected URL getURL() {
    return url;
  }

  /*
   * @param url
   */
  /**
   * Sets the URL.
   *
   * @param url the url
   */
  protected void setURL(final URL url) {
    this.url = url;
  }

  /*
   * @return defaultConfiguration
   */
  /**
   * Gets the default configuration.
   *
   * @return the default configuration
   */
  protected Configuration getDefaultConfiguration() {

    return defaultConfiguration;
  }

  /*
   * @param defaultConfiguration
   */
  /**
   * Sets the default configuration.
   *
   * @param defaultConfiguration the default configuration
   */
  protected void setDefaultConfiguration(final Configuration defaultConfiguration) {

    this.defaultConfiguration = defaultConfiguration;
  }
}
