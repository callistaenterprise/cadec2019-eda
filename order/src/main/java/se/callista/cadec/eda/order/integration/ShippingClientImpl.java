package se.callista.cadec.eda.order.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class ShippingClientImpl implements ShippingClient {

  @Value("${integration.shipping}")
  private String shipping;
  
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void createShipping(Order order) {
    restTemplate.postForEntity(shipping, order, Void.class);
  }

}
