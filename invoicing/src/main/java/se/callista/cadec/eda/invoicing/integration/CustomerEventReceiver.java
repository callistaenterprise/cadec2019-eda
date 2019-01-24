package se.callista.cadec.eda.invoicing.integration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.customer.domain.EventType;
import se.callista.cadec.eda.invoicing.customer.CustomerRepository;

@Component
public class CustomerEventReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerEventReceiver.class);

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerService customerService;

  @KafkaListener(topics = "${kafka.topic.customers}", containerFactory = "customerListenerContainerFactory")
  public void receive(ConsumerRecord<String, String> record) {
    String id = record.key();
    EventType eventType = EventType.valueOf(record.value());
    LOGGER.info("received customer event '{}' for id '{}'", eventType, id);
    switch(eventType) {
      case CREATED:
      case UPDATED:
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
          customerRepository.save(modelMapper.map(customer, se.callista.cadec.eda.invoicing.customer.Customer.class));
          LOGGER.info("Cached customer {}", customer);
        }
        break;
      case DELETED:
        customerRepository.deleteById(id);
        LOGGER.info("Removed customer '{}' from cache", id);
        break;
    }
  }

}
