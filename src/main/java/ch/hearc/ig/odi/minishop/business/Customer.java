/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
@XmlRootElement(name = "Customer")
public class Customer implements Serializable {

  private Long customerId;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Set<Cart> carts;
  private List<Order> orders;


  public Customer() {
    this.orders = new ArrayList<>();
    this.carts = new HashSet<>();
  }

  public Customer(String username, String firstName, String lastName, String email,
      String phone) {
    this();
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  public Customer(Long customerId, String username, String firstName, String lastName, String email,
      String phone) {
    this(username, firstName, lastName, email, phone);
    this.customerId = customerId;

  }

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @JsonIgnore
  @OneToMany(cascade= CascadeType.MERGE)
  public Set<Cart> getCarts() {
    return carts;
  }

  public void setCarts(Set<Cart> carts) {
    this.carts = carts;
  }

  @JsonIgnore
  @Transient
  public List getOrders() {
    return orders;
  }


  public void setOrders(List orders) {
    this.orders = orders;
  }

  /**
   * Add a cart to list of carts
   * @param cart : new cart to add to carts
   */
  public void addCart(Cart cart){
    this.carts.add(cart);
    // cart.setCustomer(this);
  }

  /**
   * Add an order to list of orders
   * @param order : new order to add to orders
   */
  public void addOrder(Order order){
    this.orders.add(order);
  }


  public void update(Customer newCustomer) {
    setEmail(newCustomer.getEmail());
    setFirstName(newCustomer.getFirstName());
    setLastName(newCustomer.getLastName());
    setPhone(newCustomer.getPhone());
    setUsername(newCustomer.getUsername());
  }
}

