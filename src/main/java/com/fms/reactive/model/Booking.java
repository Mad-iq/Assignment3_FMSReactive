package com.fms.reactive.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	    private String pnr;
	    private String flightId;
	    private String name;
	    private String email;
	    private LocalDateTime bookingDate;  
	    private LocalDateTime journeyDate;
	    private List<Passenger> passengers;
	    private List<String> seatNumbers;
	    private int seatsBooked;
	    private MealStatus mealStatus;

}
