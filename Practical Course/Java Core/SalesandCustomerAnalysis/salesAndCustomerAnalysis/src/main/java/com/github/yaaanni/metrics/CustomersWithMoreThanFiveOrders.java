package com.github.yaaanni.metrics;

import com.github.yaaanni.entities.Customer;
import com.github.yaaanni.entities.Order;

import java.util.List;
import java.util.stream.Collectors;

public class CustomersWithMoreThanFiveOrders {
    public static List<Customer> getCustomersWithMoreThanFiveOrders(List<Order> orders){
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCustomer(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 5)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }
}
