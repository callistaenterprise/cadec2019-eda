package se.callista.cadec.eda.invoicing.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.callista.cadec.eda.customer.domain.Customer;

@Component
public class CustomerServiceImpl implements CustomerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

  @Value("${integration.customer}")
  private String customerEndpoint;
  
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public Customer getCustomerById(String id) {
    LOGGER.info("Fetching Customer information for {}", id);
    return restTemplate.getForObject(customerEndpoint + "/" + id, Customer.class);
  }

  @Override
  public Customer getCustomerByEmail(String email) {
    LOGGER.info("Fetching Customer information for {}", email);
    return restTemplate.getForObject(customerEndpoint + "/search/findByEmail?email="+email, Customer.class);
  }

}
