package com.fms.reactive.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fms.reactive.model.Airline;
import com.fms.reactive.model.Flight;
import com.fms.reactive.repository.AirlineRepo;
import com.fms.reactive.request.AddInventory;
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
    private final AirlineRepo airlineRepository;
    
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Flight> addInventory(@Valid @RequestBody AddInventory req) {
        return flightService.addInventory(req);
    }
    
    @GetMapping("/test-airline/{id}")
    public Mono<Airline> testAirline(@PathVariable String id) {
        return airlineRepository.findById(id)
                .doOnNext(a -> System.out.println("FOUND: " + a))
                .switchIfEmpty(Mono.fromRunnable(() -> System.out.println("NOT FOUND IN SPRING!")));
    }
    
    @GetMapping("/test")
    public Flux<String> test() {
        return airlineRepository.findAll()
            .map(a -> "Found: " + a.getId());
    }



}

