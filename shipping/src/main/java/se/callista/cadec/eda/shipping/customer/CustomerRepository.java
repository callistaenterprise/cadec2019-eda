package se.callista.cadec.eda.shipping.customer;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

  Customer findByEmail(String email);

}
