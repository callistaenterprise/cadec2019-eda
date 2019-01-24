package se.callista.cadec.eda.invoicing.service;

import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;

public interface InvoiceService {

  void createInvoice(Order order, Customer customer);

}
