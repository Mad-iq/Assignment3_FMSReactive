package com.fms.reactive.request;

import com.fms.reactive.model.TripStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SearchFlightRequest {

    @NotBlank
    private String source;

    @NotBlank
    private String destination;

    @NotBlank
    private String journeyDate;

    @NotNull
    private TripStatus tripType;
    
    private String returnDate;
}
