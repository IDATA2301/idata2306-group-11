package com.roamroute.backend.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.entity.Order;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.TripPrice;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.OrderRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.repository.UserRepository;
import com.roamroute.backend.dto.CreateOrderRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final TripRepository tripRepository;
  private final TripPriceRepository tripPriceRepository;

  public OrderController(
    OrderRepository orderRepository,
    UserRepository userRepository,
    TripRepository tripRepository,
    TripPriceRepository tripPriceRepository
  ) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
    this.tripPriceRepository = tripPriceRepository;
  }

  @PostMapping
  public Order createOrder(@RequestBody CreateOrderRequest req, Authentication auth) {
    System.out.println("=== DEBUG START ===");
    System.out.println("AUTH OBJECT: " + auth);
    System.out.println("EMAIL FROM TOKEN: " + (auth != null ? auth.getName() : "null"));
    System.out.println("=== DEBUG END ===");
    User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
    Trip trip = tripRepository.findById(req.getTripId()).orElseThrow(() -> new RuntimeException("Trip not found"));
    TripPrice flightPrice = tripPriceRepository
      .findById(req.getFlightPriceId())
      .orElseThrow(() -> new RuntimeException("Flight price not found"));
    
    TripPrice accommodationPrice = tripPriceRepository
      .findById(req.getAccommodationPriceId())
      .orElseThrow(() -> new RuntimeException("Accommodation price not found"));

    Flight flight = flightPrice.getFlight();
    Accommodation accommodation = accommodationPrice.getAccommodation();

    BigDecimal totalPrice = flightPrice.getPrice().add(accommodationPrice.getPrice());

    Order order = new Order();
    order.setUser(user);
    order.setTrip(trip);
    order.setFlight(flight);
    order.setAccommodation(accommodation);
    order.setTotal_price(totalPrice);
    order.setStatus("CONFIRMED");
    order.setOrder_date(new Date(System.currentTimeMillis()));

    return orderRepository.save(order);
  }

  @GetMapping
  public List<Order> getUserOrders(Authentication auth) {
    User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
    return orderRepository.findByUser(user);
  }

  @GetMapping("/{id}")
  public Order getOrderById(@PathVariable int id) {
    return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
  }

}
