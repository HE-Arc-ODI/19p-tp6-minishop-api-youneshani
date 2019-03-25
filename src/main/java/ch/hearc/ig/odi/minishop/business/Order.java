/*
 * Copyright (c) 2018. Cours Outils de développement intégré, HEG Arc.
 */

package ch.hearc.ig.odi.minishop.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

  private Long orderid;
  private Date orderdate;
  private List<OrderLine> content;
  @JsonProperty("orderstatus")
  private String orderstatus = Orderstatus.OPEN.toString();

  public enum Orderstatus {OPEN ("open"), CONFIRMED ("confirmed"), PAID ("paid"), SHIPPED ("shipped"), CANCELED ("canceled");
  private String orderstatusname;
    //Constructeur
    Orderstatus(String statusname){
      this.orderstatusname = statusname;
    }
    public String toString() { return super.toString().toLowerCase();}
  }

  public Order() {
    this.content = new ArrayList<>();
    this.orderstatus = Orderstatus.OPEN.toString();
  }


  public Order(Long orderid, Date orderdate) {
    this.orderid = orderid;
    this.orderdate = orderdate;
    this.content = new ArrayList<>();
    this.orderstatus = Orderstatus.OPEN.toString();
  }

  public Long getOrderid() {
    return orderid;
  }

  public void setOrderid(Long orderid) {
    this.orderid = orderid;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
  public Date getOrderdate() {
    return orderdate;
  }

  public void setOrderdate(Date orderdate) {
    this.orderdate = orderdate;
  }

  public List<OrderLine> getContent() {
    return content;
  }

  public void setContent(List<OrderLine> content) {
    this.content = content;
  }


  public String getOrderstatus() {
    return orderstatus;
  }

  public void setOrderstatus(String orderstatus) {
    this.orderstatus = orderstatus;
  }

  /**
   * Add a new orderline
   * @param orderline : new orderline to add to content
   */
  public void addOrderLine(OrderLine orderline) {
    this.content.add(orderline);
  }
}
