package com.example.carsearch.controller;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.service.CarService;
import com.example.carsearch.service.CarSearchService;
import com.example.carsearch.criteria.SearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Validated
@Tag(name = "Car Management", description = "APIs for managing car information")
public class CarController {
    private final CarService carService;
    private final CarSearchService carSearchService;

    public CarController(CarService carService, CarSearchService carSearchService) {
        this.carService = carService;
        this.carSearchService = carSearchService;
    }

    @Operation(summary = "Create a new car", description = "Creates a new car entry with the provided details")
    @ApiResponse(responseCode = "201", description = "Car created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody CarDTO carDTO) {
        CarDTO savedCar = carService.save(carDTO);
        return ResponseEntity.status(201).body(savedCar);
    }

    @Operation(summary = "Get car by ID", description = "Retrieves car details by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Car found")
    @ApiResponse(responseCode = "404", description = "Car not found")
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(
            @Parameter(description = "Car ID", required = true) @PathVariable @Min(1) Long id) {
        return carService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search cars", description = "Search cars based on various criteria with pagination")
    @ApiResponse(responseCode = "200", description = "Search results retrieved")
    @GetMapping("/search")
    public ResponseEntity<Page<CarDTO>> searchCars(
            @Valid @ModelAttribute SearchCriteria criteria,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<CarDTO> results = carSearchService.searchCars(criteria, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get cars by color", description = "Retrieves all cars of a specific color")
    @GetMapping("/by-color/{color}")
    public ResponseEntity<List<CarDTO>> getCarsByColor(
            @Parameter(description = "Car color", required = true) @PathVariable String color,
            @PageableDefault(size = 20) Pageable pageable) {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setColor(color);
        Page<CarDTO> results = carSearchService.searchCars(criteria, pageable);
        return ResponseEntity.ok(results.getContent());
    }

    @Operation(summary = "Get cars by weight range")
    @GetMapping("/by-weight")
    public ResponseEntity<List<CarDTO>> getCarsByWeightRange(
            @RequestParam @Min(1) @Max(10000) Integer minWeight,
            @RequestParam @Min(1) @Max(10000) Integer maxWeight,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CarDTO> results = carSearchService.findByWeightRange(minWeight, maxWeight, pageable);
        return ResponseEntity.ok(results.getContent());
    }
}