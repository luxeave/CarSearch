package com.example.carsearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.carsearch.service.CarService;
import com.example.carsearch.domain.Car;
import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.mapper.CarMapper;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final CarMapper carMapper;

    public CarController(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    @PostMapping
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody CarDTO carDTO) {
        Car car = carMapper.carDTOToCar(carDTO);
        Car savedCar = carService.save(car);
        return ResponseEntity.ok(carMapper.carToCarDTO(savedCar));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
        return carService.findById(id)
            .map(car -> ResponseEntity.ok(carMapper.carToCarDTO(car)))
            .orElse(ResponseEntity.notFound().build());
    }
}