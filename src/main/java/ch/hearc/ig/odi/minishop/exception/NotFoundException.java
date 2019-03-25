/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotFoundException extends WebApplicationException {

  public NotFoundException(String message) {
    super(Response.status(Status.NOT_FOUND)
        .entity(message).type(MediaType.TEXT_PLAIN).build());
  }
}