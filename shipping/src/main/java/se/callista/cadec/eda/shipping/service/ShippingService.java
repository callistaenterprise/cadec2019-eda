package se.callista.cadec.eda.shipping.service;

import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;

public interface ShippingService {

  void createShipping(Order order, Customer customer);

}
