package com.example.ecommerce_task.Product;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("_id")    private String id;
    @SerializedName("name")   private String name;
    @SerializedName("price")  private double price;
    @SerializedName("image")  private List<String> image;
    @SerializedName("type") private String type;
    @SerializedName("material") private String marterial;
    @SerializedName("stock")  private int stock;
    @SerializedName("sale")   private boolean sale;

    public String getId()           { return id; }
    public String getName()         { return name; }
    public double getPrice()        { return price; }
    public List<String> getImage()  { return image; }
    public String getType() { return type; }
    public String getMarterial() { return marterial.replace(". ", ".\n"); }
    public int getStock()           { return stock; }
    public boolean isSale()         { return sale; }

    public String getFirstImage() {
        return (image != null && !image.isEmpty()) ? image.get(0) : null;
    }
    public String getFormattedPrice() { return String.format("$%,.2f", price); }
    public String getBadge() {
        if (stock < 2) return "Limited";
        if (sale)       return "SALE";
        return null;
    }
}