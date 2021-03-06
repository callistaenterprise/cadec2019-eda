package se.callista.cadec.eda.shipping.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;
import se.callista.cadec.eda.shipping.integration.CustomerClient;
import se.callista.cadec.eda.shipping.service.ShippingService;

@Component
public class OrderEventReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventReceiver.class);

  @Autowired
  private CustomerClient customerClient;

  @Autowired
  private ShippingService shippingService;

  @KafkaListener(topics = "${kafka.topic.orders}", containerFactory = "orderListenerContainerFactory")
  public void receive(Order order) {
    LOGGER.info("received order {}", order);
    if (order.getState().equals(Order.VALIDATED)) {
      String email = order.getCustomer();
      Customer customer = customerClient.getCustomerByEmail(email);
	    shippingService.createShipping(order, customer);
    }
  }

}
