-- Reset database so this script can be rerun safely
DROP DATABASE IF EXISTS roamroute_db;
CREATE DATABASE roamroute_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE roamroute_db;

-- Users table
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  user_password VARCHAR(255),
  user_role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
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
  nights INT,
  latitude DOUBLE(10, 6),
  longitude DOUBLE(10, 6)
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
  image_url VARCHAR(500),
  FOREIGN KEY (destination_id) REFERENCES destinations(id)
);

-- TripPrices table
CREATE TABLE tripprices (
  id INT PRIMARY KEY AUTO_INCREMENT,
  trip_id INT,
  tripprice_provider VARCHAR(255),
  price DECIMAL(10, 2),
  tripprice_type VARCHAR(50),
  flight_id INT,
  accommodation_id INT,
  FOREIGN KEY (trip_id) REFERENCES trips(id),
  FOREIGN KEY (flight_id) REFERENCES flights(id),
  FOREIGN KEY (accommodation_id) REFERENCES accommodations(id)
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
  trip_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (trip_id) REFERENCES trips(id)
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

-- Seed data
INSERT INTO destinations (city, country, image_url, image_alt)
VALUES
  ('Barcelona', 'Spain', 'barcelona.jpg', 'Beautiful view of Barcelona'),
  ('Naples', 'Italy', 'naples.jpg', 'Scenic view of Naples'),
  ('Paris', 'France', 'parisDest.jpg', 'Iconic view of Paris'),
  ('Tokyo', 'Japan', 'tokyoDest.png', 'Vibrant cityscape of Tokyo');

INSERT INTO flights (airline, departure_city, destination_city, departure_airport, destination_airport, flight_duration)
VALUES
  ('Vueling', 'Alesund', 'Barcelona', 'AES', 'BCN', '5-7h'),
  ('KLM', 'Alesund', 'Barcelona', 'AES', 'BCN', '5-7h'),
  ('Norwegian', 'Oslo', 'Naples', 'OSL', 'NAP', '3.5-5.5h'),
  ('Lufthansa', 'Oslo', 'Naples', 'OSL', 'NAP', '3.5-5.5h'),
  ('Air France', 'Trondheim', 'Paris', 'TRD', 'CDG', '4-6h'),
  ('SAS', 'Trondheim', 'Paris', 'TRD', 'CDG', '4-6h'),
  ('ANA', 'Oslo', 'Tokyo', 'OSL', 'HND', '14-17h'),
  ('Qatar Airways', 'Oslo', 'Tokyo', 'OSL', 'HND', '14-17h');

INSERT INTO accommodations
(hotel_name, hotel_type, hotel_city, hotel_location, amenities, nights, latitude, longitude)
VALUES
  ('Hotel Rialto', '3 star, Family-friendly', 'Barcelona', 'Barcelona city center',
   'Breakfast included, Wi-Fi, Family rooms', 7, 41.382250, 2.177740),
  ('Hotel Piazza Bellini', '3 star, Boutique, Centrally located', 'Naples', 'Naples Historic Center',
   'Breakfast included, Wi-Fi, Air Conditioning', 7, 40.874230, 14.298920),
  ('Hotel des Arts Montmartre', '3 star, Boutique', 'Paris', 'Montmartre, Paris',
   'Free Wi-Fi, Breakfast available, Central location, Air conditioning', 8, 48.886390, 2.349010),
  ('Hotel Mystays Premier', '4 star', 'Tokyo', 'Shinjuku',
   'Breakfast, Wi-Fi, Airport shuttle', 10, 35.695050, 139.697270);

INSERT INTO trips (id, title, trip_description, start_date, end_date, duration_days, keywords, destination_id, image_url)
VALUES
  (1023, 'Warm winter trip Alesund -> Barcelona',
   'A family-friendly economy trip from Alesund to Barcelona, ideal for a warm winter escape.',
   '2026-02-13', '2026-02-20', 7, 'Economy, Family, City Break, Winter Sun', 1, 'barcelonaTrip.jpg'),
  (2047, 'Spring Getaway Oslo -> Naples',
   'A relaxing and budget-friendly cultural trip from Oslo to Naples, perfect for travelers seeking warmer weather, Italian cuisine, and historic attractions.',
   '2026-04-10', '2026-04-17', 7, 'Economy, Couples, City Break, Spring Sun', 2, 'naplesTrip.jpg'),
  (3198, 'Romantic Spring Escape Trondheim -> Paris',
   'A relaxed and romantic spring getaway from Trondheim to Paris, perfect for couples seeking culture, food, and scenic strolls along the Seine.',
   '2026-04-05', '2026-04-12', 8, 'Romance, Culture, Economy, City Experience', 3, 'parisTrip.png'),
  (2031, 'Spring Explorer Trip Oslo -> Tokyo',
   'A cultural discovery trip from Oslo to Tokyo, perfect for travelers seeking food, history, and modern city life.',
   '2026-04-02', '2026-04-12', 10, 'Adventure, Culture, Cherry Blossom, Budget-Friendly', 4, 'tokyoTrip.jpg');

INSERT INTO tripprices (trip_id, tripprice_provider, price, tripprice_type, flight_id, accommodation_id)
VALUES
  (1023, 'Hotels.com', 645.00, 'HOTEL', NULL, 1),
  (1023, 'Booking.com', 620.00, 'HOTEL', NULL, 1),
  (1023, 'SkyScanner', 150.00, 'FLIGHT', 1, NULL),
  (1023, 'Expedia', 175.00, 'FLIGHT', 1, NULL),
  (1023, 'Momondo', 160.00, 'FLIGHT', 2, NULL),
  (2047, 'AirBnB', 540.00, 'HOTEL', NULL, 2),
  (2047, 'GetYourGuide', 565.00, 'HOTEL', NULL, 2),
  (2047, 'TripAdvisor', 210.00, 'FLIGHT', 3, NULL),
  (2047, 'Kiwi', 230.00, 'FLIGHT', 3, NULL),
  (2047, 'Kayak', 215.00, 'FLIGHT', 4, NULL),
  (3198, 'Booking.com', 900.00, 'HOTEL', NULL, 3),
  (3198, 'Agoda', 880.00, 'HOTEL', NULL, 3),
  (3198, 'Google Flights', 250.00, 'FLIGHT', 5, NULL),
  (3198, 'Kayak', 240.00, 'FLIGHT', 5, NULL),
  (3198, 'Kiwi.com', 270.00, 'FLIGHT', 6, NULL),
  (2031, 'Agoda', 1015.00, 'HOTEL', NULL, 4),
  (2031, 'Booking.com', 1050.00, 'HOTEL', NULL, 4),
  (2031, 'SkyScanner', 830.00, 'FLIGHT', 7, NULL),
  (2031, 'Kayak', 860.00, 'FLIGHT', 7, NULL),
  (2031, 'Momondo', 810.00, 'FLIGHT', 8, NULL);
