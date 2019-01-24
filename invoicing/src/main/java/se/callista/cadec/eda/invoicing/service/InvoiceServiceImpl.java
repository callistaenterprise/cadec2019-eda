package se.callista.cadec.eda.invoicing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;

@Service
public class InvoiceServiceImpl implements InvoiceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceImpl.class);

  @Override
  public void createInvoice(Order order, Customer customer) {
    LOGGER.info("Invoice for order '{}' placed by '{} {}' sent to '{}, {} {}'", order.getId(),
        customer.getFirstname(), customer.getLastname(), customer.getStreet(), customer.getZip(), customer.getCity());
  }

}
