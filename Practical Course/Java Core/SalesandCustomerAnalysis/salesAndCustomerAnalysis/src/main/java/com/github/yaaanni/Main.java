package com.github.yaaanni;

import com.github.yaaanni.entities.*;
import com.github.yaaanni.metrics.TotalIncome;
import com.github.yaaanni.metrics.UniqueCities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order
                .builder()
                .setOrderId("1")
                .setOrderDate(LocalDateTime.parse("2025-10-08T00:00:00"))
                .setCustomer(Customer
                        .builder()
                        .setCustomerId("1")
                        .setName("Nastya")
                        .setEmail("nastya@gmail.com")
                        .setRegisteredAt(LocalDateTime.parse("2025-11-08T00:00:00"))
                        .setAge(19)
                        .setCity("Minsk")
                        .build())
                .setItems(List.of(
                        OrderItem
                                .builder()
                                .setProductName("Chair")
                                .setQuantity(15)
                                .setPrice(200)
                                .setCategory(Category.HOME)
                                .build(),
                        OrderItem
                                .builder()
                                .setProductName("Doll")
                                .setQuantity(20)
                                .setPrice(300)
                                .setCategory(Category.TOYS)
                                .build(),
                        OrderItem
                                .builder()
                                .setProductName("Phone")
                                .setQuantity(2)
                                .setPrice(3000)
                                .setCategory(Category.ELECTRONICS)
                                .build()
                ))
                .setStatus(OrderStatus.NEW)
                .build());

        orders.add(Order
                .builder()
                .setOrderId("1")
                .setOrderDate(LocalDateTime.parse("2025-10-08T00:00:00"))
                .setCustomer(Customer
                        .builder()
                        .setCustomerId("1")
                        .setName("Nastya")
                        .setEmail("nastya@gmail.com")
                        .setRegisteredAt(LocalDateTime.parse("2025-11-08T00:00:00"))
                        .setAge(19)
                        .setCity("Minsk")
                        .build())
                .setItems(List.of(
                        OrderItem
                                .builder()
                                .setProductName("Chair")
                                .setQuantity(15)
                                .setPrice(200)
                                .setCategory(Category.HOME)
                                .build(),
                        OrderItem
                                .builder()
                                .setProductName("Doll")
                                .setQuantity(20)
                                .setPrice(300)
                                .setCategory(Category.TOYS)
                                .build(),
                        OrderItem
                                .builder()
                                .setProductName("Phone")
                                .setQuantity(2)
                                .setPrice(3000)
                                .setCategory(Category.ELECTRONICS)
                                .build()
                ))
                .setStatus(OrderStatus.DELIVERED)
                .build());
        orders.add(Order
                .builder()
                .setOrderId("1")
                .setOrderDate(LocalDateTime.parse("2025-10-08T00:00:00"))
                .setCustomer(Customer
                        .builder()
                        .setCustomerId("1")
                        .setName("Nastya")
                        .setEmail("nastya@gmail.com")
                        .setRegisteredAt(LocalDateTime.parse("2025-11-08T00:00:00"))
                        .setAge(19)
                        .setCity("Moscow")
                        .build())
                .setItems(List.of(
                        OrderItem
                                .builder()
                                .setProductName("Chair")
                                .setQuantity(15)
                                .setPrice(200)
                                .setCategory(Category.HOME)
                                .build(),
                        OrderItem
                                .builder()
                                .setProductName("Doll")
                                .setQuantity(20)
                                .setPrice(300)
                                .setCategory(Category.TOYS)
                                .build(),
                        OrderItem
                                .builder()
                                .setProductName("Phone")
                                .setQuantity(2)
                                .setPrice(3000)
                                .setCategory(Category.ELECTRONICS)
                                .build()
                ))
                .setStatus(OrderStatus.NEW)
                .build());
        System.out.println(UniqueCities.getUniqueCities(orders));
        System.out.println(TotalIncome.getTotalIncome(orders));


    }
}
