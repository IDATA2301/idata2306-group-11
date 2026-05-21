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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing authenticated user's favorites.
 *
 * <p>Allows adding, listing and removing favorites, and provides an endpoint
 * that returns the favorited trips as `TripHomeDTO` objects for the UI.
 */
@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
@Tag(name = "Favorites", description = "Trip favorites for the authenticated user")
public class FavoriteController {
  private final FavoriteRepository favoriteRepository;
  private final TripRepository tripRepository;
  private final UserRepository userRepository;
  private final TripService tripService;

  public FavoriteController(FavoriteRepository favoriteRepository,
                            TripRepository tripRepository,
                            UserRepository userRepository,
                            TripService tripService) {
    this.favoriteRepository = favoriteRepository;
    this.tripRepository = tripRepository;
    this.userRepository = userRepository;
    this.tripService = tripService;
  }

  @PostMapping
  @Operation(summary = "Add a trip to the authenticated user's favorites")
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

  @GetMapping
  @Operation(summary = "List favorite records for the authenticated user")
  public List<Favorite> getFavoritesByUserEmail(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow();
    return favoriteRepository.findByUser_Id(user.getId());
  }

  @GetMapping("/trips")
  @Operation(summary = "List favorited trips for the authenticated user")
  public List<TripHomeDTO> getFavoriteTrips(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow();
    return favoriteRepository.findByUser_Id(user.getId()).stream()
      .map(Favorite::getTrip)
      .map(tripService::toTripHomeDTO)
      .toList();
  }

  @DeleteMapping("/{tripId}")
  @Operation(summary = "Remove a trip from the authenticated user's favorites")
  public void removeFavorite(Authentication authentication, @PathVariable int tripId) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow();

    Favorite favorite = favoriteRepository.findByUser_IdAndTrip_Id(user.getId(), tripId).orElseThrow();
    favoriteRepository.delete(favorite);
  }
}
