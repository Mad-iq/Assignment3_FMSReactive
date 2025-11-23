package com.fms.reactive.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fms.reactive.model.Flight;

import reactor.core.publisher.Flux;

public interface FlightRepo extends ReactiveMongoRepository<Flight, String> {

	 Flux<Flight> findBySourceAndDestinationAndStartTimeBetween(
	            String source,
	            String destination,
	            LocalDateTime start,
	            LocalDateTime end
	    );
}