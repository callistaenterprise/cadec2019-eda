package se.callista.cadec.eda.customer.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.callista.cadec.eda.customer.conf.KafkaConfig;
import se.callista.cadec.eda.customer.domain.Customer;
import se.callista.cadec.eda.customer.integration.CustomerEventSender;
import se.callista.cadec.eda.customer.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@ComponentScan(basePackages = "se.callista.cadec.eda.customer",
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, value = {KafkaConfig.class})})
public class CustomerControllerTest {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CustomerRepository customerRepository;

  @MockBean
  private CustomerEventSender customerEventSender;

  private Customer customer;
  private se.callista.cadec.eda.customer.repository.Customer persistentCustomer;
  
  @Before
  public void setup() {
    customer = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "bb@callistaenterprise.se");
    persistentCustomer = modelMapper.map(customer, se.callista.cadec.eda.customer.repository.Customer.class);
  }

  @Test
  public void testGetCustomers() throws Exception {
    List<se.callista.cadec.eda.customer.repository.Customer> customers = new LinkedList<>();
    customers.add(persistentCustomer);
    when(customerRepository.findAll()).thenReturn(customers);
    MvcResult result = mockMvc.perform(get("/customer"))
        .andExpect(status().isOk()).andReturn();
    Customer[] customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Customer[].class);
    assertTrue(customerResponse[0].equals(customer));
  }

  @Test
  public void testGetCustomerById() throws Exception {
    when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(persistentCustomer));
    MvcResult result = mockMvc.perform(get("/customer/"+customer.getId()))
        .andExpect(status().isOk()).andReturn();
    Customer customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
    assertTrue(customerResponse.equals(customer));
  }

  @Test
  public void testGetCustomerByEmail() throws Exception {
    when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(persistentCustomer));
    MvcResult result = mockMvc.perform(get("/customer/search/findByEmail?email="+customer.getEmail()))
        .andExpect(status().isOk()).andReturn();
    Customer customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
    assertTrue(customerResponse.equals(customer));
  }

  @Test
  public void testCreateCustomer() throws Exception {
    MvcResult result = mockMvc.perform(post("/customer").contentType("application/json").content(
        "{\"id\":\"new\",\"email\":\"new@email\"}"))
        .andExpect(status().isOk()).andReturn();
    Customer customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
    verify(customerEventSender, times(1)).send(customerResponse.getId(), customerResponse);
    verifyZeroInteractions(customerRepository);
    assertTrue(customerResponse.getEmail().equals("new@email"));
  }

  @Test
  public void testUpdateCustomer() throws Exception {
    when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(persistentCustomer));
    MvcResult result = mockMvc.perform(put("/customer/"+customer.getId()).contentType("application/json").content(
        "{\"email\":\"updated@email\"}"))
        .andExpect(status().isOk()).andReturn();
    Customer customerResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
    verify(customerEventSender, times(1)).send(customerResponse.getId(), customerResponse);
    verifyZeroInteractions(customerRepository);
    assertTrue(customerResponse.getEmail().equals("updated@email"));
  }

  @Test
  public void testDeleteCustomer() throws Exception {
    when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(persistentCustomer));
    mockMvc.perform(delete("/customer/"+customer.getId()))
        .andExpect(status().isOk());
    verify(customerEventSender, times(1)).send(customer.getId(), null);
    verifyZeroInteractions(customerRepository);
  }

}
