package com.github.yaaanni.metrics;

import com.github.yaaanni.entities.Order;

import java.util.List;

public class UniqueCities {
    public static List<String> getUniqueCities(List<Order> orders){
        return  orders.stream()
                .map(c -> c.getCustomer()
                .getCity())
                .distinct()
                .toList();
    }
}
