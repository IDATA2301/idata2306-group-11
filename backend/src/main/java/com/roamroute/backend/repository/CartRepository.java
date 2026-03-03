package com.roamroute.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.Cart;
import com.roamroute.backend.entity.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

  Optional<Cart> findByUserAndStatus(User user, String status);
  

}
