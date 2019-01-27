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
 * The Class DataSourceInfoArrayModel.
 */
public class DataSourceInfoArrayModel
    extends ModelServer<DataSourceInfoArrayModel> {

  /** The data source infos. */
  ArrayList<DataSourceInfo> dataSourceInfos = null;

  /**
   * The Constructor.
   *
   * @param dataSourceInfos the data source infos
   */
  public DataSourceInfoArrayModel(
      final ArrayList<DataSourceInfo> dataSourceInfos) {

    super();
    this.dataSourceInfos = dataSourceInfos;
  }

  /**
   * Gets the model.
   *
   * @return the model
   */
  @Override
  protected DataSourceInfoArrayModel getModel() {

    return this;
  }

  /**
   * Add.
   *
   * @param di the di
   */
  public void add(final DataSourceInfo di) {

    dataSourceInfos.add(di);
    notifyClients();
  }

  /**
   * Remove.
   *
   * @param idx the idx
   */
  public void remove(final int idx) {

    dataSourceInfos.remove(idx);
    notifyClients();
  }

  /**
   * Get.
   *
   * @param idx the idx
   *
   * @return the local data source info
   */
  public DataSourceInfo get(final int idx) { return dataSourceInfos.get(idx); }

  /**
   * Set.
   *
   * @param di  the di
   * @param idx the idx
   */
  public void set(final int idx, final DataSourceInfo di) {

    dataSourceInfos.set(idx, di);
    notifyClients();
  }

  /**
   * Size.
   *
   * @return the int
   */
  public int size() { return dataSourceInfos.size(); }

  /**
   * Gets the names.
   *
   * @return the names
   */
  public String[] getNames() {

    final int size = dataSourceInfos.size();
    final String[] names = new String[size];
    for (int i = 0; i < size; i++)
      names[i] = dataSourceInfos.get(i).getName();
    return names;
  }

  /**
   * Gets the data source infos.
   *
   * @return the data source infos
   */
  public ArrayList<DataSourceInfo> getDataSourceInfos() {

    return dataSourceInfos;
  }
}
