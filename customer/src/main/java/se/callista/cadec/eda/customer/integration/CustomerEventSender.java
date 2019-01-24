package se.callista.cadec.eda.customer.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.EventType;

@Component
public class CustomerEventSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerEventSender.class);

  @Value("${kafka.topic.customers}")
  private String customersTopic;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void send(String id, EventType eventType) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info("sending key='{}' payload='{}' to topic='{}'", id, eventType, customersTopic);
    }
    kafkaTemplate.send(customersTopic, id, eventType.toString());
  }

}
