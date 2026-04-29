package com.roamroute.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.TripHomeDTO;
import com.roamroute.backend.entity.Favorite;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.FavoriteRepository;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.repository.UserRepository;
import com.roamroute.backend.service.TripService;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {
  private final FavoriteRepository favoriteRepository;
  private final TripService tripService;
  private final TripRepository tripRepository;
  private final UserRepository userRepository;

  public FavoriteController(FavoriteRepository favoriteRepository,
                            TripService tripService,
                            TripRepository tripRepository,
                            UserRepository userRepository) {
    this.favoriteRepository = favoriteRepository;
    this.tripService = tripService;
    this.tripRepository = tripRepository;
    this.userRepository = userRepository;
  }

  @PostMapping
  public Favorite addFavorite(Authentication authentication, @RequestParam int tripId) {

    String userEmail = authentication.getName(); 
    User user = userRepository.findByEmail(userEmail).orElseThrow();
    Trip trip = tripRepository.findById(tripId).orElseThrow();

    // Prevent double favorites for the same trip and user
    Optional<Favorite> existingFavorite = favoriteRepository.findByUser_IdAndTrip_Id(user.getId(), tripId);
    if (existingFavorite.isPresent()) {
      return existingFavorite.get();
    }

    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setTrip(trip);

    return favoriteRepository.save(favorite);
  }

  @GetMapping("/trips")
  public List<TripHomeDTO> getFavoriteTrips(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow();
    List<Favorite> favorites = favoriteRepository.findByUser_Id(user.getId());

    return favorites.stream()
      .map(fav -> tripService.toTripHomeDTO(fav.getTrip()))
      .toList();
  }

  @GetMapping
  public List<Favorite> getFavoritesByUserEmail(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow();
    return favoriteRepository.findByUser_Id(user.getId());
  }

  @DeleteMapping("/{tripId}")
  public void removeFavorite(Authentication authentication, @PathVariable int tripId) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow();

    Favorite favorite = favoriteRepository.findByUser_IdAndTrip_Id(user.getId(), tripId).orElseThrow();
    favoriteRepository.delete(favorite);
  }
}
