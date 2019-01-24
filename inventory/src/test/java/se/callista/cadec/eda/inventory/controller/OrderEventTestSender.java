package se.callista.cadec.eda.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class OrderEventTestSender {

  @Value("${kafka.topic.orders}")
  private String ordersTopic;

  @Autowired
  private KafkaTemplate<String, Order> kafkaTemplate;

  public void send(Order order) {
    kafkaTemplate.send(ordersTopic, order.getId(), order);
  }

}
