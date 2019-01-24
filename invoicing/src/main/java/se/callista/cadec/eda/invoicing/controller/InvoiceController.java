package se.callista.cadec.eda.invoicing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.invoicing.integration.CustomerClient;
import se.callista.cadec.eda.invoicing.service.InvoiceService;
import se.callista.cadec.eda.order.domain.Order;

@RestController
public class InvoiceController {

  @Autowired
  private CustomerClient customerClient;

  @Autowired
  private InvoiceService invoiceService;
  
  @PostMapping(value = "/invoice")
  public void createInvoice(@RequestBody Order order) {
    String email = order.getCustomer();
    Customer customer = customerClient.getCustomerByEmail(email);
    invoiceService.createInvoice(order, customer);
  }

}
