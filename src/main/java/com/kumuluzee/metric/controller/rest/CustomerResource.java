package com.kumuluzee.metric.controller.rest;


import com.kumuluzee.metric.model.rest.Customer;
import com.kumuluzee.metric.util.db.Database;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("customers")
@RequestScoped
public class CustomerResource {

  @Inject
  @Metric(name = "customer_counter")
  private Counter customerCounter;

  @Inject
  @Metric(name = "first_name_length_histogram")
  private Histogram nameLengthHistogram;

  @Inject
  @Metric(name = "customer_adding_meter")
  private Meter addMeter;

  @GET
  public Response getAllCustomers() {
    List<Customer> customerList = Database.getCustomers();
    return Response.ok(customerList).build();
  }

  @GET
  @Path("{customerId}")
  public Response getCustomer(@PathParam("customerId") int customerId) {
    Customer customer = Database.getCustomer(customerId);
    if (customer != null) {
      return Response.ok(customer).build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("add-sample-names")
  @Timed(name = "add-sample-names-timer")
  public Response addSampleNames() {
    addNewCustomer(new Customer(Database.getCustomers().size(), "imie1", "nazwisko1"));
    addNewCustomer(new Customer(Database.getCustomers().size(), "iie2", "nazwisk2"));
    addNewCustomer(new Customer(Database.getCustomers().size(), "iie3", "nazwis3"));
    addNewCustomer(new Customer(Database.getCustomers().size(), "ie4", "nako4"));
    addNewCustomer(new Customer(Database.getCustomers().size(), "iiiiimie5", "nisko5"));

    return Response.noContent().build();
  }

  @POST
  public Response addNewCustomer(Customer customer) {
    addMeter.mark();
    customerCounter.inc();
    nameLengthHistogram.update(customer.getFirstName().length());
    Database.addCustomer(customer);
    return Response.noContent().build();
  }

  @DELETE
  @Path("{customerId}")
  @Metered(name = "customer_deleting_meter")
  public Response deleteCustomer(@PathParam("customerId") int customerId) {
    customerCounter.dec();
    Database.deleteCustomer(customerId);
    return Response.noContent().build();
  }

  @Gauge(name = "customer_count_gauge", unit = MetricUnits.NONE)
  private int getCustomerCount() {
    return Database.getCustomers().size();
  }
}
