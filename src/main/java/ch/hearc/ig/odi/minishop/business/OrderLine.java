/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.business;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "OrderLine")
public class OrderLine implements Serializable {

  private Long orderlinepostion;
  private Product product;
  private Long quantity;

  public OrderLine() {
  }

  public OrderLine(Long orderlinepostion, Product product, Long quantity) {
    this.orderlinepostion = orderlinepostion;
    this.product = product;
    this.quantity = quantity;
  }

  @XmlElement
  public Long getOrderlinepostion() {
    return orderlinepostion;
  }

  public void setOrderlinepostion(Long orderlinepostion) {
    this.orderlinepostion = orderlinepostion;
  }

  @XmlTransient
  public Product getProduct() {
    return product;
  }

  @XmlTransient
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
