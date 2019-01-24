package se.callista.cadec.eda.invoicing.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.callista.cadec.eda.customer.domain.Customer;

@Component
public class CustomerClientImpl implements CustomerClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClientImpl.class);

  @Value("${integration.customer}")
  private String customer;
  
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public Customer getCustomerByEmail(String email) {
    LOGGER.info("Fetching Customer information for {}", email);
    return restTemplate.getForObject(customer + "/search/findByEmail?email="+email, Customer.class);
  }


}
