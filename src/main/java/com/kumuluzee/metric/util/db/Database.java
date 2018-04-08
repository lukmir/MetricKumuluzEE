package com.kumuluzee.metric.util.db;

import com.kumuluzee.metric.model.rest.Customer;

import java.util.ArrayList;
import java.util.List;

public class Database {

  public static List<Customer> customerList = new ArrayList<>();

  public static List<Customer> getCustomers() {
    return customerList;
  }

  public static void addCustomer(Customer customer) {
    customerList.add(customer);
  }

  public static void deleteCustomer(String customerId) {
    for (Customer customer : customerList) {
      if (customer.getId().equals(customerId)) {
        customerList.remove(customer);
        break;
      }
    }
  }
}
