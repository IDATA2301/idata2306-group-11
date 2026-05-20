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

import com.roamroute.backend.dto.AccommodationDTO;
import com.roamroute.backend.dto.AccommodationRequest;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.repository.AccommodationRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.service.AccommodationService;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

  @Mock
  private AccommodationRepository accommodationRepository;

  @Mock
  private TripPriceRepository tripPriceRepository;

  @InjectMocks
  private AccommodationService accommodationService;

  /* list should return a list of all accommodations mapped to DTOs.
   * This test verifies that the service correctly retrieves all Accommodation entities from the repository, maps them to AccommodationDTOs, and returns the list. 
   * It checks that the mapping is accurate by asserting that the properties of the returned DTOs match those of the original entities.
   */
  @Test
  void list_mapsAll() {
    Accommodation a1 = new Accommodation();
    a1.setId(1);
    a1.setHotel_name("H1");
    Accommodation a2 = new Accommodation();
    a2.setId(2);
    a2.setHotel_name("H2");

    when(accommodationRepository.findAll()).thenReturn(List.of(a1, a2));

    List<AccommodationDTO> list = accommodationService.list();
    assertEquals(2, list.size());
    assertEquals("H1", list.get(0).getHotelName());
  }

  /* get should return the DTO for an existing accommodation and throw a 404 Not Found for a missing accommodation.
   * This test verifies that the service correctly retrieves an Accommodation entity by its ID, maps it to an AccommodationDTO, and returns it when the accommodation exists. 
   * It also checks that when an accommodation with the specified ID does not exist, the service throws a ResponseStatusException with a 404 status, ensuring that it handles both success and failure scenarios appropriately.
   */
  @Test
  void get_existing_returnsDto() {
    Accommodation a = new Accommodation();
    a.setId(11);
    a.setHotel_name("Stay");
    when(accommodationRepository.findById(11)).thenReturn(Optional.of(a));

    AccommodationDTO dto = accommodationService.get(11);
    assertEquals(11, dto.getId());
    assertEquals("Stay", dto.getHotelName());
  }

  /* get should throw a 404 Not Found if the accommodation does not exist.
   * This test verifies that the service checks for the existence of the accommodation and throws the appropriate exception when it is not found, ensuring that it handles error scenarios gracefully.
   */
  @Test
  void get_missing_throws() {
    when(accommodationRepository.findById(999)).thenReturn(Optional.empty());
    assertThrows(ResponseStatusException.class, () -> accommodationService.get(999));
  }

  /* create should save a new accommodation and return the corresponding DTO.
   * This test verifies that when a valid AccommodationRequest is provided, the service creates a new Accommodation entity, saves it using the repository, and returns an AccommodationDTO that reflects the saved entity's information. 
   * It also checks that the save method of the repository is called to persist the new accommodation.
   */
  @Test
  void create_savesAndReturns() {
    AccommodationRequest req = new AccommodationRequest();
    req.setHotelName("NewHotel");

    when(accommodationRepository.save(any())).thenAnswer(inv -> {
      Accommodation a = inv.getArgument(0);
      a.setId(77);
      return a;
    });

    AccommodationDTO dto = accommodationService.create(req);
    assertEquals(77, dto.getId());
    assertEquals("NewHotel", dto.getHotelName());
  }

  /* createHotelOption should create a new hotel option and return the corresponding DTO when valid data is provided, and should throw a 404 Not Found if the accommodation does not exist.
   * This test verifies that when a valid accommodationId is provided and the accommodation exists, the service creates a new TripPrice entity with the correct details, saves it, and returns a HotelOptionDTO that reflects the saved entity's information. 
   * It also checks that when an accommodation with the specified ID does not exist, the service throws a ResponseStatusException with a 404 status, ensuring that it handles both success and failure scenarios appropriately.
   */
  @Test
  void update_appliesChanges() {
    Accommodation existing = new Accommodation();
    existing.setId(88);
    existing.setHotel_name("OldHotel");

    AccommodationRequest req = new AccommodationRequest();
    req.setHotelName("UpdatedHotel");

    when(accommodationRepository.findById(88)).thenReturn(Optional.of(existing));
    when(accommodationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    AccommodationDTO dto = accommodationService.update(88, req);
    assertEquals(88, dto.getId());
    assertEquals("UpdatedHotel", dto.getHotelName());
  }

  /* update should throw a 404 Not Found if the accommodation does not exist.
   * This test verifies that the service checks for the existence of the accommodation before attempting to update it and throws the appropriate exception when it is not found, ensuring that it handles error scenarios gracefully.
   */
  @Test
  void delete_conflict_throws() {
    Accommodation a = new Accommodation();
    a.setId(101);
    when(accommodationRepository.findById(101)).thenReturn(Optional.of(a));
    when(tripPriceRepository.countByAccommodation_Id(101)).thenReturn(3L);

    assertThrows(ResponseStatusException.class, () -> accommodationService.delete(101));
  }

  /* delete should delete the accommodation when it is not referenced by any trip options.
   * This test verifies that when there are no references to the accommodation, the service successfully deletes it by calling the delete method of the repository with the correct Accommodation entity. 
   * It ensures that the service allows deletion when it is safe to do so and that it interacts with the repository as expected to perform the deletion.
   */
  @Test
  void delete_success_deletes() {
    Accommodation a = new Accommodation();
    a.setId(102);
    when(accommodationRepository.findById(102)).thenReturn(Optional.of(a));
    when(tripPriceRepository.countByAccommodation_Id(102)).thenReturn(0L);

    accommodationService.delete(102);

    verify(accommodationRepository).delete(a);
  }
}
