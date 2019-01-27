/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.jdbc.datasource.config;

import java.util.ArrayList;

import net.sf.gm.core.model.ModelServer;

//
/**
 * The Class DriverClassInfoArrayModel.
 */
public class DriverClassInfoArrayModel
    extends ModelServer<DriverClassInfoArrayModel> {

  /** The class infos. */
  ArrayList<DriverClassInfo> classInfos = null;

  /**
   * The Constructor.
   *
   * @param classInfos the class infos
   */
  public DriverClassInfoArrayModel(
      final ArrayList<DriverClassInfo> classInfos) {

    super();
    this.classInfos = classInfos;
  }

  /**
   * Gets the model.
   *
   * @return the model
   */
  @Override
  protected DriverClassInfoArrayModel getModel() {

    return this;
  }

  /**
   * Add.
   *
   * @param ci the ci
   */
  public void add(final DriverClassInfo ci) {

    classInfos.add(ci);
    notifyClients();
  }

  /**
   * Remove.
   *
   * @param idx the idx
   */
  public void remove(final int idx) {

    classInfos.remove(idx);
    notifyClients();
  }

  /**
   * Get.
   *
   * @param idx the idx
   *
   * @return the local class info
   */
  public DriverClassInfo get(final int idx) { return classInfos.get(idx); }

  /**
   * Set.
   *
   * @param idx the idx
   * @param ci  the ci
   */
  public void set(final int idx, final DriverClassInfo ci) {

    classInfos.set(idx, ci);
    notifyClients();
  }

  /**
   * Size.
   *
   * @return the int
   */
  public int size() { return classInfos.size(); }

  /**
   * Gets the names.
   *
   * @return the names
   */
  public String[] getNames() {

    final int size = classInfos.size();
    final String[] names = new String[size];
    for (int i = 0; i < size; i++)
      names[i] = classInfos.get(i).getName();
    return names;
  }

  /**
   * Gets the class infos.
   *
   * @return the class infos
   */
  public ArrayList<DriverClassInfo> getClassInfos() { return classInfos; }
}
