package com.roamroute.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "selectedpackage_id")
  private SelectedPackage selectedPackage;

  private BigDecimal priceAtPurchase;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public SelectedPackage getSelectedPackage() {
    return selectedPackage;
  }

  public void setSelectedPackage(SelectedPackage selectedPackage) {
    this.selectedPackage = selectedPackage;
  }

  public BigDecimal getPriceAtPurchase() {
    return priceAtPurchase;
  }

  public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
    this.priceAtPurchase = priceAtPurchase;
  }
}
