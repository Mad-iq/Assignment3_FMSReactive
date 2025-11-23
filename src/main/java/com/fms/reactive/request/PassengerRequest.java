package com.fms.reactive.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PassengerRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String gender;

    @Min(value = 1)
    private int age;
}
