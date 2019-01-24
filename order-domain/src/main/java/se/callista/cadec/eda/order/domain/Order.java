package se.callista.cadec.eda.order.domain;

import java.io.Serializable;

public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String CREATED = "Created";
  public static final String VALIDATED = "Validated";
  
  private String id;

  private String customer;
  private String content;
  private String state;
  
  public Order() {}
  
  public Order(String id, String customer, String content, String state) {
    super();
    this.id = id;
    this.customer = customer;
    this.content = content;
    this.state = state;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getCustomer() {
    return customer;
  }
  public void setCustomer(String customer) {
    this.customer = customer;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "Order [id=" + id + ", customer=" + customer + ", content=" + content + ", state=" + state + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Order other = (Order) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  /**
   * Comparison based on all properties, not just id.
   * @param obj
   * @return
   */
  public boolean isIdenticalTo(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Order other = (Order) obj;
    if (state == null) {
      if (other.state != null)
        return false;
    } else if (!state.equals(other.state))
      return false;
    if (content == null) {
      if (other.content != null)
        return false;
    } else if (!content.equals(other.content))
      return false;
    if (customer == null) {
      if (other.customer != null)
        return false;
    } else if (!customer.equals(other.customer))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
}
