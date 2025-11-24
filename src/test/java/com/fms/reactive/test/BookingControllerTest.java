package com.fms.reactive.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fms.reactive.model.Booking;
import com.fms.reactive.model.MealStatus;
import com.fms.reactive.model.Passenger;
import com.fms.reactive.request.BookingRequest;
import com.fms.reactive.request.PassengerRequest;
import com.fms.reactive.service.BookingService;
import com.fms.reactive.service.FlightService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private FlightService flightService;


    @Test
    void testBookTicket() {

        PassengerRequest p = new PassengerRequest(
                "Ravi",
                "M",
                25
        );

        BookingRequest req = new BookingRequest(
                "John Doe",
                "john@mail.com",
                1,
                List.of(p),
                MealStatus.VEG,
                List.of("12A")
        );

        Booking booking = new Booking();
        booking.setPnr("PNR123");

        when(bookingService.bookTicket(any(), any()))
                .thenReturn(Mono.just(booking));

        client.post()
                .uri("/api/flight/booking/F001")
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.pnr").isEqualTo("PNR123");
    }

    @Test
    void testGetTicket() {

        Booking b = new Booking();
        b.setPnr("PNR123");

        when(bookingService.getTicket("PNR123"))
                .thenReturn(Mono.just(b));

        client.get()
                .uri("/api/flight/booking/ticket/PNR123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pnr").isEqualTo("PNR123");
    }

    @Test
    void testGetHistory() {

        Booking b = new Booking();
        b.setEmail("test@mail.com");

        when(bookingService.getHistory("test@mail.com"))
                .thenReturn(Flux.just(b));

        client.get()
                .uri("/api/flight/booking/history/test@mail.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].email").isEqualTo("test@mail.com");
    }
}

