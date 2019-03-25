/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
@XmlRootElement(name = "Cart")
public class Cart {
  private Long cartid;
  private String cartstatus = Cartstatus.OPEN.toString();
  private ArrayList<CartItem> content;
  @JsonIgnore
  private Customer customer;

  public boolean removeCartItemById(Long cartitemid) {
    return true;
  }


  public enum Cartstatus{OPEN("open"),CHECK_OUT("check_out");
    private String cartstatusname;
      Cartstatus(String statusname){
        this.cartstatusname = statusname;
      }
      public String toString() { return super.toString().toLowerCase();}
  }

  public Cart() {
      this.content = new ArrayList<>();
  }

  public Cart(Long cartid) {
    this.cartid = cartid;
    this.content = new ArrayList<>();
  }

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  @XmlElement
  public Long getCartid() {
    return cartid;
  }

  public void setCartid(Long cartid) {
    this.cartid = cartid;
  }

  @XmlElement
  public String getCartstatus() {
    return cartstatus;
  }

  public void setCartstatus(String cartstatus) {
    this.cartstatus = cartstatus;
  }

  @XmlElement
  @Transient
  public ArrayList<CartItem> getContent() {
    return content;
  }

  public void setContent(ArrayList<CartItem> content) {
    this.content = content;
  }

  /**
   * Add a cartitem to the list of cartitems
   * @param cartitem : new cartitem to add to content
   */
  public void addCartItem(CartItem cartitem){
    this.content.add(cartitem);
  }

  /**
   * Remove a cartitem from the list of cartitems
   * @param cartitem : cartitem to remove from content
   */
  public void removeCartItem(CartItem cartitem){
    this.content.remove(cartitem);
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
    customer.addCart(this);
  }

  @ManyToOne
  public Customer getCustomer() {
    return customer;
  }

}
