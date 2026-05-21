package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.AdminUserDetailsDTO;
import com.roamroute.backend.dto.AdminUsersDTO;
import com.roamroute.backend.dto.AdminUsersOrdersDTO;
import com.roamroute.backend.dto.UpdateOrderRequest;
import com.roamroute.backend.dto.UpdateUserRoleRequest;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.entity.Order;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.AccommodationRepository;
import com.roamroute.backend.repository.ContactMessageRepository;
import com.roamroute.backend.repository.FavoriteRepository;
import com.roamroute.backend.repository.FlightRepository;
import com.roamroute.backend.repository.OrderRepository;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Admin endpoints for managing users, orders and related entities.
 *
 * <p>Exposes operations such as listing users, inspecting user orders, updating
 * orders and user roles, and deleting users or orders. Methods are intended
 * for administrator use only and rely on repository operations for persistence.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin / Users", description = "Manage user accounts and roles")
public class AdminController {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final FavoriteRepository favoriteRepository;
  private final ContactMessageRepository contactMessageRepository;
  private final TripRepository tripRepository;
  private final FlightRepository flightRepository;
  private final AccommodationRepository accommodationRepository;

  public AdminController(UserRepository userRepository, OrderRepository orderRepository,
      FavoriteRepository favoriteRepository, ContactMessageRepository contactMessageRepository,
      TripRepository tripRepository, FlightRepository flightRepository,
      AccommodationRepository accommodationRepository) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.favoriteRepository = favoriteRepository;
    this.contactMessageRepository = contactMessageRepository;
    this.tripRepository = tripRepository;
    this.flightRepository = flightRepository;
    this.accommodationRepository = accommodationRepository;
  }

  @GetMapping("/users")
  @Operation(summary = "List all users (admin)")
  public List<AdminUsersDTO> getAllUsers(Authentication authentication) {
    System.out.println(authentication.getName());
    System.out.println(authentication.getAuthorities());

    return userRepository.findAll()
      .stream()
      .map(user -> new AdminUsersDTO(user.getId(), user.getUser_name(), user.getEmail(), user.getUser_role()))
      .toList();
  }

  @GetMapping("/users/{id}")
  @Operation(summary = "Get full details for a single user (admin)")
  public AdminUserDetailsDTO getUserDetailsById(@PathVariable int id) {
    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("User not found"));
    return new AdminUserDetailsDTO(user.getId(), user.getUser_name(), user.getEmail(), user.getUser_role());
  }

  @GetMapping("/users/{id}/orders")
  @Operation(summary = "List orders for a specific user (admin)")
  public List<AdminUsersOrdersDTO> getOrdersByUser(@PathVariable int id) {
    return orderRepository.findByUser_Id(id)
      .stream()
      .map(order -> {
        Trip trip = order.getTrip();
        Flight flight = order.getFlight();
        Accommodation accommodation = order.getAccommodation();

        return new AdminUsersOrdersDTO(
          order.getId(),
          trip.getTitle(),
          flight != null ? flight.getAirline() : null,
          accommodation != null ? accommodation.getHotel_name() : null,
          order.getTotal_price(),
          trip.getStart_date(),
          trip.getEnd_date(),
          order.getStatus(),
          trip.getId(),
          flight != null ? flight.getId() : null,
          accommodation != null ? accommodation.getId() : null
        );
      })
      .toList();
  }

  @PutMapping("/orders/{id}")
  @Operation(summary = "Update an order's trip, flight, accommodation, status, and price (admin)")
  public AdminUsersOrdersDTO updateOrder(@PathVariable int id, @RequestBody UpdateOrderRequest request) {
    Order order = orderRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

    Trip trip = tripRepository.findById(request.getTripId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

    Flight flight = request.getFlightId() != null
      ? flightRepository.findById(request.getFlightId()).orElse(null)
      : null;

    Accommodation accommodation = request.getAccommodationId() != null
      ? accommodationRepository.findById(request.getAccommodationId()).orElse(null)
      : null;

    order.setTrip(trip);
    order.setFlight(flight);
    order.setAccommodation(accommodation);
    order.setStatus(request.getStatus());
    order.setTotal_price(request.getTotalPrice());

    Order updated = orderRepository.save(order);

    return new AdminUsersOrdersDTO(
      updated.getId(),
      trip.getTitle(),
      flight != null ? flight.getAirline() : null,
      accommodation != null ? accommodation.getHotel_name() : null,
      updated.getTotal_price(),
      trip.getStart_date(),
      trip.getEnd_date(),
      updated.getStatus(),
      trip.getId(),
      flight != null ? flight.getId() : null,
      accommodation != null ? accommodation.getId() : null
    );
  }

  @DeleteMapping("/orders/{id}")
  @Operation(summary = "Delete an order (admin)")
  public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
    if (!orderRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    orderRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/users/{id}")
  @Operation(summary = "Delete a user (admin)")
  public ResponseEntity<Void> deleteUser(@PathVariable int id) {
    if (!userRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    orderRepository.deleteAll(orderRepository.findByUser_Id(id));
    favoriteRepository.deleteAll(favoriteRepository.findByUser_Id(id));
    contactMessageRepository.deleteAll(contactMessageRepository.findByUser_Id(id));
    userRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/users/{id}/role")
  @Operation(summary = "Update a user's role (USER or ADMIN)")
  public AdminUsersDTO updateUserRole(@PathVariable int id, @RequestBody UpdateUserRoleRequest request) {
    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("User not found"));

    String newRole = request.getRole();
    if (!newRole.equals("USER") && !newRole.equals("ADMIN")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role. Role must be either 'USER' or 'ADMIN'.");
    }

    user.setUser_role(newRole);
    User updatedUser = userRepository.save(user);
    return new AdminUsersDTO(updatedUser.getId(), updatedUser.getUser_name(), updatedUser.getEmail(), updatedUser.getUser_role());
  }

}
