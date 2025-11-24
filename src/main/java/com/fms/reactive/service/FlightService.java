package com.fms.reactive.service;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.fms.reactive.model.City;
import com.fms.reactive.model.Flight;
import com.fms.reactive.repository.AirlineRepo;
import com.fms.reactive.repository.FlightRepo;
import com.fms.reactive.request.AddInventory;

import lombok.RequiredArgsConstructor;
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
            throw new RuntimeException(
                "Invalid city");
        }
    }

}
