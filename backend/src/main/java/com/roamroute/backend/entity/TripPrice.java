package com.roamroute.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tripprices")
public class TripPrice {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  int id;

  String tripprice_provider;

  String price;

  String tripprice_type;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTripprice_provider() {
    return tripprice_provider;
  }

  public void setTripprice_provider(String tripprice_provider) {
    this.tripprice_provider = tripprice_provider;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getTripprice_type() {
    return tripprice_type;
  }

  public void setTripprice_type(String tripprice_type) {
    this.tripprice_type = tripprice_type;
  }
}
