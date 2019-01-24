package se.callista.cadec.eda.invoicing.controller;

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
import se.callista.cadec.eda.invoicing.integration.CustomerClient;
import se.callista.cadec.eda.invoicing.service.InvoiceService;
import se.callista.cadec.eda.order.domain.Order;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CustomerClient customerClient;

  @MockBean
  private InvoiceService invoiceService;

  @Test
  public void testCreateInvoice() throws Exception {
    Customer customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerClient.getCustomerByEmail("bb@callistaenterprise.se")).thenReturn(customer);
    mockMvc.perform(post("/invoice").contentType("application/json").content(
        "{\"customer\":\"bb@callistaenterprise.se\", \"content\":\"Event Driven Architecture\"}"))
        .andExpect(status().isOk());
    verify(invoiceService, times(1)).createInvoice(any(Order.class), eq(customer));

  }

}
