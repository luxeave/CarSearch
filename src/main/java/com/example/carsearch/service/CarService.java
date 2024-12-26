package com.example.carsearch.service;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.entity.CarEntity;
import com.example.carsearch.mapper.CarMapper;
import com.example.carsearch.repository.CarRepository;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;  
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(
            CarRepository carRepository,
            CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    @Cacheable(value = "cars", key = "#id")
    public Optional<CarDTO> findById(Long id) {
        logger.debug("Fetching car with id: {}", id);
        return carRepository.findById(id)
                .map(carMapper::carEntityToCarDTO);
    }

    @CacheEvict(value = "cars", key = "#result.id")
    @Transactional
    public CarDTO save(CarDTO carDTO) {
        logger.debug("Saving car: {}", carDTO);
        validateCarDTO(carDTO);
        CarEntity entity = carMapper.carDTOToCarEntity(carDTO);
        CarEntity saved = carRepository.save(entity);
        return carMapper.carEntityToCarDTO(saved);
    }

    private void validateCarDTO(CarDTO carDTO) {
        if (carDTO.getWeightKg() <= 0 || carDTO.getWeightKg() > 10000) {
            throw new ValidationException("Invalid weight: " + carDTO.getWeightKg());
        }
        // Add more validation rules
    }
}