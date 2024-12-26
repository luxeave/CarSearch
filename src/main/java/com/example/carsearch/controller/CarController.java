package com.example.carsearch.controller;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.service.CarService;
import com.example.carsearch.service.CarSearchService;
import com.example.carsearch.criteria.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final CarSearchService carSearchService;

    public CarController(CarService carService, CarSearchService carSearchService) {
        this.carService = carService;
        this.carSearchService = carSearchService;
    }

    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        CarDTO savedCar = carService.save(carDTO);
        return ResponseEntity.ok(savedCar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
        return carService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CarDTO>> searchCars(
            @ModelAttribute SearchCriteria criteria,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<CarDTO> results = carSearchService.searchCars(criteria, pageable);
        return ResponseEntity.ok(results);
    }
}