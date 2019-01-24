package se.callista.cadec.eda.inventory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class OrderEventReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventReceiver.class);

  @Autowired
  private OrderEventSender orderEventSender;
  
  @KafkaListener(topics = "${kafka.topic.orders}", containerFactory = "orderListenerContainerFactory")
  public void receive(Order order) {
    if (order.getState().equals(Order.CREATED)) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        // Never mind
      }
      order.setState(Order.VALIDATED);
      LOGGER.info("Order '{}' validated", order.getId());
      orderEventSender.send(order);
    }
  }

}

