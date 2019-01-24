package se.callista.cadec.eda.order.controller;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.callista.cadec.eda.order.domain.Order;

@RestController
public class OrderController {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private OrderEventSender orderEventSender;

  @PostMapping(value = "/order")
  public Order placeOrder(@RequestBody Order order) {
    order.setId(UUID.randomUUID().toString());
    orderEventSender.send(order);
    LOGGER.info("Order '{}' placed by '{}' created", order.getId(), order.getCustomer());
    return order;
  }

}
