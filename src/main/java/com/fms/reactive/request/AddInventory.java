package com.fms.reactive.request;

import com.fms.reactive.model.TripStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddInventory {
	   @NotBlank(message = "Airline ID is required")
	    private String airlineId;

	    @NotBlank(message = "Source is required")
	    private String source;

	    @NotBlank(message = "Destination is required")
	    private String destination;
	    
	    @NotBlank(message = "Starting date and time is required")
	    private String startTime;

	    @Positive(message = "Price must be positive")
	    private double price;

	    @Min(value = 1, message = "Total seats must be atleast 1")
	    private int totalSeats;
}
