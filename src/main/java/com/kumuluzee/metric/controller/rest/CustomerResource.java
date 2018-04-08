package com.kumuluzee.metric.controller.rest;


import com.kumuluzee.metric.model.rest.Customer;
import com.kumuluzee.metric.util.db.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("customers")
public class CustomerResource {

  @GET
  public Response getAllCustomers() {
    List<Customer> customerList = Database.getCustomers();
    return Response.ok(customerList).build();
  }

  @POST
  public Response addNewCustomer(Customer customer) {
    Database.addCustomer(customer);
    return Response.noContent().build();
  }

  @DELETE
  @Path("{customerId}")
  public Response deleteCustomer(@PathParam("customerId") String customerId) {
    Database.deleteCustomer(customerId);
    return Response.noContent().build();
  }
}
