package com.example.carsearch.service;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.dto.CarSearchCriteria;
import com.example.carsearch.entity.CarEntity;
import com.example.carsearch.mapper.CarMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarSearchServiceTests {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CarMapper carMapper;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<CarEntity> criteriaQuery;

    // *** Use separate mocks for main-root and count-root ***
    @Mock
    private Root<CarEntity> mainRoot;

    @Mock
    private Root<CarEntity> countRoot;

    @Mock
    private TypedQuery<CarEntity> typedQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private CarSearchService carSearchService;

    private CarDTO carDTO;
    private CarEntity carEntity;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        carDTO = new CarDTO(1L, "TestModel", 400, 1500, 200, "blue");
        carEntity = new CarEntity();
        carEntity.setId(1L);
        carEntity.setModel("TestModel");
        carEntity.setLengthCm(400);
        carEntity.setWeightKg(1500);
        carEntity.setMaxVelocityKmH(200);
        carEntity.setColor("blue");

        pageable = PageRequest.of(0, 10);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(CarEntity.class)).thenReturn(criteriaQuery);

        @SuppressWarnings("unchecked")
        CriteriaQuery<Long> countCriteriaQuery = (CriteriaQuery<Long>) Mockito.mock(CriteriaQuery.class);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countCriteriaQuery);

        // Main query uses mainRoot
        when(criteriaQuery.from(CarEntity.class)).thenReturn(mainRoot);
    }

    @Test
    void searchCarsNoFilters() {
        // Arrange
        CarSearchCriteria criteria = new CarSearchCriteria();
        setupMocksForSearch(List.of(carEntity), 1L);

        // Act
        Page<CarDTO> result = carSearchService.searchCars(criteria, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    void searchCarsWithColor() {
        // Arrange
        CarSearchCriteria criteria = new CarSearchCriteria();
        criteria.setColor("blue");

        Path<String> colorPath = mainRoot.get("color");
        when(criteriaBuilder.equal(colorPath, "blue")).thenReturn(mock(Predicate.class));

        setupMocksForSearch(List.of(carEntity), 1L);

        // Act
        Page<CarDTO> result = carSearchService.searchCars(criteria, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        // We added a color filter, so we expect that stub to be used
        verify(criteriaBuilder, atLeastOnce()).equal(colorPath, "blue");
    }

    @Test
    void searchCarsWithWeightRange() {
        // Arrange
        CarSearchCriteria criteria = new CarSearchCriteria();
        criteria.setMinWeightKg(1000);
        criteria.setMaxWeightKg(2000);

        Path<Integer> weightPath = mainRoot.get("weightKg");
        when(criteriaBuilder.greaterThanOrEqualTo(weightPath, 1000)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.lessThanOrEqualTo(weightPath, 2000)).thenReturn(mock(Predicate.class));

        setupMocksForSearch(List.of(carEntity), 1L);

        // Act
        Page<CarDTO> result = carSearchService.searchCars(criteria, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(criteriaBuilder, atLeastOnce()).greaterThanOrEqualTo(weightPath, 1000);
        verify(criteriaBuilder, atLeastOnce()).lessThanOrEqualTo(weightPath, 2000);
    }

    @Test
    void searchCarsWithMultipleFilters() {
        // Arrange
        CarSearchCriteria criteria = new CarSearchCriteria();
        criteria.setColor("blue");
        criteria.setMinWeightKg(1000);
        criteria.setMaxWeightKg(2000);

        Path<String> colorPath = mainRoot.get("color");
        Path<Integer> weightPath = mainRoot.get("weightKg");

        when(criteriaBuilder.equal(colorPath, "blue")).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.greaterThanOrEqualTo(weightPath, 1000)).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.lessThanOrEqualTo(weightPath, 2000)).thenReturn(mock(Predicate.class));

        setupMocksForSearch(List.of(carEntity), 1L);

        // Act
        Page<CarDTO> result = carSearchService.searchCars(criteria, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(criteriaBuilder, atLeastOnce()).equal(colorPath, "blue");
        verify(criteriaBuilder, atLeastOnce()).greaterThanOrEqualTo(weightPath, 1000);
        verify(criteriaBuilder, atLeastOnce()).lessThanOrEqualTo(weightPath, 2000);
    }

    @Test
    void findByWeightRangeValid() {
        // Arrange
        setupMocksForSearch(List.of(carEntity), 1L);

        // Act
        Page<CarDTO> result = carSearchService.findByWeightRange(1000, 2000, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }

    /**
     * Helper method to mock out the queries and results
     */
    @SuppressWarnings("unchecked")
    private void setupMocksForSearch(List<CarEntity> entities, Long total) {
        // 1) Main query stubs
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(entities);

        // 2) Count query stubs
        // Create a mock CriteriaQuery<Long>
        CriteriaQuery<Long> mockCountCriteriaQuery = (CriteriaQuery<Long>) Mockito.mock(CriteriaQuery.class);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(mockCountCriteriaQuery);

        // The count query uses a different root (countRoot)
        when(mockCountCriteriaQuery.from(CarEntity.class)).thenReturn(countRoot);

        Expression<Long> mockCountExpression = mock(Expression.class);
        when(criteriaBuilder.count(countRoot)).thenReturn(mockCountExpression);

        when(mockCountCriteriaQuery.select(mockCountExpression)).thenReturn(mockCountCriteriaQuery);
        when(entityManager.createQuery(mockCountCriteriaQuery)).thenReturn(countQuery);

        when(countQuery.getSingleResult()).thenReturn(total);

        // 3) Map CarEntities to CarDTO
        when(carMapper.carEntityToCarDTO(any(CarEntity.class))).thenReturn(carDTO);
    }
}
