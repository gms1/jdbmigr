/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.datasource.config;

//
/**
 * The Class DataSourceInfo.
 */
public class DataSourceInfo {

  /** The name. */
  private String name;

  /** The class name. */
  private String className;

  /** The url. */
  private String url;

  /**
   * The Constructor.
   *
   * @param className the class name
   * @param url       the url
   * @param name      the name
   */
  public DataSourceInfo(final String name, final String className,
                        final String url) {

    this.name = name;
    this.className = className;
    this.url = url;
  }

  /**
   * The Constructor.
   */
  public DataSourceInfo() {

    this.name = null;
    this.className = null;
    this.url = null;
  }

  /**
   * Gets the class name.
   *
   * @return the class name
   */
  public String getClassName() { return className; }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() { return name; }

  /**
   * Gets the url.
   *
   * @return the url
   */
  public String getUrl() { return url; }

  /**
   * Sets the class name.
   *
   * @param className the class name
   */
  public void setClassName(final String className) {

    this.className = className;
  }

  /**
   * Sets the name.
   *
   * @param name the name
   */
  public void setName(final String name) { this.name = name; }

  /**
   * Sets the url.
   *
   * @param url the url
   */
  public void setUrl(final String url) { this.url = url; }

  /**
   * Clone.
   *
   * @return the object
   */
  @Override
  public Object clone() {

    return new DataSourceInfo(name, className, url);
  }
}
