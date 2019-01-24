package se.callista.cadec.eda.invoicing.integration;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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
import se.callista.cadec.eda.customer.domain.EventType;
import se.callista.cadec.eda.invoicing.customer.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "se.callista.cadec.eda.invoicing")
@EnableKafka
public class CustomerEventReceiverIntegrationTest {

  
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CustomerEventSender customerEventSender;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @MockBean
  private CustomerRepository customerRepository;

  @MockBean
  private CustomerService customerService;

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
  public void testCustomerCreated() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    se.callista.cadec.eda.invoicing.customer.Customer cachedCustomer = modelMapper.map(customer, se.callista.cadec.eda.invoicing.customer.Customer.class);
    when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
    customerEventSender.send(customer.getId(), EventType.CREATED);
    verify(customerRepository, timeout(500).times(1)).save(eq(cachedCustomer));
  }

  @Test
  public void testCustomerUpdated() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    se.callista.cadec.eda.invoicing.customer.Customer cachedCustomer = modelMapper.map(customer, se.callista.cadec.eda.invoicing.customer.Customer.class);
    when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
    customerEventSender.send(customer.getId(), EventType.UPDATED);
    verify(customerRepository, timeout(500).times(1)).save(eq(cachedCustomer));
  }

  @Test
  public void testCustomerDeleted() throws Exception {
    customerEventSender.send("deleted", EventType.DELETED);
    verify(customerRepository, timeout(500).times(1)).deleteById("deleted");
  }

}
