package com.fms.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fms.reactive.model.Booking;

public interface BookingRepo extends ReactiveMongoRepository<Booking, String> {

}
