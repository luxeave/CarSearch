package com.example.carsearch.service;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.entity.CarEntity;
import com.example.carsearch.mapper.CarMapper;
import com.example.carsearch.repository.CarRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    @Cacheable(value = "cars", key = "#id")
    public Optional<CarDTO> findById(Long id) {
        return carRepository.findById(id)
                .map(carMapper::carEntityToCarDTO);
    }

    @CacheEvict(value = "cars", key = "#result.id")
    @Transactional
    public CarDTO save(CarDTO carDTO) {
        CarEntity entity = carMapper.carDTOToCarEntity(carDTO);
        CarEntity saved = carRepository.save(entity);
        return carMapper.carEntityToCarDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<CarDTO> searchCars(
            String color,
            String model,
            Integer minLength,
            Integer maxLength,
            Pageable pageable) {
        return carRepository.searchCars(color, model, minLength, maxLength, pageable)
                .map(carMapper::carEntityToCarDTO);
    }
}
