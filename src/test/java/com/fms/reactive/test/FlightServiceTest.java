package com.fms.reactive.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fms.reactive.model.Flight;
import com.fms.reactive.model.TripStatus;
import com.fms.reactive.repository.AirlineRepo;
import com.fms.reactive.repository.FlightRepo;
import com.fms.reactive.request.AddInventory;
import com.fms.reactive.request.SearchFlightRequest;
import com.fms.reactive.service.FlightService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepo flightRepo;

    @Mock
    private AirlineRepo airlineRepo;

    @InjectMocks
    private FlightService flightService;


    @Test
    void addInventory_success() {
        AddInventory req = new AddInventory();
        req.setAirlineId("A001");
        req.setSource("DELHI");
        req.setDestination("MUMBAI");
        req.setStartTime("2025-12-10T10:00:00");
        req.setPrice(5000);
        req.setTotalSeats(100);

        when(airlineRepo.existsById("A001")).thenReturn(Mono.just(true));
        when(flightRepo.save(any())).thenReturn(Mono.just(new Flight()));

        StepVerifier.create(flightService.addInventory(req))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void addInventory_airlineNotFound() {
        AddInventory req = new AddInventory();
        req.setAirlineId("BAD");
        req.setSource("DELHI");
        req.setDestination("MUMBAI");
        req.setStartTime("2025-12-10T10:00:00");
        req.setPrice(5000);
        req.setTotalSeats(50);

        when(airlineRepo.existsById("BAD")).thenReturn(Mono.just(false));

        StepVerifier.create(flightService.addInventory(req))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void addInventory_invalidCity() {

        AddInventory req = new AddInventory();
        req.setAirlineId("A001");
        req.setSource("XYZ");   // invalid
        req.setDestination("MUMBAI");
        req.setStartTime("2025-12-10T10:00:00");
        req.setPrice(5000);
        req.setTotalSeats(100);

        StepVerifier.create(
                Mono.defer(() -> flightService.addInventory(req))
        )
        .expectError(RuntimeException.class)
        .verify();
    }



    @Test
    void searchFlights_oneWay() {
        SearchFlightRequest req = new SearchFlightRequest();
        req.setSource("DELHI");
        req.setDestination("MUMBAI");
        req.setJourneyDate("2025-12-10");
        req.setTripStatus(TripStatus.ONE_WAY);

        when(flightRepo.findBySourceAndDestinationAndStartTimeBetween(
                any(), any(), any(), any()))
                .thenReturn(Flux.just(new Flight()));

        StepVerifier.create(flightService.searchFlights(req))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void searchFlights_roundTrip_missingReturnDate() {
        SearchFlightRequest req = new SearchFlightRequest();
        req.setSource("DELHI");
        req.setDestination("MUMBAI");
        req.setJourneyDate("2025-12-10");
        req.setTripStatus(TripStatus.ROUND_TRIP);

        StepVerifier.create(flightService.searchFlights(req))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

}

