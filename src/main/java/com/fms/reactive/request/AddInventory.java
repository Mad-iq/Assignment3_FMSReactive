package com.fms.reactive.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class AddInventory {
	   @NotBlank
	    private String airlineId;

	    @NotBlank
	    private String source;

	    @NotBlank
	    private String destination;
	    
	    @NotBlank
	    private String startTime;

	    @Positive
	    private double price;

	    @Min(value = 1)
	    private int totalSeats;
}
