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
                        "(:maxLength IS NULL OR c.lengthCm <= :maxLength) AND " +
                        "(:minWeight IS NULL OR c.weightKg >= :minWeight) AND " +
                        "(:maxWeight IS NULL OR c.weightKg <= :maxWeight) AND " +
                        "(:minVelocity IS NULL OR c.maxVelocityKmH >= :minVelocity) AND " +
                        "(:maxVelocity IS NULL OR c.maxVelocityKmH <= :maxVelocity)")
        Page<CarEntity> searchCars(
                        @Param("color") String color,
                        @Param("model") String model,
                        @Param("minLength") Integer minLength,
                        @Param("maxLength") Integer maxLength,
                        @Param("minWeight") Integer minWeight,
                        @Param("maxWeight") Integer maxWeight,
                        @Param("minVelocity") Integer minVelocity,
                        @Param("maxVelocity") Integer maxVelocity,
                        Pageable pageable);

        // Add custom query for finding by color
        Page<CarEntity> findByColor(String color, Pageable pageable);

        // Add custom query for finding by model containing text
        Page<CarEntity> findByModelContainingIgnoreCase(String model, Pageable pageable);

        // Add custom query for finding by weight range
        @Query("SELECT c FROM CarEntity c WHERE c.weightKg BETWEEN :minWeight AND :maxWeight")
        Page<CarEntity> findByWeightBetween(
                        @Param("minWeight") Integer minWeight,
                        @Param("maxWeight") Integer maxWeight,
                        Pageable pageable);

        // Add custom query for finding by length range
        @Query("SELECT c FROM CarEntity c WHERE c.lengthCm BETWEEN :minLength AND :maxLength")
        Page<CarEntity> findByLengthBetween(
                        @Param("minLength") Integer minLength,
                        @Param("maxLength") Integer maxLength,
                        Pageable pageable);
}