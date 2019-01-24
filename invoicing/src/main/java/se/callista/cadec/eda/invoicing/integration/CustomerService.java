package se.callista.cadec.eda.invoicing.integration;

import se.callista.cadec.eda.customer.domain.Customer;

public interface CustomerService {

  Customer getCustomerById(String id);

  Customer getCustomerByEmail(String email);

}
