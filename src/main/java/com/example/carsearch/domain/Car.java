package com.example.carsearch.domain;

import jakarta.validation.constraints.*;

public class Car {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotBlank(message = "Model name is required")
    @Size(min = 2, max = 100, message = "Model name must be between 2 and 100 characters")
    private String model;

    @Positive(message = "Length must be positive")
    @Max(value = 1000, message = "Length cannot exceed 1000 cm")
    private Integer lengthCm;

    @Positive(message = "Weight must be positive")
    @Max(value = 10000, message = "Weight cannot exceed 10000 kg")
    private Integer weightKg;

    @Positive(message = "Velocity must be positive")
    @Max(value = 500, message = "Maximum velocity cannot exceed 500 km/h")
    private Integer maxVelocityKmH;

    @NotBlank(message = "Color is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Color must contain only letters")
    private String color;

    // Constructor, getters, setters, equals, hashCode, and toString methods
}