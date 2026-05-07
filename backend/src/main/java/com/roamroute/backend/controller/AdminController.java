package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.AdminUserDetailsDTO;
import com.roamroute.backend.dto.AdminUsersDTO;
import com.roamroute.backend.dto.AdminUsersOrdersDTO;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.OrderRepository;
import com.roamroute.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;

  public AdminController(UserRepository userRepository, OrderRepository orderRepository) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
  }

  @GetMapping("/users")
  public List<AdminUsersDTO> getAllUsers(Authentication authentication) {
    System.out.println(authentication.getName());
    System.out.println(authentication.getAuthorities());

    return userRepository.findAll()
      .stream()
      .map(user -> new AdminUsersDTO(user.getId(), user.getUser_name(), user.getEmail(), user.getUser_role()))
      .toList();
  }

  @GetMapping("/users/{id}")
  public AdminUserDetailsDTO getUserDetailsById(@PathVariable int id) {

    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("User not found"));

    return new AdminUserDetailsDTO(user.getId(), user.getUser_name(), user.getEmail(), user.getUser_role());
  }
  
  @GetMapping("/users/{id}/orders")
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
}
