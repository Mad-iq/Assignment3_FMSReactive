package com.fms.reactive.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fms.reactive.model.Flight;
import com.fms.reactive.model.TripStatus;
import com.fms.reactive.request.AddInventory;
import com.fms.reactive.request.SearchFlightRequest;
import com.fms.reactive.service.BookingService;
import com.fms.reactive.service.FlightService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlightControllerTest {

    @Autowired
    private WebTestClient client;

    @Mock
    private FlightService flightService;

    @Mock
    private BookingService bookingService;

    @Test
    void testAddInventory() {

        AddInventory req = new AddInventory(
                "A001",
                "DELHI",
                "MUMBAI",
                "2025-12-01T10:00:00",
                5000,
                100
        );

        Flight flight = new Flight(
                "F001",
                "A001",
                "DELHI",
                "MUMBAI",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                5000,
                100,
                100
        );

        when(flightService.addInventory(any())).thenReturn(Mono.just(flight));

        client.post()
                .uri("/api/flight/add")
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("F001");
    }

    @Test
    void testSearchFlights() {

        SearchFlightRequest req = new SearchFlightRequest(
                "DELHI",
                "MUMBAI",
                "2025-12-01",
                TripStatus.ONE_WAY,
                null
        );

        Flight f = new Flight();
        f.setId("F1");

        when(flightService.searchFlights(any()))
                .thenReturn(Flux.just(f));

        client.post()
                .uri("/api/flight/search")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("F1");
    }
}
