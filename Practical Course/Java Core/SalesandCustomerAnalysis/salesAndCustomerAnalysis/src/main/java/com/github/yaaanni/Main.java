package com.github.yaaanni;

import com.github.yaaanni.entities.*;
import com.github.yaaanni.metrics.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Order> orders = new ArrayList<Order>();

        Customer elena = Customer.builder()
                .setCustomerId("1")
                .setName("Elena")
                .setEmail("elena@gmail.com")
                .setRegisteredAt(LocalDateTime.parse("2025-11-08T00:00:00"))
                .setAge(19)
                .setCity("Minsk")
                .build();

        Customer ivan = Customer.builder()
                .setCustomerId("2")
                .setName("Ivan")
                .setEmail("ivan@gmail.com")
                .setRegisteredAt(LocalDateTime.parse("2025-09-01T00:00:00"))
                .setAge(25)
                .setCity("Moscow")
                .build();

        Customer nastya = Customer.builder()
                .setCustomerId("3")
                .setName("Nastya")
                .setEmail("nastya@gmail.com")
                .setRegisteredAt(LocalDateTime.parse("2025-01-01T00:00:00"))
                .setAge(22)
                .setCity("Minsk")
                .build();

        orders.add(Order.builder()
                .setOrderId("1")
                .setOrderDate(LocalDateTime.parse("2025-10-08T00:00:00"))
                .setCustomer(elena)
                .setItems(List.of(
                        OrderItem.builder().setProductName("Chair").setQuantity(2).setPrice(10).setCategory(Category.HOME).build(),
                        OrderItem.builder().setProductName("Doll").setQuantity(20).setPrice(1).setCategory(Category.TOYS).build(),
                        OrderItem.builder().setProductName("Phone").setQuantity(2).setPrice(7).setCategory(Category.ELECTRONICS).build()
                ))
                .setStatus(OrderStatus.DELIVERED)
                .build());

        orders.add(Order.builder()
                .setOrderId("2")
                .setOrderDate(LocalDateTime.parse("2025-10-10T00:00:00"))
                .setCustomer(ivan)
                .setItems(List.of(
                        OrderItem.builder().setProductName("Book").setQuantity(1).setPrice(15).setCategory(Category.BOOKS).build()
                ))
                .setStatus(OrderStatus.CANCELLED)
                .build());

        orders.add(Order.builder()
                .setOrderId("3")
                .setOrderDate(LocalDateTime.parse("2025-10-12T00:00:00"))
                .setCustomer(ivan)
                .setItems(List.of(
                        OrderItem.builder().setProductName("Phone").setQuantity(1).setPrice(7).setCategory(Category.ELECTRONICS).build()
                ))
                .setStatus(OrderStatus.DELIVERED)
                .build());

        for (int i = 3; i <= 8; i++) {
            orders.add(Order.builder()
                    .setOrderId(String.valueOf(i))
                    .setOrderDate(LocalDateTime.parse("2025-10-0" + (i + 1) + "T00:00:00"))
                    .setCustomer(nastya)
                    .setItems(List.of(
                            OrderItem.builder().setProductName("Doll").setQuantity(1).setPrice(1).setCategory(Category.TOYS).build()
                    ))
                    .setStatus(OrderStatus.DELIVERED)
                    .build());
        }
        System.out.println(UniqueCities.getUniqueCities(orders));
        System.out.println(TotalIncome.getTotalIncome(orders));
        System.out.println(MostPopular.getMostPopular(orders));
        System.out.println(AverageCheck.getAverageCheck(orders));
        System.out.println(CustomersWithMoreThanFiveOrders.getCustomersWithMoreThanFiveOrders(orders));
    }
}
