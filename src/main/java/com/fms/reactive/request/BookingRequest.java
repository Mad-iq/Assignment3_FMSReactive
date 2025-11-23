package com.fms.reactive.request;

import java.util.List;

import com.fms.reactive.model.MealStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BookingRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String email;

    @Min(value = 1)
    private int seats;

    @NotEmpty
    private List<PassengerRequest> passengers;

    @NotNull
    private MealStatus mealStatus;

    @NotEmpty
    private List<String> seatNumbers;
}
