/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.business;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="CartItem")
public class CartItem implements Serializable{
  private Long cartitemid;
  private Product product;
  private Long quantity;

  public CartItem() {
    
  }

  public CartItem(Long cartitemid, Product product, Long quantity) {
    this.cartitemid = cartitemid;
    this.product = product;
    this.quantity = quantity;
  }

  @XmlElement
  public Long getCartitemid() {
    return cartitemid;
  }

  public void setCartitem(Long cartitemid) {
    this.cartitemid = cartitemid;
  }

  @XmlTransient
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @XmlElement
  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }
}
