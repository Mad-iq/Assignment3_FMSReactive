package com.fms.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fms.reactive.model.Airline;

public interface AirlineRepo extends ReactiveMongoRepository<Airline, String> {

}