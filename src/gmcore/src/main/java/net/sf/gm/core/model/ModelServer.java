/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.model;

import java.util.ArrayList;

//
/**
 * The Class ModelServer.
 *
 * @param <M>
 */
public abstract class ModelServer<M> {

  /** The clients. */
  private ArrayList<ModelClient<M>> clients = null;

  /**
   * The Constructor.
   */
  public ModelServer() { clients = new ArrayList<ModelClient<M>>(); }

  /**
   * Gets the model.
   *
   * @return the model
   */
  protected abstract M getModel();

  /**
   * Register client.
   *
   * @param client the client
   */
  public void RegisterClient(final ModelClient<M> client) {

    if (!this.clients.contains(client)) {
      this.clients.add(client);
      notifyClient(client);
    }
  }

  /**
   * Un register client.
   *
   * @param client the client
   */
  public void UnRegisterClient(final ModelClient<M> client) {

    this.clients.remove(client);
  }

  /**
   * Notify clients.
   */
  public void notifyClients() {

    for (final ModelClient<M> client : this.clients)
      notifyClient(client);
  }

  /**
   * Notify client.
   *
   * @param client the client
   */
  protected void notifyClient(final ModelClient<M> client) {

    client.serverChanged(this.getModel());
  }
}
