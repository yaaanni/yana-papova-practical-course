package com.github.yaaanni.metrics;

import com.github.yaaanni.entities.Order;
import com.github.yaaanni.entities.OrderStatus;

import java.util.List;

public class AverageCheck {
    public static double getAverageCheck(List<Order> orders){
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(o -> o.getItems().stream()
                        .mapToDouble(i -> i.getQuantity()*i.getPrice())
                        .sum())
                .average()
                .orElseThrow();
    }
}
