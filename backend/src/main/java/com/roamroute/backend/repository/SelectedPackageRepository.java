package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.SelectedPackage;

public interface SelectedPackageRepository extends JpaRepository<SelectedPackage, Integer> {

  long countByFlightTripPrice_Id(int tripPriceId);

  long countByHotelTripPrice_Id(int tripPriceId);

  void deleteByTrip_Id(int tripId);
}
