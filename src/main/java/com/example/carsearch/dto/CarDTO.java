package com.example.carsearch.dto;

public class CarDTO {
    private Long id;
    private String model;
    private Integer lengthCm;
    private Integer weightKg;
    private Integer maxVelocityKmH;
    private String color;

    public CarDTO(Long id, String model, Integer lengthCm, Integer weightKg, Integer maxVelocityKmH, String color) {
        this.id = id;
        this.model = model;
        this.lengthCm = lengthCm;
        this.weightKg = weightKg;
        this.maxVelocityKmH = maxVelocityKmH;
        this.color = color;
    }

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
}