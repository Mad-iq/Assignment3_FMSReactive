package com.fms.reactive.request;

import java.util.List;

import com.fms.reactive.model.MealStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotBlank(message = "User name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Min(value = 1, message = "Atleast 1 seat must be booked" )
    private int seats;

    @NotEmpty(message = "Passenger list cannot be empty")
    private List<PassengerRequest> passengers;

    @NotNull(message = "Meal status is required")
    private MealStatus mealStatus;

    @NotEmpty(message = "Seat numbers cannot be empty")
    private List<String> seatNumbers;
}
