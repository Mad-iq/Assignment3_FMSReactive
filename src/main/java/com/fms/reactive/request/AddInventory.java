package com.fms.reactive.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
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
