package com.example.carsearch.mapper;

import org.mapstruct.Mapper;
import com.example.carsearch.entity.CarEntity;
import com.example.carsearch.dto.CarDTO;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarDTO carEntityToCarDTO(CarEntity car);
    CarEntity carDTOToCarEntity(CarDTO carDTO);
}