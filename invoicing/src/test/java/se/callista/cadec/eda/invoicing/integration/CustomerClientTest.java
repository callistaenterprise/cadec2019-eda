package se.callista.cadec.eda.invoicing.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.invoicing.customer.CustomerRepository;

public class CustomerClientTest {

  private ModelMapper modelMapper = new ModelMapper();

  private CustomerClient customerClient;

  private CustomerRepository customerRepository;

  private CustomerService customerService;

  @Before
  public void setup() {
    customerRepository = Mockito.mock(CustomerRepository.class);
    customerService = Mockito.mock(CustomerService.class);
    customerClient = new CustomerClientImpl();
    ReflectionTestUtils.setField(customerClient, "modelMapper", modelMapper);
    ReflectionTestUtils.setField(customerClient, "customerRepository", customerRepository);
    ReflectionTestUtils.setField(customerClient, "customerService", customerService);
  }

  @Test
  public void testCacheHit() {
    se.callista.cadec.eda.invoicing.customer.Customer cachedCustomer =
        new se.callista.cadec.eda.invoicing.customer.Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerRepository.findByEmail("bb@callistaenterprise.se")).thenReturn(cachedCustomer);
    Customer customer = customerClient.getCustomerByEmail("bb@callistaenterprise.se");
    assertEquals(modelMapper.map(cachedCustomer, Customer.class), customer);
    verifyZeroInteractions(customerService);
  }

  @Test
  public void testCacheMiss() {
    Customer customer =
        new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    when(customerRepository.findByEmail("bb@callistaenterprise.se")).thenReturn(null);
    when(customerService.getCustomerByEmail("bb@callistaenterprise.se")).thenReturn(customer);
    Customer response = customerClient.getCustomerByEmail("bb@callistaenterprise.se");
    assertEquals(customer, response);
    verify(customerRepository, times(1)).save(eq(modelMapper.map(customer, se.callista.cadec.eda.invoicing.customer.Customer.class)));
  }

  @Test
  public void testNonExisting() {
    when(customerRepository.findByEmail("bb@callistaenterprise.se")).thenReturn(null);
    when(customerService.getCustomerByEmail("bb@callistaenterprise.se")).thenReturn(null);
    Customer response = customerClient.getCustomerByEmail("bb@callistaenterprise.se");
    assertNull(response);
    verify(customerRepository, times(0)).save(any(se.callista.cadec.eda.invoicing.customer.Customer.class));
  }

}

