package se.callista.cadec.eda.inventory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class OrderEventSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventSender.class);

  @Value("${kafka.topic.orders}")
  private String ordersTopic;

  @Autowired
  private KafkaTemplate<String, Order> kafkaTemplate;

  public void send(Order order) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("sending payload='{}' to topic='{}'", order, ordersTopic);
    }
    kafkaTemplate.send(ordersTopic, order.getId(), order);
  }

}
