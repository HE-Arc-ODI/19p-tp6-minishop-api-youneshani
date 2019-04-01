/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.restresources;

import ch.hearc.ig.odi.minishop.business.Customer;
import ch.hearc.ig.odi.minishop.exception.CustomerException;
import ch.hearc.ig.odi.minishop.services.PersistenceService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("customer")
public class CustomerResource {
    @Inject
    private PersistenceService persistenceService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Customer> findAllCustomer() {
        return persistenceService.getAllCustomers();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerByID(@PathParam("id") Long id) throws CustomerException {
        return persistenceService.getCustomerByID(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public void createCustomer(String username, String firstname, String lastname,
                               String email,
                               String phone) {
        persistenceService.createAndPersistCustomer(username, firstname, lastname, email, phone);
    }

    @DELETE
    @Path("{id}")
    public void deleteCustomer(@PathParam("id") Long id) throws CustomerException {
        persistenceService.deleteCustomer(id);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateCustomer(@PathParam("id") Long id, Customer customer) throws CustomerException {
        persistenceService.updateCustomer(id, customer);
    }
}

