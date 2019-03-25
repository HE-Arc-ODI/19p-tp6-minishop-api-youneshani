/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.injection;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * Configures the runtime context to load an instance of the service binder. Required for correct
 * injection of the RestService.
 */
@Provider
public class ServiceFeature implements Feature {

  @Override
  public boolean configure(final FeatureContext context) {
    context.register(new ServiceBinder());
    return true;
  }
}