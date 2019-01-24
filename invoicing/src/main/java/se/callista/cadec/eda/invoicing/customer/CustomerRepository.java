package se.callista.cadec.eda.invoicing.customer;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

  Customer findByEmail(String email);

}
