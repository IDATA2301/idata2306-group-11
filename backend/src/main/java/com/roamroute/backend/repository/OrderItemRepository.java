package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
