package se.callista.cadec.eda.shipping.customer;

import se.callista.cadec.eda.customer.domain.Customer;

public interface CustomerRepository {

  Customer findById(String id);
  
  Customer findByEmail(String email);
  
  Customer save(Customer customer);
  
  void delete(Customer customer);

}
