package com.fms.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fms.reactive.model.Flight;

public interface FlightRepo extends ReactiveMongoRepository<Flight, String> {

}