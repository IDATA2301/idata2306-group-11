package com.roamroute.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
  List<Favorite> findByUser_Id(int userId);

  Optional<Favorite> findByUser_IdAndTrip_Id(int userId, int tripId);
}
