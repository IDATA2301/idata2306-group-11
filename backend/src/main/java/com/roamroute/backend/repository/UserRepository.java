package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByEmail(String email);

}
