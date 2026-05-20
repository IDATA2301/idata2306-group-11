package com.roamroute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.Order;
import com.roamroute.backend.entity.User;

public interface OrderRepository extends JpaRepository<Order, Integer> {

  List<Order> findByUser(User user);

  List<Order> findByUser_Id(int userId);

  boolean existsByTrip_Id(int tripId);
}
