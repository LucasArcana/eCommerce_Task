package com.example.ecommerce_task.Order;

import com.example.ecommerce_task.Cart.CartItem;

import java.util.List;

public class Order {
    private String orderId;
    private String date;
    private String status;
    private List<CartItem> items;
    private double total;

    public Order(String orderId, String date, String status, List<CartItem> items, double total) {
        this.orderId = orderId;
        this.date    = date;
        this.status  = status;
        this.items   = items;
        this.total   = total;
    }

    public String getOrderId()       { return orderId; }
    public String getDate()          { return date; }
    public String getStatus()        { return status; }
    public List<CartItem> getItems() { return items; }
    public double getTotal()         { return total; }
    public int getItemCount()        { return items != null ? items.size() : 0; }
    public String getFormattedTotal(){ return String.format("$%,.2f", total); }

    public List<String> getThumbnailUrls() {
        List<String> urls = new java.util.ArrayList<>();
        if (items == null) return urls;
        for (int i = 0; i < Math.min(2, items.size()); i++) {
            String url = items.get(i).getProduct().getFirstImage();
            if (url != null) urls.add(url);
        }
        return urls;
    }
}
