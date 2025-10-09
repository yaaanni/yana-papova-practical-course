package com.github.yaaanni.metrics;

import com.github.yaaanni.entities.Order;
import com.github.yaaanni.entities.OrderStatus;

import java.util.List;

public class TotalIncome {
    public static double getTotalIncome(List<Order> orders){
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(o -> o.getItems()
                        .stream()
                        .mapToDouble(i -> i.getPrice()*i.getQuantity())
                        .sum())
                .sum();
    }
}
