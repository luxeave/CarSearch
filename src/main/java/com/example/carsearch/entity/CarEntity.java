package com.example.carsearch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Model name is required")
    @Size(min = 2, max = 100)
    private String model;

    @Column(nullable = false)
    @Positive
    @Max(value = 1000)
    private Integer lengthCm;

    @Column(nullable = false)
    @Positive
    @Max(value = 10000)
    private Integer weightKg;

    @Column(nullable = false)
    @Positive
    @Max(value = 500)
    private Integer maxVelocityKmH;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String color;

    // Add audit fields
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}