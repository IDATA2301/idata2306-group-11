package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roamroute.backend.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

}
