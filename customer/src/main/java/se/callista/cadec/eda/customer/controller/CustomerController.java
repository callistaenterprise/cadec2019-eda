package se.callista.cadec.eda.customer.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.customer.integration.CustomerEventSender;
import se.callista.cadec.eda.customer.repository.CustomerRepository;

@RestController
public class CustomerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerEventSender customerEventSender;

  @GetMapping(value = "/customer")
  public Iterable<Customer> getCustomers() {
    Iterable<se.callista.cadec.eda.customer.repository.Customer> customers = customerRepository.findAll();
    List<Customer> result = new LinkedList<>();
    for (se.callista.cadec.eda.customer.repository.Customer customer : customers) {
      result.add(modelMapper.map(customer, Customer.class));
    }
    return result;
  }

  @GetMapping(value = "/customer/{id}")
  public Optional<Customer> getCustomer(@PathVariable String id) {
    return Optional.ofNullable(modelMapper.map(customerRepository.findById(id).get(), Customer.class));
  }

  @GetMapping(value = "/customer/search/findByEmail")
  public Optional<Customer> getCustomerByEmail(@RequestParam String email) {
    return Optional.ofNullable(modelMapper.map(customerRepository.findByEmail(email).get(), Customer.class));
  }

  @PostMapping(value = "/customer")
  public Customer createCustomer(@RequestBody Customer customer) {
    customer.setId(UUID.randomUUID().toString());
    customerEventSender.send(customer.getId(), customer);
    LOGGER.info("Customer '{}' created", customer);
    return customer;
  }

  @PutMapping(value = "/customer/{id}")
  public Customer updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
    customer.setId(id);
    customerEventSender.send(customer.getId(), customer);
    LOGGER.info("Customer '{}' updated", customer);
    return customer;
  }

  @DeleteMapping(value = "/customer/{id}")
  public void deleteCustomerById(@PathVariable String id) {
    customerEventSender.send(id, null);
    LOGGER.info("Customer '{}' deleted", id);
  }

}
