package com.fms.reactive.request;

import com.fms.reactive.model.TripStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchFlightRequest {

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Journey date is required")
    private String journeyDate;

    @NotNull(message = "Trip status is required")
    private TripStatus tripStatus;
    
    private String returnDate;
}
