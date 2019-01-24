package se.callista.cadec.eda.order.domain;

import static org.junit.Assert.*;
import org.junit.Test;

public class OrderTest {
  
  @Test
  public void ordersWithSameIdAreEqual() {
    Order order1 = new Order("id", "customer1", "content1", Order.CREATED);
    Order order2 = new Order("id", "customer2", "content2", Order.CREATED);
    assertTrue(order1.equals(order2));
  }

  @Test
  public void ordersWithDifferentIdAreNotEqual() {
    Order order1 = new Order("id1", "customer1", "content1", Order.CREATED);
    Order order2 = new Order("id2", "customer2", "content2", Order.CREATED);
    assertFalse(order1.equals(order2));
  }

  @Test
  public void ordersWithSamePropertiesAreIdentical() {
    Order order1 = new Order("id", "customer", "content", Order.CREATED);
    Order order2 = new Order("id", "customer", "content", Order.CREATED);
    assertTrue(order1.isIdenticalTo(order2));
  }

  @Test
  public void ordersWithDifferentPropertiesAreNotIdentical() {
    Order order1 = new Order("id", "customer1", "content", Order.CREATED);
    Order order2 = new Order("id", "customer2", "content", Order.CREATED);
    assertFalse(order1.isIdenticalTo(order2));
  }

}
