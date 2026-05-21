package com.roamroute.backend.controller;

import com.roamroute.backend.repository.AccommodationRepository;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller handling order placement and retrieval for authenticated users.
 *
 * <p>Users can place new orders (combining flight and accommodation options)
 * and retrieve their existing orders. Security checks ensure only the owner
 * or an admin may view a specific order.
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order placement and retrieval for the authenticated user")
public class OrderController {

  private final AccommodationRepository accommodationRepository;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final TripRepository tripRepository;
  private final TripPriceRepository tripPriceRepository;

  public OrderController(
    OrderRepository orderRepository,
    UserRepository userRepository,
    TripRepository tripRepository,
    TripPriceRepository tripPriceRepository, AccommodationRepository accommodationRepository
  ) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.tripRepository = tripRepository;
    this.tripPriceRepository = tripPriceRepository;
    this.accommodationRepository = accommodationRepository;
  }

  @PostMapping
  @Operation(summary = "Place a new order for the authenticated user")
  public Order createOrder(@RequestBody CreateOrderRequest req, Authentication auth) {

    if (auth == null || !auth.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated to place an order");
    }
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
  @Operation(summary = "List orders belonging to the authenticated user")
  public List<Order> getUserOrders(Authentication auth) {
    User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
    return orderRepository.findByUser(user);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a single order (owner or admin only)")
  public Order getOrderById(@PathVariable int id, Authentication auth) {
    User user = userRepository.findByEmail(auth.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

    boolean isOwner = order.getUser() != null && order.getUser().getId() == user.getId();
    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

    if (!isOwner && !isAdmin) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to view this order");
    }

    return order;
  }

}
