package com.example.carsearch.dto;

public class CarSearchCriteria {
    private String model;
    private Integer minLengthCm;
    private Integer maxLengthCm;
    private Integer minWeightKg;
    private Integer maxWeightKg;
    private Integer minMaxVelocityKmH;
    private Integer maxMaxVelocityKmH;
    private String color;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMinLengthCm() {
        return minLengthCm;
    }

    public void setMinLengthCm(Integer minLengthCm) {
        this.minLengthCm = minLengthCm;
    }

    public Integer getMaxLengthCm() {
        return maxLengthCm;
    }

    public void setMaxLengthCm(Integer maxLengthCm) {
        this.maxLengthCm = maxLengthCm;
    }

    public Integer getMinWeightKg() {
        return minWeightKg;
    }

    public void setMinWeightKg(Integer minWeightKg) {
        this.minWeightKg = minWeightKg;
    }

    public Integer getMaxWeightKg() {
        return maxWeightKg;
    }

    public void setMaxWeightKg(Integer maxWeightKg) {
        this.maxWeightKg = maxWeightKg;
    }

    public Integer getMinMaxVelocityKmH() {
        return minMaxVelocityKmH;
    }

    public void setMinMaxVelocityKmH(Integer minMaxVelocityKmH) {
        this.minMaxVelocityKmH = minMaxVelocityKmH;
    }

    public Integer getMaxMaxVelocityKmH() {
        return maxMaxVelocityKmH;
    }

    public void setMaxMaxVelocityKmH(Integer maxMaxVelocityKmH) {
        this.maxMaxVelocityKmH = maxMaxVelocityKmH;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
