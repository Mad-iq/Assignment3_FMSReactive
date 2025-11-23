package com.fms.reactive.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PassengerRequest {

	    @NotBlank(message = "Passenger name is required")
	    private String name;

	    @NotBlank(message= "Passenger gender is required")
	    private String gender;

	    @Min(value = 1, message= "Age must be valid")
	    @Max(value = 120, message= "Age must be valid")
	    private int age;
}
