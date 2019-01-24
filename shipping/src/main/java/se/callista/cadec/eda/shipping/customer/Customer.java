package se.callista.cadec.eda.shipping.customer;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

  @Id
  private String id;
  private String firstname;
  private String lastname;
  private String street;
  private String zip;
  private String city;
  private String email;

  protected Customer() {}

  public Customer(String id, String firstname, String lastname, String street, String zip,
      String city, String email) {
    super();
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.street = street;
    this.zip = zip;
    this.city = city;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
    Customer other = (Customer) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Customer [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname
        + ", street=" + street + ", zip=" + zip + ", city=" + city + ", email=" + email + "]";
  }

}
