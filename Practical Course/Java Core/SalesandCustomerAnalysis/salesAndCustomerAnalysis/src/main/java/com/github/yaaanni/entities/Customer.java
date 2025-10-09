package com.github.yaaanni.entities;

import java.time.LocalDateTime;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private LocalDateTime registeredAt;
    private int age;
    private String city;

    public Customer(String customerId, String name, String email, LocalDateTime registeredAt, int age, String city) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
        this.age = age;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public static CustomerBuilder builder(){
        return new CustomerBuilder();
    }
    public static class CustomerBuilder{
        private String customerId;
        private String name;
        private String email;
        private LocalDateTime registeredAt;
        private int age;
        private String city;

        public CustomerBuilder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder setRegisteredAt(LocalDateTime registeredAt) {
            this.registeredAt = registeredAt;
            return this;
        }

        public CustomerBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public CustomerBuilder setCity(String city) {
            this.city = city;
            return this;
        }
        public Customer build(){
            return new Customer(customerId, name, email, registeredAt, age, city);
        }
    }
}
