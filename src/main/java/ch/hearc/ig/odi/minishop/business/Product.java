/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Product")
public class Product {

  private Long productid;
  private String productname;
  private String description;
  private String category;
  private BigDecimal price;
  @JsonProperty("status")
  private String productstatus = Productstatus.ACTIVE.toString();

  public enum Productstatus {ACTIVE("active"),INACTIVE("inactive");
    private String productstatusname;
    Productstatus(String productstatusname){
      this.productstatusname = productstatusname;
    }
    public String toString() { return super.toString().toLowerCase();}
  }

  public Product() {
  }

  public Product(Long productid, String productname, String description, String category, BigDecimal price) {
    this.productid = productid;
    this.productname = productname;
    this.description = description;
    this.category = category;
    this.price = price;
    this.productstatus = Productstatus.ACTIVE.toString();
  }

  public Long getProductid() {
    return productid;
  }

  public void setProductid(Long productid) {
    this.productid = productid;
  }

  public String getProductname() {
    return productname;
  }

  public void setProductname(String productname) {
    this.productname = productname;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getProductstatus() {
    return productstatus;
  }

  public void setProductstatus(String productstatus) {
    this.productstatus = productstatus;
  }

}
