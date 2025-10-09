package com.github.yaaanni.metrics;

import com.github.yaaanni.entities.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostPopular {
    public static String getMostPopular(List<Order> orders) {
        return orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(o -> o.getProductName(),
                        Collectors.summingDouble(o -> o.getQuantity())))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey())
                .orElse(null);

    }
}
