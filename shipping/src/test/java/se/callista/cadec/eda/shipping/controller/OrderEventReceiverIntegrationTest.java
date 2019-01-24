package se.callista.cadec.eda.shipping.controller;

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
import se.callista.cadec.eda.order.domain.Order;
import se.callista.cadec.eda.shipping.integration.CustomerClient;
import se.callista.cadec.eda.shipping.service.ShippingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "se.callista.cadec.eda.shipping")
@EnableKafka
public class OrderEventReceiverIntegrationTest {

  @Autowired
  private OrderEventSender orderEventSender;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @MockBean
  private CustomerClient customerClient;

  @MockBean
  private ShippingService shippingService;

  @ClassRule
  public static EmbeddedKafkaRule kafkaBroker = new EmbeddedKafkaRule(1, false, "orders");

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
  public void testEventCreated() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerClient.getCustomerByEmail("bb@callistaenterprise.se")).thenReturn(customer);
    Order order = new Order("order1", "bb@callistaenterprise.se", "Event Driven Architecture", Order.CREATED);
    orderEventSender.send(order);
    verify(shippingService, timeout(500).times(0)).createShipping(eq(order), eq(customer));
  }

  @Test
  public void testEventValidated() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerClient.getCustomerByEmail("bb@callistaenterprise.se")).thenReturn(customer);
    Order order = new Order("order1", "bb@callistaenterprise.se", "Event Driven Architecture", Order.VALIDATED);
    orderEventSender.send(order);
    verify(shippingService, timeout(500).times(1)).createShipping(eq(order), eq(customer));
  }

}
