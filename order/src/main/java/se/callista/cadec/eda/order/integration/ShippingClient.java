package se.callista.cadec.eda.order.integration;

import se.callista.cadec.eda.order.domain.Order;

public interface ShippingClient {

  void createShipping(Order order);

}
