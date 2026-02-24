package com.roamroute.backend.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trips")
public class Trip {

	@Id
	private int id;

	private String title;

	private String trip_description;

	private Date start_date;
	
	private Date end_date;

	private String keywords;

	@ManyToOne
	@JoinColumn(name = "destination_id")
	private Destination destination;

	@ManyToOne
	@JoinColumn(name = "flight_id")
	private Flight flight;

	@ManyToOne
	@JoinColumn(name = "accommodation_id")
	private Accommodation accommodation;

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

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Accommodation getAccomodation() {
		return accommodation;
	}

	public void setAccomodation(Accommodation accomodation) {
		this.accommodation = accomodation;
	}

}
