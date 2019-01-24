package se.callista.cadec.eda.shipping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.order.domain.Order;

@Service
public class ShippingServiceImpl implements ShippingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShippingServiceImpl.class);

  @Override
  public void createShipping(Order order, Customer customer) {
    LOGGER.info("'{}' sent to '{} {}' at '{}, {} {}'", order.getContent(),
        customer.getFirstname(), customer.getLastname(), customer.getStreet(), customer.getZip(), customer.getCity());
  }

}
