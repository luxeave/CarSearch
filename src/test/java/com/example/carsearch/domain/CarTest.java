package com.example.carsearch.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CarTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Car createValidCar() {
        Car car = new Car();
        car.setId(1L);
        car.setModel("Tesla Model 3");
        car.setLengthCm(500);
        car.setWeightKg(2000);
        car.setMaxVelocityKmH(200);
        car.setColor("red");
        return car;
    }

    @Test
    @DisplayName("Should validate car with valid attributes")
    void validateValidCarObject() {
        Car car = createValidCar();
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations).isEmpty();
    }

    @Nested
    @DisplayName("Model validation tests")
    class ModelValidationTests {
        @Test
        @DisplayName("Should fail validation when model is null")
        void validateNullModel() {
            Car car = createValidCar();
            car.setModel(null);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Model name is required");
        }

        @Test
        @DisplayName("Should fail validation when model is too short")
        void validateShortModel() {
            Car car = createValidCar();
            car.setModel("A");

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Model name must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Should fail validation when model is too long")
        void validateLongModel() {
            Car car = createValidCar();
            car.setModel("A".repeat(101));

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Model name must be between 2 and 100 characters");
        }
    }

    @Nested
    @DisplayName("Length validation tests")
    class LengthValidationTests {
        @Test
        @DisplayName("Should fail validation when length is negative")
        void validateNegativeLength() {
            Car car = createValidCar();
            car.setLengthCm(-1);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Length must be positive");
        }

        @Test
        @DisplayName("Should fail validation when length exceeds maximum")
        void validateExcessiveLength() {
            Car car = createValidCar();
            car.setLengthCm(1001);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Length cannot exceed 1000 cm");
        }
    }

    @Nested
    @DisplayName("Weight validation tests")
    class WeightValidationTests {
        @Test
        @DisplayName("Should fail validation when weight is negative")
        void validateNegativeWeight() {
            Car car = createValidCar();
            car.setWeightKg(-1);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Weight must be positive");
        }

        @Test
        @DisplayName("Should fail validation when weight exceeds maximum")
        void validateExcessiveWeight() {
            Car car = createValidCar();
            car.setWeightKg(10001);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Weight cannot exceed 10000 kg");
        }
    }

    @Nested
    @DisplayName("Velocity validation tests")
    class VelocityValidationTests {
        @Test
        @DisplayName("Should fail validation when velocity is negative")
        void validateNegativeVelocity() {
            Car car = createValidCar();
            car.setMaxVelocityKmH(-1);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Velocity must be positive");
        }

        @Test
        @DisplayName("Should fail validation when velocity exceeds maximum")
        void validateExcessiveVelocity() {
            Car car = createValidCar();
            car.setMaxVelocityKmH(501);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Maximum velocity cannot exceed 500 km/h");
        }
    }

    @Nested
    @DisplayName("Color validation tests")
    class ColorValidationTests {
        @Test
        @DisplayName("Should fail validation when color is null")
        void validateNullColor() {
            Car car = createValidCar();
            car.setColor(null);

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Color is required");
        }

        @Test
        @DisplayName("Should fail validation when color contains numbers")
        void validateColorWithNumbers() {
            Car car = createValidCar();
            car.setColor("red123");

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Color must contain only letters");
        }

        @Test
        @DisplayName("Should fail validation when color contains special characters")
        void validateColorWithSpecialChars() {
            Car car = createValidCar();
            car.setColor("red#blue");

            Set<ConstraintViolation<Car>> violations = validator.validate(car);

            assertThat(violations)
                    .hasSize(1)
                    .element(0)
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("Color must contain only letters");
        }
    }
}