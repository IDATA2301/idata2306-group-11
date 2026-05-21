package com.roamroute.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roamroute.backend.entity.User;

/**
 * Spring Data JPA repository for User entity, providing custom query methods for email and username lookup.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.user_name) = LOWER(:userName)")
  boolean existsByUserNameIgnoreCase(@Param("userName") String userName);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.user_name) = LOWER(:userName) AND u.id <> :id")
  boolean existsByUserNameIgnoreCaseAndIdNot(@Param("userName") String userName, @Param("id") int id);

}
