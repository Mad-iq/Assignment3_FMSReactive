package com.fms.reactive.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fms.reactive.model.Booking;
import com.fms.reactive.model.BookingStatus;
import com.fms.reactive.model.Flight;
import com.fms.reactive.model.MealStatus;
import com.fms.reactive.repository.BookingRepo;
import com.fms.reactive.repository.FlightRepo;
import com.fms.reactive.request.BookingRequest;
import com.fms.reactive.request.PassengerRequest;
import com.fms.reactive.service.BookingService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private FlightRepo flightRepo;

    @Mock
    private BookingRepo bookingRepo;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void bookTicket_success() {
        Flight flight = new Flight();
        flight.setId("F1");
        flight.setAvailableSeats(10);
        flight.setPrice(2000);

        Booking savedBooking = new Booking();  

        BookingRequest req = new BookingRequest();
        req.setName("User");
        req.setEmail("user@mail.com");
        req.setSeats(2);
        req.setMealStatus(MealStatus.VEG);
        req.setSeatNumbers(List.of("1A", "1B"));
        req.setPassengers(List.of(new PassengerRequest("A", "M", 22)));

        when(flightRepo.findById("F1")).thenReturn(Mono.just(flight));

        when(flightRepo.save(any(Flight.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        when(bookingRepo.save(any(Booking.class)))
                .thenReturn(Mono.just(savedBooking));

        StepVerifier.create(bookingService.bookTicket("F1", req))
                .expectNextMatches(b -> b != null)
                .verifyComplete();
    }


    @Test
    void bookTicket_insufficientSeats() {
        Flight flight = new Flight();
        flight.setId("F1");
        flight.setAvailableSeats(1);

        BookingRequest req = new BookingRequest();
        req.setSeats(3);

        when(flightRepo.findById("F1")).thenReturn(Mono.just(flight));

        StepVerifier.create(bookingService.bookTicket("F1", req))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getTicket_success() {
        Booking booking = new Booking();
        booking.setPnr("PNR1");

        when(bookingRepo.findById("PNR1")).thenReturn(Mono.just(booking));

        StepVerifier.create(bookingService.getTicket("PNR1"))
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void getHistory_success() {
        Booking b = new Booking();
        b.setEmail("test@mail.com");

        when(bookingRepo.findByEmail("test@mail.com"))
                .thenReturn(Flux.just(b));

        StepVerifier.create(bookingService.getHistory("test@mail.com"))
                .expectNextCount(1)
                .verifyComplete();
    }
    
    @Test
    void cancelTicket_success() {

        Booking booking = new Booking();
        booking.setPnr("PNR123");
        booking.setFlightId("F1");
        booking.setSeatsBooked(2);
        booking.setJourneyDate(LocalDateTime.now().plusDays(3)); // >24 hrs
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Flight flight = new Flight();
        flight.setId("F1");
        flight.setAvailableSeats(5);

        when(bookingRepo.findById("PNR123")).thenReturn(Mono.just(booking));
        when(flightRepo.findById("F1")).thenReturn(Mono.just(flight));
        when(flightRepo.save(any(Flight.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(bookingRepo.save(any(Booking.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(bookingService.cancelTicket("PNR123"))
                .expectNext("Ticket cancelled successfully")
                .verifyComplete();
    }


}

