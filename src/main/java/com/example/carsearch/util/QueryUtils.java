package com.example.carsearch.util;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public static List<Order> toOrders(Sort sort, Root<?> root, CriteriaBuilder cb) {
        List<Order> orders = new ArrayList<>();

        sort.forEach(order -> {
            Path<?> path = root.get(order.getProperty());
            orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
        });

        return orders;
    }
}