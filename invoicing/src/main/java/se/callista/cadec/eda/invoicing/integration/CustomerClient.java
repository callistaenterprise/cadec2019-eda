package se.callista.cadec.eda.invoicing.integration;

import se.callista.cadec.eda.customer.domain.Customer;

public interface CustomerClient {

  Customer getCustomerByEmail(String email);

}
