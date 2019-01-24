package se.callista.cadec.eda.shipping.integration;

import se.callista.cadec.eda.customer.domain.Customer;

public interface CustomerClient {

  Customer getCustomerByEmail(String email);

}
