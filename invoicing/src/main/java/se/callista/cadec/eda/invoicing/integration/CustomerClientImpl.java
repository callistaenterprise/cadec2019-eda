package se.callista.cadec.eda.invoicing.integration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.invoicing.customer.CustomerRepository;

@Component
public class CustomerClientImpl implements CustomerClient {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public Customer getCustomerByEmail(String email) {
    return modelMapper.map(customerRepository.findByEmail(email), Customer.class);
  }

}
