package com.fms.reactive.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    private String id;
    private String airlineId;     // reference to Airline.id
    private String source;
    private String destination;
    private LocalDateTime startTime;  
    private LocalDateTime endTime;
    private double price;
    private int totalSeats;
    private int availableSeats;
}
