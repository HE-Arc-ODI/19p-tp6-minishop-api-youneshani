/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.injection;

import ch.hearc.ig.odi.minishop.services.PersistenceService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Performs the singleton binding for the RestService mockup persistence object.
 */
public class ServiceBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(new PersistenceService()).to(PersistenceService.class);
  }
}
