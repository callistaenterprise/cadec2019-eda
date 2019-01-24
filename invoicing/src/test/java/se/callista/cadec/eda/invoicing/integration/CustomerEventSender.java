package se.callista.cadec.eda.invoicing.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.EventType;

@Component
public class CustomerEventSender {

  @Value("${kafka.topic.customers}")
  private String customersTopic;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void send(String id, EventType eventType) {
    kafkaTemplate.send(customersTopic, id, eventType.toString());
  }

}
