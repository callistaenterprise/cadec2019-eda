package se.callista.cadec.eda.order.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.callista.cadec.eda.order.domain.Order;

@Component
public class InvoicingClientImpl implements InvoicingClient {

  @Value("${integration.invoicing}")
  private String invoicing;
  
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void createInvoice(Order order) {
    restTemplate.postForEntity(invoicing, order, Void.class);
  }

}
