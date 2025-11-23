package com.fms.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fms.reactive.model.Booking;

import reactor.core.publisher.Flux;

public interface BookingRepo extends ReactiveMongoRepository<Booking, String> {

	Flux<Booking> findByEmail(String email);
}
