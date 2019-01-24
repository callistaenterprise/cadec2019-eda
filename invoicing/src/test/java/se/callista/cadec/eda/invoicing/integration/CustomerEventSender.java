package se.callista.cadec.eda.invoicing.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;

@Component
public class CustomerEventSender {

  @Value("${kafka.topic.customers}")
  private String customersTopic;

  @Autowired
  private KafkaTemplate<String, Customer> kafkaTemplate;

  public void send(String id, Customer customer) {
    kafkaTemplate.send(customersTopic, id, customer);
  }

}
