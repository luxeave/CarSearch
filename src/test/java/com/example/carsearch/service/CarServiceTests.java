package com.example.carsearch.service;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.entity.CarEntity;
import com.example.carsearch.exception.ValidationException;
import com.example.carsearch.mapper.CarMapper;
import com.example.carsearch.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTests {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarService carService;

    private CarDTO validCarDTO;
    private CarEntity validCarEntity;

    @BeforeEach
    void setUp() {
        validCarDTO = new CarDTO(1L, "TestModel", 400, 1500, 200, "blue");
        validCarEntity = new CarEntity();
        validCarEntity.setId(1L);
        validCarEntity.setModel("TestModel");
        validCarEntity.setLengthCm(400);
        validCarEntity.setWeightKg(1500);
        validCarEntity.setMaxVelocityKmH(200);
        validCarEntity.setColor("blue");
    }

    @Test
    void findByIdSuccessful() {
        // Arrange
        when(carRepository.findById(1L)).thenReturn(Optional.of(validCarEntity));
        when(carMapper.carEntityToCarDTO(validCarEntity)).thenReturn(validCarDTO);

        // Act
        Optional<CarDTO> result = carService.findById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(validCarDTO);
        verify(carRepository).findById(1L);
        verify(carMapper).carEntityToCarDTO(validCarEntity);
    }

    @Test
    void findByIdNotFound() {
        // Arrange
        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<CarDTO> result = carService.findById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(carRepository).findById(999L);
        verify(carMapper, never()).carEntityToCarDTO(any());
    }

    @Test
    void saveNewCarSuccessful() {
        // Arrange
        CarDTO newCarDTO = new CarDTO(null, "TestModel", 400, 1500, 200, "blue");
        when(carMapper.carDTOToCarEntity(newCarDTO)).thenReturn(validCarEntity);
        when(carRepository.save(any(CarEntity.class))).thenReturn(validCarEntity);
        when(carMapper.carEntityToCarDTO(validCarEntity)).thenReturn(validCarDTO);

        // Act
        CarDTO result = carService.save(newCarDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(carRepository).save(any(CarEntity.class));
        verify(carMapper).carDTOToCarEntity(newCarDTO);
        verify(carMapper).carEntityToCarDTO(validCarEntity);
    }

    @Test
    void saveExistingCarSuccessful() {
        // Arrange
        when(carMapper.carDTOToCarEntity(validCarDTO)).thenReturn(validCarEntity);
        when(carRepository.save(validCarEntity)).thenReturn(validCarEntity);
        when(carMapper.carEntityToCarDTO(validCarEntity)).thenReturn(validCarDTO);

        // Act
        CarDTO result = carService.save(validCarDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validCarDTO.getId());
        verify(carRepository).save(validCarEntity);
    }

    @Test
    void saveInvalidCar() {
        // Arrange
        CarDTO invalidCarDTO = new CarDTO(1L, "TestModel", 400, 15000, 200, "blue"); // Weight > 10000

        // Act & Assert
        assertThatThrownBy(() -> carService.save(invalidCarDTO))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid weight: 15000");
    }
}