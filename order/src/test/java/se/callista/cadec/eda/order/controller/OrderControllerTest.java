package se.callista.cadec.eda.order.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.callista.cadec.eda.order.controller.OrderController;
import se.callista.cadec.eda.order.controller.OrderEventSender;
import se.callista.cadec.eda.order.domain.Order;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderEventSender orderEventSender;

  @Test
  public void testPlaceOrder() throws Exception {
    ArgumentCaptor<Order> orderNotification = ArgumentCaptor.forClass(Order.class);
    doNothing().when(orderEventSender).send(orderNotification.capture());
    MvcResult result = mockMvc.perform(post("/order").contentType("application/json").content(
        "{\"customer\":\"bb@callistaenterprise.se\", \"content\":\"Event Driven Architecture\"}"))
        .andExpect(status().isOk()).andReturn();
    Order order = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);
    assertTrue(orderNotification.getValue().isIdenticalTo(order));
  }

}
