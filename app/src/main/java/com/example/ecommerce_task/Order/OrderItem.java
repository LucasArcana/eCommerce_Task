package com.example.ecommerce_task.Order;

public class OrderItem {
    private String productName;
    private String productImage;
    private double price;
    private String date;
    private String status;

    public OrderItem(String productName, String productImage, double price, String date, String status) {
        this.productName  = productName;
        this.productImage = productImage;
        this.price        = price;
        this.date         = date;
        this.status       = status;
    }

    public String getProductName()  { return productName; }
    public String getProductImage() { return productImage; }
    public double getPrice()        { return price; }
    public String getDate()         { return date; }
    public String getStatus()       { return status; }
}
