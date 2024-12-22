package com.example.carsearch.mapper;

import org.mapstruct.Mapper;
import com.example.carsearch.domain.Car;
import com.example.carsearch.dto.CarDTO;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarDTO carToCarDTO(Car car);
    Car carDTOToCar(CarDTO carDTO);
}