package com.roamroute.backend.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class AdminUsersOrdersDTO {
  private int id;
  private String title;
  private String airline;
  private String hotelName;
  private BigDecimal totalPrice;
  private Date startDate;
  private Date endDate;
  private String status;

  public AdminUsersOrdersDTO(int id, String title, String airline, String hotelName, BigDecimal totalPrice, Date startDate, Date endDate, String status) {
    this.id = id;
    this.title = title;
    this.airline = airline;
    this.hotelName = hotelName;
    this.totalPrice = totalPrice;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAirline() {
    return airline;
  }

  public String getHotelName() {
    return hotelName;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public String getStatus() {
    return status;
  }
  
  
}