/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.restresources;

import ch.hearc.ig.odi.minishop.business.Order;
import ch.hearc.ig.odi.minishop.exception.NotFoundException;
import ch.hearc.ig.odi.minishop.exception.OrderException;
import ch.hearc.ig.odi.minishop.services.PersistenceService;
import java.text.ParseException;
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


@Path("order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class OrderResource {

  @Inject
  private PersistenceService persistenceService;

  @GET
  public List<Order> orderGet() {
    return persistenceService.getAllOrders();
  }

  @GET
  @Path("{id}")
  public Order orderIdGet(@PathParam("id") Long orderid) {
    try {
      return persistenceService.getOrderById(orderid);
    } catch (OrderException e) {
      e.printStackTrace();
      throw new NotFoundException("the order does not exist");
    }
  }

  @POST
  public Order orderPost(@FormParam("cartid") Long cartid) throws ParseException, OrderException {
      return persistenceService.createOrder(cartid);
  }
}
