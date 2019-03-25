/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.restresources;

import ch.hearc.ig.odi.minishop.business.Cart;
import ch.hearc.ig.odi.minishop.exception.CustomerException;
import ch.hearc.ig.odi.minishop.exception.NotFoundException;
import ch.hearc.ig.odi.minishop.exception.NullFormException;
import ch.hearc.ig.odi.minishop.exception.ProductException;
import ch.hearc.ig.odi.minishop.exception.StoreException;
import ch.hearc.ig.odi.minishop.services.PersistenceService;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("customer/{customerid}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class StoreResource {

  @Inject
  private PersistenceService persistenceService;


  @GET
  public List<Cart> getAllCustomerCarts(@PathParam("customerid") Long customerid){
    try {
      return persistenceService.getOpenCartsForCustomer(customerid);
    } catch (StoreException e) {
      e.printStackTrace();
      throw new NotFoundException("Customer not found");
    }
  }

  @GET
  @Path("{cartid}")
  public Cart customerIdCartCartIdGet(@PathParam("customerid") Long customerid, @PathParam("cartid") Long cartid){
    try {
      return persistenceService.getCartById(customerid, cartid);
    } catch (StoreException e) {
      e.printStackTrace();
      throw new NotFoundException("the cart does not exist");
    }
  }

  @POST
  @Path("{cartid}")
  public Cart customerIdCartCartIdPost(@PathParam("customerid")Long id, @PathParam("cartid") Long cartid, @FormParam("productid") Long productid, @FormParam("quantity")Long quantity){
    try{
      return persistenceService.addItemToAGivenCart(id,cartid, productid, quantity);
    } catch (StoreException e) {
      e.printStackTrace();
      throw new NullFormException("The item could not have been added. error"); //error 400
    } catch (ProductException e) {
      e.printStackTrace();
      throw new NullFormException("The item could not have been added. error"); //à enlever lorque l'on "corrige" les exceptions
    }
  }



  @GET
  @Path("{cartid}/total")
  public BigDecimal customerIdCartCartIdTotalGet(@PathParam("customerid") Long id, @PathParam("cartid") Long cartid){
    try{
      return persistenceService.getTotalPrice(id, cartid);
    } catch (StoreException e) {
      e.printStackTrace();
      throw new NotFoundException("The cart does not exist");
    }
  }

  @POST
  public Cart customerIdCartPost(@PathParam("customerid") Long customerid, @FormParam("productid") Long productid, @FormParam("quantity") Long quantity){
    try {
      return persistenceService.addItemToNewCart(customerid, productid, quantity);
    } catch (ProductException | CustomerException e) {
      e.printStackTrace();
      throw new NullFormException("item couldn't have been added. error");
    }
  }

}
