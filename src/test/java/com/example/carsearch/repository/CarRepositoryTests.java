package com.example.carsearch.repository;

import com.example.carsearch.entity.CarEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CarRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    private CarEntity testCar;
    private CarEntity savedCar;

    @BeforeEach
    void setUp() {
        // Create a test car entity
        testCar = new CarEntity();
        testCar.setModel("Test Model");
        testCar.setLengthCm(400);
        testCar.setWeightKg(1500);
        testCar.setMaxVelocityKmH(200);
        testCar.setColor("blue");

        // Save it using TestEntityManager
        savedCar = entityManager.persist(testCar);
        entityManager.flush();
    }

    @Test
    void findByIdExisting() {
        Optional<CarEntity> found = carRepository.findById(savedCar.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getModel()).isEqualTo(testCar.getModel());
        assertThat(found.get().getColor()).isEqualTo(testCar.getColor());
    }

    @Test
    void findByIdNonExistent() {
        Optional<CarEntity> found = carRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void saveNewCar() {
        CarEntity newCar = new CarEntity();
        newCar.setModel("New Model");
        newCar.setLengthCm(450);
        newCar.setWeightKg(1600);
        newCar.setMaxVelocityKmH(220);
        newCar.setColor("red");

        CarEntity saved = carRepository.save(newCar);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getModel()).isEqualTo("New Model");
        assertThat(saved.getColor()).isEqualTo("red");
    }

    @Test
    void updateExistingCar() {
        savedCar.setColor("green");
        savedCar.setModel("Updated Model");

        CarEntity updated = carRepository.save(savedCar);

        assertThat(updated.getId()).isEqualTo(savedCar.getId());
        assertThat(updated.getColor()).isEqualTo("green");
        assertThat(updated.getModel()).isEqualTo("Updated Model");
    }

    @Test
    void searchCarsByColor() {
        CarEntity redCar = new CarEntity();
        redCar.setModel("Red Car");
        redCar.setLengthCm(380);
        redCar.setWeightKg(1400);
        redCar.setMaxVelocityKmH(180);
        redCar.setColor("red");
        entityManager.persistAndFlush(redCar);

        Page<CarEntity> redCars = carRepository.searchCars(
                "red", null, null, null, null, null, null, null,
                PageRequest.of(0, 10));

        assertThat(redCars.getContent())
                .extracting(CarEntity::getColor)
                .containsOnly("red");
    }

    @Test
    void findByModelContainingIgnoreCase() {
        // Create additional test cars
        CarEntity sportsCar = new CarEntity();
        sportsCar.setModel("Sports Model X");
        sportsCar.setLengthCm(450);
        sportsCar.setWeightKg(1300);
        sportsCar.setMaxVelocityKmH(250);
        sportsCar.setColor("red");
        entityManager.persistAndFlush(sportsCar);

        CarEntity luxuryCar = new CarEntity();
        luxuryCar.setModel("Luxury MODEL Y");
        luxuryCar.setLengthCm(500);
        luxuryCar.setWeightKg(2000);
        luxuryCar.setMaxVelocityKmH(200);
        luxuryCar.setColor("black");
        entityManager.persistAndFlush(luxuryCar);

        // Test case-insensitive search
        Page<CarEntity> modelResults = carRepository.findByModelContainingIgnoreCase("model", PageRequest.of(0, 10));
        assertThat(modelResults.getContent())
                .extracting(CarEntity::getModel)
                .containsExactlyInAnyOrder("Test Model", "Sports Model X", "Luxury MODEL Y");
    }

    @Test
    void findByWeightBetween() {
        // Create additional test cars with different weights
        CarEntity lightCar = new CarEntity();
        lightCar.setModel("Light Car");
        lightCar.setLengthCm(350);
        lightCar.setWeightKg(1000);
        lightCar.setMaxVelocityKmH(180);
        lightCar.setColor("white");
        entityManager.persistAndFlush(lightCar);

        CarEntity heavyCar = new CarEntity();
        heavyCar.setModel("Heavy Car");
        heavyCar.setLengthCm(500);
        heavyCar.setWeightKg(2500);
        heavyCar.setMaxVelocityKmH(160);
        heavyCar.setColor("black");
        entityManager.persistAndFlush(heavyCar);

        // Test weight range query
        Page<CarEntity> midWeightCars = carRepository.findByWeightBetween(1200, 2000, PageRequest.of(0, 10));
        assertThat(midWeightCars.getContent())
                .extracting(CarEntity::getWeightKg)
                .allSatisfy(weight -> {
                    assertThat(weight).isBetween(1200, 2000);
                });
    }

    @Test
    void findByLengthBetween() {
        // Create additional test cars with different lengths
        CarEntity shortCar = new CarEntity();
        shortCar.setModel("Compact Car");
        shortCar.setLengthCm(300);
        shortCar.setWeightKg(1200);
        shortCar.setMaxVelocityKmH(170);
        shortCar.setColor("silver");
        entityManager.persistAndFlush(shortCar);

        CarEntity longCar = new CarEntity();
        longCar.setModel("Limo");
        longCar.setLengthCm(600);
        longCar.setWeightKg(2200);
        longCar.setMaxVelocityKmH(180);
        longCar.setColor("black");
        entityManager.persistAndFlush(longCar);

        // Test length range query
        Page<CarEntity> midLengthCars = carRepository.findByLengthBetween(350, 500, PageRequest.of(0, 10));
        assertThat(midLengthCars.getContent())
                .extracting(CarEntity::getLengthCm)
                .allSatisfy(length -> {
                    assertThat(length).isBetween(350, 500);
                });
    }

    @Test
    void searchCarsWithMultipleParameters() {
        // Create a variety of cars for testing
        CarEntity fastRedCar = new CarEntity();
        fastRedCar.setModel("Fast Red Car");
        fastRedCar.setLengthCm(450);
        fastRedCar.setWeightKg(1400);
        fastRedCar.setMaxVelocityKmH(280);
        fastRedCar.setColor("red");
        entityManager.persistAndFlush(fastRedCar);

        CarEntity slowBlueCar = new CarEntity();
        slowBlueCar.setModel("Slow Blue Car");
        slowBlueCar.setLengthCm(380);
        slowBlueCar.setWeightKg(1600);
        slowBlueCar.setMaxVelocityKmH(150);
        slowBlueCar.setColor("blue");
        entityManager.persistAndFlush(slowBlueCar);

        // Test complex search with multiple parameters
        Page<CarEntity> searchResults = carRepository.searchCars(
                "red",                  // color
                "Fast",                 // model
                400,                    // minLength
                500,                    // maxLength
                1300,                   // minWeight
                1500,                   // maxWeight
                250,                    // minVelocity
                300,                    // maxVelocity
                PageRequest.of(0, 10)
        );

        assertThat(searchResults.getContent())
                .hasSize(1)
                .first()
                .satisfies(car -> {
                    assertThat(car.getColor()).isEqualTo("red");
                    assertThat(car.getModel()).contains("Fast");
                    assertThat(car.getLengthCm()).isBetween(400, 500);
                    assertThat(car.getWeightKg()).isBetween(1300, 1500);
                    assertThat(car.getMaxVelocityKmH()).isBetween(250, 300);
                });
    }
}
