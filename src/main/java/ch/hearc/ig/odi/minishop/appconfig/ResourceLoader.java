/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.appconfig;

import ch.hearc.ig.odi.minishop.injection.ServiceBinder;
import ch.hearc.ig.odi.minishop.injection.ServiceFeature;
import ch.hearc.ig.odi.minishop.restresources.CustomerResource;
import ch.hearc.ig.odi.minishop.restresources.OrderResource;
import ch.hearc.ig.odi.minishop.restresources.ProductResource;
import ch.hearc.ig.odi.minishop.restresources.StoreResource;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Registers all resources with Jersey
 */
public class ResourceLoader extends ResourceConfig {

  public ResourceLoader() {
    register(CustomerResource.class);
    register(ProductResource.class);
    register(OrderResource.class);
    register(StoreResource.class);
    register(ServiceFeature.class);
    registerInstances(new ServiceBinder());
  }

}