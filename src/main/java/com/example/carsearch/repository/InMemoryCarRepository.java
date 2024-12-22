package com.example.carsearch.repository;

import java.util.*;
import org.springframework.stereotype.Repository;
import com.example.carsearch.domain.Car;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class InMemoryCarRepository {
    private final Map<Long, Car> cars = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Car save(Car car) {
        if (car.getId() == null) {
            car.setId(idGenerator.incrementAndGet());
        }
        cars.put(car.getId(), car);
        return car;
    }

    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(cars.get(id));
    }

    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    public void deleteById(Long id) {
        cars.remove(id);
    }
}