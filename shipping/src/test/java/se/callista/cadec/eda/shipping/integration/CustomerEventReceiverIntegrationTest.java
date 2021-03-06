package se.callista.cadec.eda.shipping.integration;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.shipping.customer.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "se.callista.cadec.eda.shipping")
@EnableKafka
public class CustomerEventReceiverIntegrationTest {

  @Autowired
  private CustomerEventSender customerEventSender;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @MockBean
  private CustomerRepository customerRepository;

  @ClassRule
  public static EmbeddedKafkaRule kafkaBroker = new EmbeddedKafkaRule(1, false, "customers", "orders");

  @BeforeClass
  public static void setUpBeforeClass() {
      System.setProperty("spring.kafka.bootstrap-servers", kafkaBroker.getEmbeddedKafka().getBrokersAsString());
  }

  @Before
  public void setUp() throws Exception {
    // wait until the partitions are assigned
    for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
        .getListenerContainers()) {
      ContainerTestUtils.waitForAssignment(messageListenerContainer,
          kafkaBroker.getEmbeddedKafka().getPartitionsPerTopic());
    }
  }

  @Test
  public void testCustomerCreatedOrUpdated() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    customerEventSender.send(customer.getId(), customer);
    verify(customerRepository, timeout(500).times(1)).save(eq(customer));
  }

  @Test
  public void testCustomerDeleted() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerRepository.findById("id")).thenReturn(customer);
    customerEventSender.send("id", null);
    verify(customerRepository, timeout(500).times(1)).delete(customer);
  }

}
