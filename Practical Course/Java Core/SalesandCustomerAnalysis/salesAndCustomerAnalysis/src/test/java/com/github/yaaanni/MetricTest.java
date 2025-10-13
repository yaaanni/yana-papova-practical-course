package com.github.yaaanni;

import com.github.yaaanni.entities.*;
import com.github.yaaanni.metrics.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MetricTest {
    private List<Order> orders;
    private List<Order> emptyOrders;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<Order>();
        emptyOrders = new ArrayList<Order>();

        Customer elena = Customer.builder()
                .setCustomerId("1")
                .setName("Elena")
                .setEmail("elena@gmail.com")
                .setRegisteredAt(LocalDateTime.parse("2025-11-08T00:00:00"))
                .setAge(19)
                .setCity("Minsk")
                .build();

        orders.add(Order.builder()
                .setOrderId("10")
                .setOrderDate(LocalDateTime.parse("2025-10-10T00:00:00"))
                .setCustomer(elena)
                .setItems(List.of(
                        OrderItem.builder().setProductName("Doll").setQuantity(3).setPrice(5).setCategory(Category.TOYS).build()
                ))
                .setStatus(OrderStatus.CANCELLED)
                .build());

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
                .setOrderId("9")
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
                .setOrderId("1")
                .setOrderDate(LocalDateTime.parse("2025-10-10T00:00:00"))
                .setCustomer(ivan)
                .setItems(List.of(
                        OrderItem.builder().setProductName("Book").setQuantity(1).setPrice(15).setCategory(Category.BOOKS).build()
                ))
                .setStatus(OrderStatus.CANCELLED)
                .build());

        orders.add(Order.builder()
                .setOrderId("2")
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

    }

    @Test
    void getUniqueCitiesTest() {
        List<String> cities = UniqueCities.getUniqueCities(orders);
        List<String> emptyCities = UniqueCities.getUniqueCities(emptyOrders);
        Assertions.assertEquals(2, cities.size());
        Assertions.assertTrue(cities.contains("Minsk"));
        Assertions.assertTrue(cities.contains("Moscow"));
        Assertions.assertTrue(emptyCities.isEmpty());
    }

    @Test
    void getTotalIncomeTest(){
        double total = TotalIncome.getTotalIncome(orders);
        double emptyTotal = TotalIncome.getTotalIncome(emptyOrders);
        Assertions.assertEquals(30, total);
        Assertions.assertEquals(0, emptyTotal);
    }

    @Test
    void getMostPopularTest(){
        String mostPopular = MostPopular.getMostPopular(orders);
        Assertions.assertEquals("Doll", mostPopular);
        Assertions.assertThrows(NoSuchElementException.class, () -> MostPopular.getMostPopular(emptyOrders));
    }

    @Test
    void getAverageCheckTest(){
        double averageCheck = AverageCheck.getAverageCheck(orders);
        Assertions.assertEquals(8.375, averageCheck);
        Assertions.assertThrows(NoSuchElementException.class, () -> AverageCheck.getAverageCheck(emptyOrders));
    }

    @Test
    void getCustomersWithMoreThanFiveOrdersTest(){
        List<Customer> emptyCustomers = CustomersWithMoreThanFiveOrders
                .getCustomersWithMoreThanFiveOrders(emptyOrders);
        List<Customer> customers = CustomersWithMoreThanFiveOrders
                .getCustomersWithMoreThanFiveOrders(orders);
        Assertions.assertTrue(emptyCustomers.isEmpty());
        Assertions.assertTrue(customers.stream()
                        .anyMatch(c -> "Nastya"
                        .equals(c.getName())));
    }

}
