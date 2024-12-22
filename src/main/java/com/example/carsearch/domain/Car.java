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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getLengthCm() {
        return lengthCm;
    }

    public void setLengthCm(Integer lengthCm) {
        this.lengthCm = lengthCm;
    }

    public Integer getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Integer weightKg) {
        this.weightKg = weightKg;
    }

    public Integer getMaxVelocityKmH() {
        return maxVelocityKmH;
    }

    public void setMaxVelocityKmH(Integer maxVelocityKmH) {
        this.maxVelocityKmH = maxVelocityKmH;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Constructor, equals, hashCode, and toString methods
}