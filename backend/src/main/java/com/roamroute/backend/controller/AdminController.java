package com.roamroute.backend.controller;

import java.util.List;

import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
import com.roamroute.backend.dto.UpdateUserRoleRequest;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.OrderRepository;
import com.roamroute.backend.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin / Users", description = "Manage user accounts and roles")
public class AdminController {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;

  public AdminController(UserRepository userRepository, OrderRepository orderRepository) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
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
          order.getStatus()
        );

      })
      .toList();
  }

  @PutMapping("/users/{id}/role")
  @Operation(summary = "Update a user's role (USER or ADMIN)")
  public AdminUsersDTO updateUserRole(@PathVariable int id, @RequestBody UpdateUserRoleRequest request) {
    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("User not found"));

    String newRole = request.getRole();

    if (!newRole.equals("USER") && !newRole.equals("ADMIN")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid role. Role must be either 'USER' or 'ADMIN'.");
    }

    user.setUser_role(newRole);

    User updatedUser = userRepository.save(user);

    return new AdminUsersDTO(updatedUser.getId(), updatedUser.getUser_name(), updatedUser.getEmail(), updatedUser.getUser_role());
  }

}
