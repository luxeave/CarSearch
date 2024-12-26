package com.example.carsearch.repository;

import com.example.carsearch.entity.CarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {

    @Query("SELECT c FROM CarEntity c WHERE " +
            "(:color IS NULL OR c.color = :color) AND " +
            "(:model IS NULL OR c.model LIKE %:model%) AND " +
            "(:minLength IS NULL OR c.lengthCm >= :minLength) AND " +
            "(:maxLength IS NULL OR c.lengthCm <= :maxLength)")
    Page<CarEntity> searchCars(
            @Param("color") String color,
            @Param("model") String model,
            @Param("minLength") Integer minLength,
            @Param("maxLength") Integer maxLength,
            Pageable pageable);
}