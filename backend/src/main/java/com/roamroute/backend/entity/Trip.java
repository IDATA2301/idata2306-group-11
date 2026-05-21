package com.roamroute.backend.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trips")
/**
 * JPA entity representing a travel trip with destination, dates, description, and searchable keywords.
 */
public class Trip {

	@Id
	private int id;

	private String title;

	private String trip_description;

	private Date start_date;
	
	private Date end_date;

	private String keywords;

	@OneToOne
	@JoinColumn(name = "destination_id")
	private Destination destination;

	private String image_url;

	@Column(nullable = false)
	private boolean active = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTrip_description() {
		return trip_description;
	}

	public void setTrip_description(String trip_description) {
		this.trip_description = trip_description;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public boolean isActive() {
		return active;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
