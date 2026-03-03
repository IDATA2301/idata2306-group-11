-- Create database
CREATE DATABASE IF NOT EXISTS roamroute_db;
USE roamroute_db;

-- Users table
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255),
  email VARCHAR(255),
  user_password VARCHAR(255),
  user_role VARCHAR(50),
  user_address VARCHAR(255),
  user_country VARCHAR(50)
);

-- Destinations table
CREATE TABLE destinations (
  id INT PRIMARY KEY AUTO_INCREMENT,
  city VARCHAR(255),
  country VARCHAR(255),
  image_url VARCHAR(500),
  image_alt TEXT
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
  amenities TEXT,
  nights INT
);

-- Trips table
CREATE TABLE trips (
  id INT PRIMARY KEY,
  title VARCHAR(255),
  trip_description TEXT,
  start_date DATE,
  end_date DATE,
  duration_days INT,
  keywords TEXT,
  destination_id INT,
  flight_id INT,
  accommodation_id INT,
  FOREIGN KEY (destination_id) REFERENCES destinations(id),
  FOREIGN KEY (flight_id) REFERENCES flights(id),
  FOREIGN KEY (accommodation_id) REFERENCES accommodations(id)
);

-- TripPrices table
CREATE TABLE tripprices (
  id INT PRIMARY KEY AUTO_INCREMENT,
  trip_id INT,
  tripprice_provider VARCHAR(255),
  price DECIMAL(10, 2),
  tripprice_type VARCHAR(50),
  FOREIGN KEY (trip_id) REFERENCES trips(id)
);

-- SelectedPackages table
CREATE TABLE selectedpackages (
  id INT PRIMARY KEY AUTO_INCREMENT,
  trip_id INT,
  flight_tripprice_id INT,
  hotel_tripprice_id INT,
  FOREIGN KEY (trip_id) REFERENCES trips(id),
  FOREIGN KEY (flight_tripprice_id) REFERENCES tripprices(id),
  FOREIGN KEY (hotel_tripprice_id) REFERENCES tripprices(id)
);

-- Favorites table
CREATE TABLE favorites (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  selectedpackage_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (selectedpackage_id) REFERENCES selectedpackages(id)
);

-- ContactMessages table
CREATE TABLE contactmessages (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  contactmessage_subject VARCHAR(255),
  contactmessage_message TEXT,
  created_at DATETIME,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Carts table
CREATE TABLE carts (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  created_at DATETIME,
  status VARCHAR(50),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- CartItems table
CREATE TABLE cart_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  cart_id INT,
  selectedpackage_id INT,
  FOREIGN KEY (cart_id) REFERENCES carts(id),
  FOREIGN KEY (selectedpackage_id) REFERENCES selectedpackages(id)
);

-- Orders table
CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  order_date DATETIME,
  total_price DECIMAL(10, 2),
  status VARCHAR(50),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- OrderItems table
CREATE TABLE order_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  selectedpackage_id INT NOT NULL,
  price_at_purchase DECIMAL(10, 2),
  FOREIGN KEY (order_id) REFERENCES orders(id),
  FOREIGN KEY (selectedpackage_id) REFERENCES selectedpackages(id)
);
