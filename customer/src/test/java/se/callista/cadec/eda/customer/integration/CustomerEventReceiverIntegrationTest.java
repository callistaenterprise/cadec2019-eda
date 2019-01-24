package se.callista.cadec.eda.customer.integration;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.customer.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "se.callista.cadec.eda.customer")
@EnableKafka
@TestPropertySource(properties = {"spring.liquibase.enabled=false"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class CustomerEventReceiverIntegrationTest {

  @Autowired
  private ModelMapper modelMapper;

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
    se.callista.cadec.eda.customer.repository.Customer cachedCustomer = modelMapper.map(customer, se.callista.cadec.eda.customer.repository.Customer.class);
    customerEventSender.send(customer.getId(), customer);
    verify(customerRepository, timeout(500).times(1)).save(eq(cachedCustomer));
  }

  @Test
  public void testCustomerDeleted() throws Exception {
    customerEventSender.send("deleted", null);
    verify(customerRepository, timeout(500).times(1)).deleteById("deleted");
  }

}
