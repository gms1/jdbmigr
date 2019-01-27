/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.model;

//
/**
 * The Class ModelClient.
 *
 * @param <M>
 */
public abstract class ModelClient<M> {

  /** The server. */
  ModelServer<M> server = null;

  /** The registered. */
  boolean registered = false;

  /**
   * The Constructor.
   *
   * @param server the server
   */
  public ModelClient(final ModelServer<M> server) {

    setServer(server);
    connect();
  }

  /**
   * The Constructor.
   *
   * @param connect the connect
   * @param server  the server
   */
  public ModelClient(final ModelServer<M> server, final boolean connect) {

    setServer(server);
    if (connect)
      connect();
  }

  /**
   * The Constructor.
   */
  public ModelClient() {}

  /**
   * Connect.
   */
  public void connect() {

    if (server != null && !registered) {
      server.RegisterClient(this);
      registered = true;
    }
  }

  /**
   * Disconnect.
   */
  public void disconnect() {

    if (registered) {
      this.server.UnRegisterClient(this);
      registered = false;
    }
  }

  /**
   * Sets the server.
   *
   * @param server the server
   */
  public void setServer(final ModelServer<M> server) {

    if (this.server != server && registered)
      disconnect();
    this.server = server;
  }

  /**
   * Model changed.
   *
   * @param model the model
   */
  public abstract void modelChanged(M model);

  /**
   * Server changed.
   *
   * @param model the model
   */
  protected void serverChanged(final M model) { this.modelChanged(model); }
}
