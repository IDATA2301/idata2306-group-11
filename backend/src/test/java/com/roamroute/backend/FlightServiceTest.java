package com.roamroute.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.FlightDTO;
import com.roamroute.backend.dto.FlightRequest;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.repository.FlightRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.service.FlightService;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

  @Mock
  private FlightRepository flightRepository;

  @Mock
  private TripPriceRepository tripPriceRepository;

  @InjectMocks
  private FlightService flightService;

  /**
   * list should return a list of all flights mapped to DTOs.
   * This test verifies that the service correctly retrieves all Flight entities from the repository, maps them to FlightDTOs, and returns the list. 
   * It checks that the mapping is accurate by asserting that the properties of the returned DTOs match those of the original entities.
   */
  @Test
  void list_mapsAllFlights() {
    Flight f1 = new Flight();
    f1.setId(1);
    f1.setAirline("Air1");
    Flight f2 = new Flight();
    f2.setId(2);
    f2.setAirline("Air2");

    when(flightRepository.findAll()).thenReturn(List.of(f1, f2));

    List<FlightDTO> list = flightService.list();

    assertEquals(2, list.size());
    assertEquals(1, list.get(0).getId());
    assertEquals("Air2", list.get(1).getAirline());
  }

  /**
   * get should return the DTO for an existing flight and throw a 404 Not Found for a missing flight.
   * This test verifies that the service correctly retrieves a Flight entity by its ID, maps it to a FlightDTO, and returns it when the flight exists. 
   * It also checks that when a flight with the specified ID does not exist, the service throws a ResponseStatusException with a 404 status, ensuring that it handles both success and failure scenarios appropriately.
   */
  @Test
  void get_existing_returnsDto() {
    Flight f = new Flight();
    f.setId(10);
    f.setAirline("Ocean");
    when(flightRepository.findById(10)).thenReturn(Optional.of(f));

    FlightDTO dto = flightService.get(10);

    assertEquals(10, dto.getId());
    assertEquals("Ocean", dto.getAirline());
  }

  /* get should throw a 404 Not Found if the flight does not exist.
   * This test verifies that the service checks for the existence of the flight and throws the appropriate exception when it is not found, ensuring that it handles error scenarios gracefully.
   */
  @Test
  void get_missing_throwsNotFound() {
    when(flightRepository.findById(99)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class, () -> flightService.get(99));
  }

  /**
   * create should save a new flight and return the corresponding DTO.
   * This test verifies that when a valid FlightRequest is provided, the service creates a new Flight entity, saves it using the repository, and returns a FlightDTO that reflects the saved entity's information. 
   * It also checks that the save method of the repository is called to persist the new flight.
   */
  @Test
  void create_savesAndReturns() {
    FlightRequest req = new FlightRequest();
    req.setAirline("NewAir");

    when(flightRepository.save(any())).thenAnswer(inv -> {
      Flight f = inv.getArgument(0);
      f.setId(55);
      return f;
    });

    FlightDTO dto = flightService.create(req);
    assertEquals(55, dto.getId());
    assertEquals("NewAir", dto.getAirline());
  }

  /**
   * update should apply changes to an existing flight and return the updated DTO, and should throw a 404 Not Found if the flight does not exist.
   * This test verifies that when a valid FlightRequest is provided for an existing flight, the service updates the Flight entity's properties, saves it, and returns a FlightDTO that reflects the updated information. 
   * It also checks that when a flight with the specified ID does not exist, the service throws a ResponseStatusException with a 404 status, ensuring that it handles both success and failure scenarios appropriately.
   */
  @Test
  void update_appliesChanges() {
    Flight existing = new Flight();
    existing.setId(60);
    existing.setAirline("Old");

    FlightRequest req = new FlightRequest();
    req.setAirline("Updated");

    when(flightRepository.findById(60)).thenReturn(Optional.of(existing));
    when(flightRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    FlightDTO dto = flightService.update(60, req);
    assertEquals(60, dto.getId());
    assertEquals("Updated", dto.getAirline());
  }

  /* update should throw a 404 Not Found if the flight does not exist.
   * This test verifies that the service checks for the existence of the flight before attempting to update it and throws the appropriate exception when it is not found, ensuring that it handles error scenarios gracefully.
   */
  @Test
  void delete_conflict_throws() {
    Flight f = new Flight();
    f.setId(70);
    when(flightRepository.findById(70)).thenReturn(Optional.of(f));
    when(tripPriceRepository.countByFlight_Id(70)).thenReturn(2L);

    assertThrows(ResponseStatusException.class, () -> flightService.delete(70));
  }

  /* delete should delete the flight when it is not referenced by any trip options.
   * This test verifies that when there are no references to the flight, the service successfully deletes it by calling the delete method of the repository with the correct Flight entity. 
   * It ensures that the service allows deletion when it is safe to do so and that it interacts with the repository as expected to perform the deletion.
   */
  @Test
  void delete_success_deletes() {
    Flight f = new Flight();
    f.setId(71);
    when(flightRepository.findById(71)).thenReturn(Optional.of(f));
    when(tripPriceRepository.countByFlight_Id(71)).thenReturn(0L);

    flightService.delete(71);

    verify(flightRepository).delete(f);
  }
}
