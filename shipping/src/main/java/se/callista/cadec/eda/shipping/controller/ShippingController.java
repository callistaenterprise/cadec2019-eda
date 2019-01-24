package se.callista.cadec.eda.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;
import se.callista.cadec.eda.shipping.integration.CustomerClient;
import se.callista.cadec.eda.shipping.service.ShippingService;

@RestController
public class ShippingController {

  @Autowired
  private CustomerClient customerClient;

  @Autowired
  private ShippingService shippingService;

  @PostMapping(value = "/shipping")
  public void createShipping(@RequestBody Order order) {
    String email = order.getCustomer();
    Customer customer = customerClient.getCustomerByEmail(email);
    shippingService.createShipping(order, customer);
  }

}
