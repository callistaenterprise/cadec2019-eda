package se.callista.cadec.eda.invoicing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.invoicing.integration.CustomerClient;
import se.callista.cadec.eda.invoicing.service.InvoiceService;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class OrderEventReceiver {

  @Autowired
  private CustomerClient customerClient;

  @Autowired
  private InvoiceService invoiceService;

  @KafkaListener(topics = "${kafka.topic.orders}", containerFactory = "orderListenerContainerFactory")
  public void receive(Order order) {
    if (order.getState().equals(Order.VALIDATED)) {
      String email = order.getCustomer();
      Customer customer = customerClient.getCustomerByEmail(email);
	    invoiceService.createInvoice(order, customer);
    }
  }

}
