package se.callista.cadec.eda.inventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
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
import se.callista.cadec.eda.order.domain.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "se.callista.cadec.eda.inventory")
@EnableKafka
public class OrderEventReceiverIntegrationTest {

  @Autowired
  private OrderEventTestSender orderEventTestSender;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @MockBean
  private OrderEventSender orderEventSender;

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
    Order createdOrder = new Order("order1", "bb@callistaenterprise.se", "Event Driven Architecture", Order.CREATED);
    Order validatedOrder = new Order("order1", "bb@callistaenterprise.se", "Event Driven Architecture", Order.VALIDATED);
    orderEventTestSender.send(createdOrder);
    verify(orderEventSender, timeout(1000).times(1)).send(eq(validatedOrder));
  }

  @Test
  public void testEventValidated() throws Exception {
    Order validatedOrder = new Order("order1", "bb@callistaenterprise.se", "Event Driven Architecture", Order.VALIDATED);
    orderEventTestSender.send(validatedOrder);
    verify(orderEventSender, timeout(1000).times(0)).send(any(Order.class));
  }

}
