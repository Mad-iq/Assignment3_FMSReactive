package com.fms.reactive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fms.reactive.model.City;
import com.fms.reactive.model.Flight;
import com.fms.reactive.model.TripStatus;
import com.fms.reactive.repository.AirlineRepo;
import com.fms.reactive.repository.FlightRepo;
import com.fms.reactive.request.AddInventory;
import com.fms.reactive.request.SearchFlightRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepo flightRepository;
    private final AirlineRepo airlineRepository;

    public Mono<Flight> addInventory(AddInventory req) {
    	
    	 City sourceCity = validateCity(req.getSource());
    	 City destinationCity = validateCity(req.getDestination());

        return airlineRepository.existsById(req.getAirlineId())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new RuntimeException("Airline not found with id: " + req.getAirlineId()));
                    }

                    Flight flight = new Flight();
                    flight.setAirlineId(req.getAirlineId());
                    flight.setSource(req.getSource());
                    flight.setDestination(req.getDestination());

                    LocalDateTime startTime = LocalDateTime.parse(req.getStartTime());
                    flight.setStartTime(startTime);
                    flight.setEndTime(startTime.plusHours(2));

                    flight.setPrice(req.getPrice());
                    flight.setTotalSeats(req.getTotalSeats());
                    flight.setAvailableSeats(req.getTotalSeats());

                    return flightRepository.save(flight);
                });
    }
    
    private City validateCity(String value) {
        try {
            return City.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid city");
        }
    }
    
    
    
    public Flux<Flight> searchFlights(SearchFlightRequest req) {

        City sourceCity = validateCity(req.getSource());
        City destinationCity = validateCity(req.getDestination());

        LocalDate journey = LocalDate.parse(req.getJourneyDate());
        LocalDateTime startOfDay = journey.atStartOfDay();
        LocalDateTime endOfDay = journey.atTime(23, 59, 59);

        Flux<Flight> onward = flightRepository
                .findBySourceAndDestinationAndStartTimeBetween(
                        sourceCity.name(),
                        destinationCity.name(),
                        startOfDay,
                        endOfDay
                );

        if (req.getTripStatus() == null || req.getTripStatus() == TripStatus.ONE_WAY) {
            return onward.switchIfEmpty(
                    Flux.error(new RuntimeException("No onward flights found"))
            );
        }

        if (req.getReturnDate() == null || req.getReturnDate().isBlank()) {
            return Flux.error(new IllegalArgumentException(
                    "Return date is required for ROUND_TRIP search"
            ));
        }

        City tempSource = destinationCity;  
        City tempDestination = sourceCity;

        LocalDate returnDate = LocalDate.parse(req.getReturnDate());
        LocalDateTime returnStart = returnDate.atStartOfDay();
        LocalDateTime returnEnd = returnDate.atTime(23, 59, 59);

        Flux<Flight> returnFlights = flightRepository
                .findBySourceAndDestinationAndStartTimeBetween(
                        tempSource.name(),
                        tempDestination.name(),
                        returnStart,
                        returnEnd
                );

        return onward
                .switchIfEmpty(Flux.error(new RuntimeException("No onward flights found")))
                .concatWith(
                        returnFlights.switchIfEmpty(
                                Flux.error(new RuntimeException("No return flights found"))
                        )
                );
    }

}
