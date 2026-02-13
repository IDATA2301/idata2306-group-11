-- Create database
CREATE DATABASE IF NOT EXISTS idata2306_travel;
USE idata2306_travel;

-- Users table
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255),
  email VARCHAR(255),
  user_password VARCHAR(255),
  user_role VARCHAR(50)
);

-- Destinations table
CREATE TABLE destinations (
  id INT PRIMARY KEY AUTO_INCREMENT,
  city VARCHAR(255),
  country VARCHAR(255),
  image_url VARCHAR(500),
  destination_description TEXT
);

-- Flights table
CREATE TABLE flights (
  id INT PRIMARY KEY AUTO_INCREMENT,
  airline VARCHAR(255),
  departure_city VARCHAR(255),
  destination_city VARCHAR(255),
  departure_airport VARCHAR(100),
  destination_airport VARCHAR(100),
  flight_duration VARCHAR(50)
);

-- Accommodations table
CREATE TABLE accommodations (
  id INT PRIMARY KEY AUTO_INCREMENT,
  hotel_name VARCHAR(255),
  hotel_type VARCHAR(100),
  hotel_city VARCHAR(255),
  hotel_location VARCHAR(255),
  amenities TEXT
);

-- Trips table
CREATE TABLE trips (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255),
  trip_description TEXT,
  start_date DATE,
  end_date DATE,
  duration_days INT,
  keywords TEXT,
  destination_id INT,
  flight_id INT,
  accommodation_id INT,
  FOREIGN KEY (destination_id) REFERENCES Destinations(id),
  FOREIGN KEY (flight_id) REFERENCES Flights(id),
  FOREIGN KEY (accommodation_id) REFERENCES Accommodations(id)
);

-- TripPrices table
CREATE TABLE tripprices (
  id INT PRIMARY KEY AUTO_INCREMENT,
  trip_id INT,
  tripprice_provider VARCHAR(255),
  price DECIMAL(10, 2),
  tripprice_type VARCHAR(50),
  FOREIGN KEY (trip_id) REFERENCES Trips(id)
);

-- Favorites table
CREATE TABLE favorites (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  trip_id INT,
  FOREIGN KEY (user_id) REFERENCES Users(id),
  FOREIGN KEY (trip_id) REFERENCES Trips(id)
);

-- ContactMessages table
CREATE TABLE contactmessages (
  id INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255),
  contactmessage_subject VARCHAR(255),
  contactmessage_message TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
