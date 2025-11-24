package com.fms.reactive.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fms.reactive.model.Flight;
import com.fms.reactive.repository.AirlineRepo;
import com.fms.reactive.request.AddInventory;
import com.fms.reactive.request.SearchFlightRequest;
import com.fms.reactive.service.FlightService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Flight> addInventory(@Valid @RequestBody AddInventory req) {
        return flightService.addInventory(req);
    }
    
    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Flight> searchFlights(@Valid @RequestBody SearchFlightRequest req) {
        return flightService.searchFlights(req);
    }



}

