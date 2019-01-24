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
import se.callista.cadec.eda.order.domain.Order;
import se.callista.cadec.eda.order.integration.InvoicingClient;
import se.callista.cadec.eda.order.integration.ShippingClient;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InvoicingClient invoicing;

  @MockBean
  private ShippingClient shipping;

  @Test
  public void testPlaceOrder() throws Exception {
    ArgumentCaptor<Order> invoicingOrder = ArgumentCaptor.forClass(Order.class);
    doNothing().when(invoicing).createInvoice(invoicingOrder.capture());
    ArgumentCaptor<Order> shippingOrder = ArgumentCaptor.forClass(Order.class);
    doNothing().when(shipping).createShipping(shippingOrder.capture());
    MvcResult result = mockMvc.perform(post("/order").contentType("application/json").content(
        "{\"customer\":\"bb@callistaenterprise.se\", \"content\":\"Event Driven Architecture\"}"))
        .andExpect(status().isOk()).andReturn();
    Order order = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);
    assertTrue(invoicingOrder.getValue().isIdenticalTo(order));
    assertTrue(shippingOrder.getValue().isIdenticalTo(order));
  }

}
