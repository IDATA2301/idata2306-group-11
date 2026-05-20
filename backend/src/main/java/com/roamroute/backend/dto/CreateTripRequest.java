package com.roamroute.backend.dto;

import java.util.List;

public class CreateTripRequest {

  private String title;
  private String description;
  private String imageUrl;
  private String startDate;
  private String endDate;
  private List<String> keywords;
  private Integer destinationId;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public Integer getDestinationId() {
    return destinationId;
  }

  public void setDestinationId(Integer destinationId) {
    this.destinationId = destinationId;
  }
}
