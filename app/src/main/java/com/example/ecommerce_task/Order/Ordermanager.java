package com.example.ecommerce_task.Order;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ecommerce_task.Cart.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Ordermanager {
    private static final String PREF_NAME = "order_prefs";
    private static final String KEY_ORDERS = "orders";

    private static Ordermanager instance;
    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public Ordermanager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static Ordermanager getInstance(Context context) {
        if (instance == null) instance = new Ordermanager(context);
        return instance;
    }


    public void saveOrdersFromCart(List<CartItem> cartItems) {
        List<OrderItem> existing = getOrders();

        String date = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
                .format(new Date());

        for (CartItem cartItem : cartItems) {
            existing.add(0, new OrderItem(
                    cartItem.getProduct().getName(),
                    cartItem.getProduct().getFirstImage(),
                    cartItem.getProduct().getPrice(),
                    date,
                    "PROCESSING"
            ));
        }

        prefs.edit().putString(KEY_ORDERS, gson.toJson(existing)).apply();
    }

    public List<OrderItem> getOrders() {
        String json = prefs.getString(KEY_ORDERS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<OrderItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void clearOrders() {
        prefs.edit().remove(KEY_ORDERS).apply();
    }
}
