package se.callista.cadec.eda.shipping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;
import se.callista.cadec.eda.shipping.integration.CustomerClient;
import se.callista.cadec.eda.shipping.service.ShippingService;

@RunWith(SpringRunner.class)
@WebMvcTest(ShippingController.class)
public class ShippingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CustomerClient customerClient;

  @MockBean
  private ShippingService shippingService;

  @Test
  public void testCreateInvoice() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerClient.getCustomerByEmail("bb@callistaenterprise.se")).thenReturn(customer);
    mockMvc.perform(post("/shipping").contentType("application/json").content(
        "{\"customer\":\"bb@callistaenterprise.se\", \"content\":\"Event Driven Architecture\"}"))
        .andExpect(status().isOk());
    verify(shippingService, times(1)).createShipping(any(Order.class), eq(customer));

  }

}
