package com.example.carsearch.service;

import com.example.carsearch.dto.CarDTO;
import com.example.carsearch.dto.CarSearchCriteria;
import com.example.carsearch.entity.CarEntity;
import com.example.carsearch.mapper.CarMapper;
import com.example.carsearch.util.QueryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CarSearchService {
    private final EntityManager entityManager;
    private final CarMapper carMapper;

    public CarSearchService(EntityManager entityManager, CarMapper carMapper) {
        this.entityManager = entityManager;
        this.carMapper = carMapper;
    }

    @Cacheable(value = "carSearches", key = "#criteria.hashCode() + #pageable.hashCode()")
    public Page<CarDTO> searchCars(CarSearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CarEntity> query = cb.createQuery(CarEntity.class);
        Root<CarEntity> car = query.from(CarEntity.class);

        List<Predicate> predicates = buildPredicates(criteria, cb, car);
        query.where(predicates.toArray(new Predicate[0]));

        // Apply sorting
        if (pageable.getSort().isSorted()) {
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), car, cb));
        }

        // Create count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CarEntity> countRoot = countQuery.from(CarEntity.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(buildPredicates(criteria, cb, countRoot).toArray(new Predicate[0]));

        // Execute queries
        TypedQuery<CarEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<CarEntity> cars = typedQuery.getResultList();
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(
                cars.stream()
                        .map(carMapper::carEntityToCarDTO)
                        .toList(),
                pageable,
                total);
    }

    private List<Predicate> buildPredicates(CarSearchCriteria criteria,
            CriteriaBuilder cb,
            Root<CarEntity> car) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getColor() != null) {
            predicates.add(cb.equal(car.get("color"), criteria.getColor()));
        }

        if (criteria.getModel() != null) {
            predicates.add(cb.like(cb.lower(car.get("model")),
                    "%" + criteria.getModel().toLowerCase() + "%"));
        }

        if (criteria.getMinWeightKg() != null) {
            predicates.add(cb.greaterThanOrEqualTo(car.get("weightKg"),
                    criteria.getMinWeightKg()));
        }

        if (criteria.getMaxWeightKg() != null) {
            predicates.add(cb.lessThanOrEqualTo(car.get("weightKg"),
                    criteria.getMaxWeightKg()));
        }

        if (criteria.getMinLengthCm() != null) {
            predicates.add(cb.greaterThanOrEqualTo(car.get("lengthCm"),
                    criteria.getMinLengthCm()));
        }

        if (criteria.getMaxLengthCm() != null) {
            predicates.add(cb.lessThanOrEqualTo(car.get("lengthCm"),
                    criteria.getMaxLengthCm()));
        }

        if (criteria.getMinMaxVelocityKmH() != null) {
            predicates.add(cb.greaterThanOrEqualTo(car.get("maxVelocityKmH"),
                    criteria.getMinMaxVelocityKmH()));
        }

        if (criteria.getMaxMaxVelocityKmH() != null) {
            predicates.add(cb.lessThanOrEqualTo(car.get("maxVelocityKmH"),
                    criteria.getMaxMaxVelocityKmH()));
        }

        return predicates;
    }

    @Cacheable(value = "carSearches", key = "'weight-' + #minWeight + '-' + #maxWeight + '-' + #pageable.hashCode()")
    public Page<CarDTO> findByWeightRange(Integer minWeight, Integer maxWeight, Pageable pageable) {
        CarSearchCriteria criteria = new CarSearchCriteria();
        criteria.setMinWeightKg(minWeight);
        criteria.setMaxWeightKg(maxWeight);
        return searchCars(criteria, pageable);
    }
}