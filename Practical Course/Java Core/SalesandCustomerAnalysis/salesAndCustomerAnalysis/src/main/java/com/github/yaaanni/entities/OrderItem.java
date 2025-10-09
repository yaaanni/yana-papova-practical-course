package com.github.yaaanni.entities;

public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;

    public OrderItem(String productName, int quantity, double price, Category category) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }
    public static OrderItemBuilder builder(){
        return new OrderItemBuilder();
    }
    public static class OrderItemBuilder{
        private String productName;
        private int quantity;
        private double price;
        private Category category;

        public OrderItemBuilder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public OrderItemBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder setPrice(double price) {
            this.price = price;
            return this;
        }

        public OrderItemBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }
        public OrderItem build(){
            return new OrderItem(productName, quantity, price, category);
        }

    }
}
