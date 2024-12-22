package com.example.carsearch.service;

import com.example.carsearch.domain.Car;
import com.example.carsearch.repository.InMemoryCarRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService {
    private final InMemoryCarRepository carRepository;

    public CarService(InMemoryCarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }
}
