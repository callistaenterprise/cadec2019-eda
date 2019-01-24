package se.callista.cadec.eda.customer.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CustomerTest {

  @Test
  public void ordersWithSameIdAreEqual() {
    Customer customer1 = new Customer("id", "firstName1", "lastName1", "street1", "zip1", "city1", "email1");
    Customer customer2 = new Customer("id", "firstName2", "lastName2", "street2", "zip2", "city2", "email2");
    assertTrue(customer1.equals(customer2));
  }

  @Test
  public void ordersWithDifferentIdAreNotEqual() {
    Customer customer1 = new Customer("id1", "firstName1", "lastName1", "street1", "zip1", "city1", "email1");
    Customer customer2 = new Customer("id2", "firstName1", "lastName2", "street2", "zip2", "city2", "email2");
    assertFalse(customer1.equals(customer2));
  }

  @Test
  public void ordersWithSamePropertiesAreIdentical() {
    Customer customer1 = new Customer("id", "firstName", "lastName", "street", "zip", "city", "email");
    Customer customer2 = new Customer("id", "firstName", "lastName", "street", "zip", "city", "email");
    assertTrue(customer1.isIdenticalTo(customer2));
  }

  @Test
  public void ordersWithDifferentPropertiesAreNotIdentical() {
    Customer customer1 = new Customer("id", "firstName1", "lastName", "street", "zip", "city", "email");
    Customer customer2 = new Customer("id", "firstName2", "lastName", "street", "zip", "city", "email");
    assertFalse(customer1.isIdenticalTo(customer2));
  }


}
