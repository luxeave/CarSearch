package com.example.carsearch.criteria;

public class SearchCriteria {
    private String color;
    private String model;
    private Integer minWeight;
    private Integer maxWeight;
    private Integer minLength;
    private Integer maxLength;
    private Integer minVelocity;
    private Integer maxVelocity;

    // Getters and setters

    public Integer getMinWeight() {
        return minWeight;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Integer getMinVelocity() {
        return minVelocity;
    }

    public Integer getMaxVelocity() {
        return maxVelocity;
    }

    public String getColor() {
        return color;
    }

    public String getModel() {
        return model;
    }

    // Setters
    public void setColor(String color) {
        this.color = color;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public void setMinVelocity(Integer minVelocity) {
        this.minVelocity = minVelocity;
    }

    public void setMaxVelocity(Integer maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
}