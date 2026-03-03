package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
