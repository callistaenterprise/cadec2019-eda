package se.callista.cadec.eda.shipping.integration;

import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.shipping.customer.CustomerRepository;

@Component
public class CustomerEventReceiver implements ConsumerSeekAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerEventReceiver.class);

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @KafkaListener(topics = "${kafka.topic.customers}", containerFactory = "customerListenerContainerFactory")
  public void receive(ConsumerRecord<String, Customer> record) {
    String id = record.key();
    Customer customer = record.value();
    if (customer != null) {
      LOGGER.info("received customer update {}", customer);
      customerRepository.save(modelMapper.map(customer, se.callista.cadec.eda.shipping.customer.Customer.class));
    } else {
      LOGGER.info("received customer delete {}", id);
      customerRepository.deleteById(id);
    }
  }

  @Override
  public void onPartitionsAssigned(Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback) {
    for (TopicPartition topicPartition : assignments.keySet()) {
      callback.seekToBeginning(topicPartition.topic(), topicPartition.partition());
    }
  }

  @Override
  public void registerSeekCallback(ConsumerSeekCallback callback) {
    // NOOP
  }

  @Override
  public void onIdleContainer(Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback) {
    // NOOP
  }

}
