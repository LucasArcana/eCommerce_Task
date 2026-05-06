package com.example.ecommerce_task.Cart;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ecommerce_task.Product.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Cartmanager {

    private static final String PREF_NAME  = "cart_prefs";
    private static final String KEY_CART   = "cart_items";

    private static Cartmanager instance;
    private final SharedPreferences prefs;
    private final Gson gson = new Gson();
    private List<CartItem> items;

    private Cartmanager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        items = loadFromPrefs();
    }

    public static Cartmanager getInstance(Context context) {
        if (instance == null) {
            instance = new Cartmanager(context);
        }
        return instance;
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                save();
                return;
            }
        }
        items.add(new CartItem(product, quantity));
        save();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            save();
        }
    }

    public void updateQuantity(int index, int quantity) {
        if (index >= 0 && index < items.size()) {
            if (quantity <= 0) {
                items.remove(index);
            } else {
                items.get(index).setQuantity(quantity);
            }
            save();
        }
    }

    public List<CartItem> getItems()  { return items; }
    public int getItemCount()         { return items.size(); }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) total += item.getTotalPrice();
        return total;
    }

    public void clear() {
        items.clear();
        save();
    }

    private void save() {
        prefs.edit().putString(KEY_CART, gson.toJson(items)).apply();
    }

    private List<CartItem> loadFromPrefs() {
        String json = prefs.getString(KEY_CART, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> loaded = gson.fromJson(json, type);
        return loaded != null ? loaded : new ArrayList<>();
    }
}