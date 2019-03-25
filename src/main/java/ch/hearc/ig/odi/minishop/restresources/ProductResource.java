/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.restresources;

import ch.hearc.ig.odi.minishop.business.Product;
import ch.hearc.ig.odi.minishop.exception.NullFormException;
import ch.hearc.ig.odi.minishop.exception.ProductException;
import ch.hearc.ig.odi.minishop.services.PersistenceService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class ProductResource {

  @Inject
  private PersistenceService persistenceService;

  @GET
  public List<Product> getProducts() {
    return persistenceService.getAllProducts();
  }

  @GET
  @Path("{id}")
  public Product getProduct(@PathParam("id") Long productid) throws ProductException {
      return persistenceService.getProductByID(productid);
  }


  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void deleteProduct(@PathParam("id") Long id) {
    try {
      persistenceService.deleteProduct(id);
    } catch (ProductException e) {
      e.printStackTrace();
      throw new NullFormException("product not deleted.");
    }
  }

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Product updateProduct(@PathParam("id") Long id, Product product) {
    try {
      return persistenceService.updateProduct(id, product);
    } catch (ProductException e) {
      e.printStackTrace();
      throw new NullFormException("product couldn't have been updated.");
    }
  }
}
