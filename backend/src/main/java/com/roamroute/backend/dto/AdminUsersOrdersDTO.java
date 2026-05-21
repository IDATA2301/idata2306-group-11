package com.roamroute.backend.dto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * DTO representing a user's order for admin inspection.
 *
 * <p>Contains order summary data including trip, flight and accommodation details,
 * pricing, dates and status. Used when an admin views a specific user's orders.
 */
public class AdminUsersOrdersDTO {
  private int id;
  private String title;
  private String airline;
  private String hotelName;
  private BigDecimal totalPrice;
  private Date startDate;
  private Date endDate;
  private String status;
  private Integer tripId;
  private Integer flightId;
  private Integer accommodationId;

  public AdminUsersOrdersDTO(int id, String title, String airline, String hotelName,
      BigDecimal totalPrice, Date startDate, Date endDate, String status,
      Integer tripId, Integer flightId, Integer accommodationId) {
    this.id = id;
    this.title = title;
    this.airline = airline;
    this.hotelName = hotelName;
    this.totalPrice = totalPrice;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
    this.tripId = tripId;
    this.flightId = flightId;
    this.accommodationId = accommodationId;
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public String getAirline() { return airline; }
  public String getHotelName() { return hotelName; }
  public BigDecimal getTotalPrice() { return totalPrice; }
  public Date getStartDate() { return startDate; }
  public Date getEndDate() { return endDate; }
  public String getStatus() { return status; }
  public Integer getTripId() { return tripId; }
  public Integer getFlightId() { return flightId; }
  public Integer getAccommodationId() { return accommodationId; }
}
