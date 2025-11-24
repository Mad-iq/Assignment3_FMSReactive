package com.fms.reactive.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fms.reactive.model.Booking;
import com.fms.reactive.model.BookingStatus;
import com.fms.reactive.repository.BookingRepo;
import com.fms.reactive.repository.FlightRepo;
import com.fms.reactive.request.BookingRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final FlightRepo flightRepo;
    private final BookingRepo bookingRepo;

    public Mono<Booking> bookTicket(String flightId, BookingRequest req) {

        return flightRepo.findById(flightId)
                .switchIfEmpty(Mono.error(new RuntimeException("Flight not found: " + flightId)))
                .flatMap(flight -> {

                    if (flight.getAvailableSeats() < req.getSeats()) {
                        return Mono.error(new RuntimeException("Not enough seats available"));
                    }

                    String pnr = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                    Booking booking = new Booking();
                    booking.setPnr(pnr);
                    booking.setFlightId(flightId);
                    booking.setName(req.getName());
                    booking.setEmail(req.getEmail());
                    booking.setBookingDate(LocalDateTime.now());
                    booking.setJourneyDate(flight.getStartTime());
                    booking.setPassengers(req.getPassengers().stream()
                            .map(p -> new com.fms.reactive.model.Passenger(
                                    p.getName(),
                                    p.getGender(),
                                    p.getAge()
                            )).toList()
                    );
                    booking.setSeatNumbers(req.getSeatNumbers());
                    booking.setSeatsBooked(req.getSeats());
                    booking.setMealStatus(req.getMealStatus());
                    booking.setBookingStatus(BookingStatus.CONFIRMED);

                    double totalPrice = req.getSeats() * flight.getPrice();
                    booking.setTotalPrice(totalPrice);

                    flight.setAvailableSeats(flight.getAvailableSeats() - req.getSeats());

                    return flightRepo.save(flight)
                            .then(bookingRepo.save(booking));
                });
    }
    
    public Mono<Booking> getTicket(String pnr) {
        return bookingRepo.findById(pnr)
                .switchIfEmpty(Mono.error(new RuntimeException("PNR not found")));
    }

    public Flux<Booking> getHistory(String email) {
        return bookingRepo.findByEmail(email);
    }
    
    public Mono<String> cancelTicket(String pnr) {

        return bookingRepo.findById(pnr)
                .switchIfEmpty(Mono.error(new RuntimeException("PNR not found")))
                .flatMap(booking -> {

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime journey = booking.getJourneyDate();

                    if (now.plusHours(24).isAfter(journey)) {
                        return Mono.error(new RuntimeException(
                                "Ticket cannot be cancelled less than 24 hours before journey"));
                    }

                    booking.setBookingStatus(BookingStatus.CANCELLED);

                    return flightRepo.findById(booking.getFlightId())
                            .flatMap(flight -> {
                                flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatsBooked());
                                return flightRepo.save(flight);
                            })
                            .then(bookingRepo.save(booking))
                            .thenReturn("Ticket cancelled successfully");
                });
    }


}
