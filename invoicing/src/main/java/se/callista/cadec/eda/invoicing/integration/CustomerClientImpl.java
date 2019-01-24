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

  @Autowired
  private CustomerService customerService;

  @Override
  public Customer getCustomerByEmail(String email) {
    se.callista.cadec.eda.invoicing.customer.Customer cachedCustomer = customerRepository.findByEmail(email);
    if (cachedCustomer != null) {
      return modelMapper.map(cachedCustomer, Customer.class);
    } else {
      Customer customer = customerService.getCustomerByEmail(email);
      if (customer != null) {
        customerRepository.save(modelMapper.map(customer, se.callista.cadec.eda.invoicing.customer.Customer.class));
      }
      return customer;
    }
  }


}
