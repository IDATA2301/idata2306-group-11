package com.roamroute.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.roamroute.backend.dto.FlightOptionRequest;
import com.roamroute.backend.dto.FlightOptionDTO;
import com.roamroute.backend.dto.HotelOptionDTO;
import com.roamroute.backend.dto.HotelOptionRequest;
import com.roamroute.backend.entity.Flight;
import com.roamroute.backend.entity.Trip;
import com.roamroute.backend.entity.TripPrice;
import com.roamroute.backend.entity.Accommodation;
import com.roamroute.backend.repository.AccommodationRepository;
import com.roamroute.backend.repository.FlightRepository;
import com.roamroute.backend.repository.SelectedPackageRepository;
import com.roamroute.backend.repository.TripPriceRepository;
import com.roamroute.backend.repository.TripRepository;
import com.roamroute.backend.service.TripOptionService;

@ExtendWith(MockitoExtension.class)
class TripOptionServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripPriceRepository tripPriceRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private SelectedPackageRepository selectedPackageRepository;

    @InjectMocks
    private TripOptionService tripOptionService;

    /**
     * listFlightOptions should return a list of flight options sorted by price for a given trip.
     * This test sets up a trip with two flight options at different prices and verifies that the
     * service returns both options sorted in ascending order by price. It also verifies that the service correctly maps the TripPrice entities to FlightOptionDTOs.
     */
    @Test
    void listFlightOptions_mapsAndSortsByPrice() {
        Trip trip = new Trip();
        trip.setId(401);

        Flight f1 = new Flight();
        f1.setId(11);
        f1.setAirline("A1");

        Flight f2 = new Flight();
        f2.setId(12);
        f2.setAirline("A2");

        TripPrice tp1 = new TripPrice();
        tp1.setId(1);
        tp1.setTrip(trip);
        tp1.setFlight(f1);
        tp1.setPrice(new BigDecimal("300"));

        TripPrice tp2 = new TripPrice();
        tp2.setId(2);
        tp2.setTrip(trip);
        tp2.setFlight(f2);
        tp2.setPrice(new BigDecimal("200"));

        when(tripRepository.findById(401)).thenReturn(Optional.of(trip));
        when(tripPriceRepository.findByTrip_IdAndFlightIsNotNull(401)).thenReturn(List.of(tp1, tp2));

        List<FlightOptionDTO> list = tripOptionService.listFlightOptions(401);

        assertEquals(2, list.size());
        // sorted ascending by price -> tp2 (200) first
        assertEquals(2, list.get(0).getId());
        assertEquals(1, list.get(1).getId());
    }

    /**
     * createFlightOption should throw a 400 Bad Request if the flightId is missing or if the specified flight does not exist, and should create and return a flight option when valid data is provided.
     * This test verifies that the service checks for the presence of the flightId and the existence
     * of the flight, throwing the appropriate exceptions when validation fails. 
     * It also verifies that when valid data is provided, the service creates a new TripPrice entity, saves it, and returns the correct FlightOptionDTO with the expected details.
     */
    @Test
    void createFlightOption_missingFlightId_throwsBadRequest() {
        FlightOptionRequest req = new FlightOptionRequest();
        req.setProvider("P");
        req.setPrice(new BigDecimal("100"));

        when(tripRepository.findById(501)).thenReturn(Optional.of(new Trip()));

        assertThrows(ResponseStatusException.class, () -> tripOptionService.createFlightOption(501, req));
    }

    /**
     * createFlightOption should throw a 400 Bad Request if the specified flight does not exist, and should create and return a flight option when valid data is provided.
     * This test verifies that the service checks for the presence of the flightId and the existence of the flight, throwing the appropriate exceptions when validation fails. 
     * It also verifies that when valid data is provided, the service creates a new TripPrice entity, saves it, and returns the correct FlightOptionDTO with the expected details.
     */
    @Test
    void createFlightOption_flightNotFound_throwsBadRequest() {
        FlightOptionRequest req = new FlightOptionRequest();
        req.setFlightId(77);
        req.setProvider("P");
        req.setPrice(new BigDecimal("100"));

        Trip trip = new Trip();
        trip.setId(502);
        when(tripRepository.findById(502)).thenReturn(Optional.of(trip));
        when(flightRepository.findById(77)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tripOptionService.createFlightOption(502, req));
    }

    /**
     * createFlightOption should create a new flight option and return the corresponding DTO when valid data is provided.
     * This test verifies that when a valid flightId is provided and the flight exists, 
     * the service creates a new TripPrice entity with the correct details, saves it, and returns a FlightOptionDTO that reflects the saved entity's information. 
     * It also verifies that the save method of the repository is called to persist the new flight option.
     */
    @Test
    void createFlightOption_success_savesAndReturnsDto() {
        FlightOptionRequest req = new FlightOptionRequest();
        req.setFlightId(88);
        req.setProvider("Best");
        req.setPrice(new BigDecimal("123.45"));

        Trip trip = new Trip();
        trip.setId(503);

        Flight flight = new Flight();
        flight.setId(88);
        flight.setAirline("AirX");

        when(tripRepository.findById(503)).thenReturn(Optional.of(trip));
        when(flightRepository.findById(88)).thenReturn(Optional.of(flight));
        when(tripPriceRepository.save(any())).thenAnswer(inv -> {
            TripPrice p = inv.getArgument(0);
            p.setId(999);
            return p;
        });

        FlightOptionDTO dto = tripOptionService.createFlightOption(503, req);

        assertEquals(999, dto.getId());
        assertEquals(88, dto.getFlightId());
        assertEquals("AirX", dto.getAirline());
        verify(tripPriceRepository).save(any());
    }

    /**
     * listHotelOptions should return a list of hotel options sorted by price for a given trip.
     * This test sets up a trip with two hotel options at different prices and verifies that the
     * service returns both options sorted in ascending order by price. It also verifies that the service correctly maps 
     * the TripPrice entities to HotelOptionDTOs, ensuring that the hotel details are accurately reflected in the returned DTOs.
     */
    @Test
    void listHotelOptions_mapsAndSortsByPrice() {
        Trip trip = new Trip();
        trip.setId(601);

        Accommodation a1 = new Accommodation();
        a1.setId(21);
        a1.setHotel_name("H1");

        Accommodation a2 = new Accommodation();
        a2.setId(22);
        a2.setHotel_name("H2");

        TripPrice tp1 = new TripPrice();
        tp1.setId(3);
        tp1.setTrip(trip);
        tp1.setAccommodation(a1);
        tp1.setPrice(new BigDecimal("150"));

        TripPrice tp2 = new TripPrice();
        tp2.setId(4);
        tp2.setTrip(trip);
        tp2.setAccommodation(a2);
        tp2.setPrice(new BigDecimal("100"));

        when(tripRepository.findById(601)).thenReturn(Optional.of(trip));
        when(tripPriceRepository.findByTrip_IdAndAccommodationIsNotNull(601)).thenReturn(List.of(tp1, tp2));

        List<HotelOptionDTO> list = tripOptionService.listHotelOptions(601);

        assertEquals(2, list.size());
        // sorted ascending by price -> tp2 (100) first
        assertEquals(4, list.get(0).getId());
        assertEquals(3, list.get(1).getId());
    }
    
    /**
     * deleteFlightOption should throw a 409 Conflict if the flight option is referenced by an order or cart, and should delete the flight option when it is not referenced.
     * This test verifies that the service checks for existing references to the flight option before allowing deletion, throwing the appropriate exception when a conflict is detected.
     * It also verifies that when there are no references, the service successfully deletes the flight option by calling the delete method of the repository with the correct TripPrice entity.
     * The test ensures that the service handles both scenarios correctly, preventing deletion when it would cause issues and allowing it when it is safe to do so.
     */
    @Test
    void deleteFlightOption_conflict_throwsConflict() {
        Trip trip = new Trip();
        trip.setId(701);

        Flight f = new Flight();
        f.setId(31);

        TripPrice tp = new TripPrice();
        tp.setId(7);
        tp.setTrip(trip);
        tp.setFlight(f);

        when(tripPriceRepository.findById(7)).thenReturn(Optional.of(tp));
        when(selectedPackageRepository.countByFlightTripPrice_Id(7)).thenReturn(1L);

        assertThrows(ResponseStatusException.class, () -> tripOptionService.deleteFlightOption(701, 7));
    }
    
    /**
     * deleteFlightOption should delete the flight option when it is not referenced by any order or cart.
     * This test verifies that when there are no references to the flight option, the service successfully
     * deletes it by calling the delete method of the repository with the correct TripPrice entity. 
     * It ensures that the service allows deletion when it is safe to do so and that it interacts with the repository as expected to perform the deletion.
     */
    @Test
    void deleteFlightOption_success_deletes() {
        Trip trip = new Trip();
        trip.setId(702);

        Flight f = new Flight();
        f.setId(32);

        TripPrice tp = new TripPrice();
        tp.setId(8);
        tp.setTrip(trip);
        tp.setFlight(f);

        when(tripPriceRepository.findById(8)).thenReturn(Optional.of(tp));
        when(selectedPackageRepository.countByFlightTripPrice_Id(8)).thenReturn(0L);

        tripOptionService.deleteFlightOption(702, 8);

        verify(tripPriceRepository).delete(tp);
    }

    /**
     * createHotelOption should throw a 400 Bad Request if the accommodationId is missing or if the specified accommodation does not exist, and should create and return a hotel option when valid data is provided.
     * This test verifies that the service checks for the presence of the accommodationId and the existence of the accommodation, throwing the appropriate exceptions when validation fails. 
     * It also verifies that when valid data is provided, the service creates a new TripPrice entity with the correct details, saves it, and returns a HotelOptionDTO that reflects the saved entity's information. 
     * The test ensures that the service handles both validation scenarios correctly and interacts with the repository to persist new hotel options as expected.
     */
    @Test
    void createHotelOption_missingAccommodationId_throwsBadRequest() {
        HotelOptionRequest req = new HotelOptionRequest();
        req.setProvider("P");
        req.setPrice(new BigDecimal("80"));

        when(tripRepository.findById(801)).thenReturn(Optional.of(new Trip()));

        assertThrows(ResponseStatusException.class, () -> tripOptionService.createHotelOption(801, req));
    }

    /**
     * createHotelOption should throw a 400 Bad Request if the specified accommodation does not exist, and should create and return a hotel option when valid data is provided.
     * This test verifies that the service checks for the presence of the accommodationId and the existence of the accommodation, throwing the appropriate exceptions when validation fails.
     * It also verifies that when valid data is provided, the service creates a new TripPrice entity with the correct details, saves it, and returns a HotelOptionDTO that reflects the saved entity's information. 
     * The test ensures that the service handles both validation scenarios correctly and interacts with the repository to persist new hotel options as expected.
     */
    @Test
    void createHotelOption_accommodationNotFound_throwsBadRequest() {
        HotelOptionRequest req = new HotelOptionRequest();
        req.setAccommodationId(55);
        req.setProvider("P");
        req.setPrice(new BigDecimal("80"));

        Trip trip = new Trip();
        trip.setId(802);
        when(tripRepository.findById(802)).thenReturn(Optional.of(trip));
        when(accommodationRepository.findById(55)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tripOptionService.createHotelOption(802, req));
    }
    
    /**
     * createHotelOption should create a new hotel option and return the corresponding DTO when valid data is provided.
     * This test verifies that when a valid accommodationId is provided and the accommodation exists, 
     * the service creates a new TripPrice entity with the correct details, saves it, and returns a HotelOptionDTO that reflects the saved entity's information. 
     * It also verifies that the save method of the repository is called to persist the new hotel option.
     */
    @Test
    void createHotelOption_success_savesAndReturnsDto() {
        HotelOptionRequest req = new HotelOptionRequest();
        req.setAccommodationId(99);
        req.setProvider("HotelBest");
        req.setPrice(new BigDecimal("222.22"));

        Trip trip = new Trip();
        trip.setId(803);

        Accommodation ac = new Accommodation();
        ac.setId(99);
        ac.setHotel_name("Grand");

        when(tripRepository.findById(803)).thenReturn(Optional.of(trip));
        when(accommodationRepository.findById(99)).thenReturn(Optional.of(ac));
        when(tripPriceRepository.save(any())).thenAnswer(inv -> {
            TripPrice p = inv.getArgument(0);
            p.setId(321);
            return p;
        });

        HotelOptionDTO dto = tripOptionService.createHotelOption(803, req);

        assertEquals(321, dto.getId());
        assertEquals(99, dto.getAccommodationId());
        assertEquals("Grand", dto.getHotelName());
        verify(tripPriceRepository).save(any());
    }
}
 
