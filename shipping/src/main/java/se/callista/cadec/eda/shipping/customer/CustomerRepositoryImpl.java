package se.callista.cadec.eda.shipping.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import se.callista.cadec.eda.customer.domain.Customer;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {
  
  @Autowired
  private CacheManager cacheManager;
  
  @Override
  public Customer findById(String id) {
    Cache customersById = cacheManager.getCache("customerById");
    return (Customer) customersById.get(id).get();
  }

  @Override
  public Customer findByEmail(String email) {
    Cache customerByEmail = cacheManager.getCache("customerByEmail");
    return (Customer) customerByEmail.get(email).get();
  }

  public Customer save(Customer customer) {
    Cache customersById = cacheManager.getCache("customerById");
    customersById.put(customer.getId(), customer);
    Cache customerByEmail = cacheManager.getCache("customerByEmail");
    customerByEmail.put(customer.getEmail(), customer);
    return customer;
  }

  public void delete(Customer customer) {
    Cache customersById = cacheManager.getCache("customerById");
    customersById.evict(customer.getId());
    Cache customerByEmail = cacheManager.getCache("customerByEmail");
    customerByEmail.evict(customer.getEmail());
  }

}
