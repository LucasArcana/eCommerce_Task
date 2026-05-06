package com.example.ecommerce_task;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ecommerce_task.API.RetrofitClient;
import com.example.ecommerce_task.API.TokenManager;
import com.example.ecommerce_task.Cart.CartItem;
import com.example.ecommerce_task.Cart.Cartmanager;
import com.example.ecommerce_task.LoginRegister.User;
import com.example.ecommerce_task.Order.Ordermanager;
import com.example.ecommerce_task.Product.Product;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName    = findViewById(R.id.et_name);
        etEmail   = findViewById(R.id.et_email);
        etPhone   = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);

        ImageButton back = findViewById(R.id.btn_back);
        back.setOnClickListener(view -> finish());
        getUserDetail();

        loadOrderSummary();

        MaterialButton btnProceed = findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(v -> handleProceed());
    }

    private void getUserDetail(){
        String token = new TokenManager(this).getToken();

        RetrofitClient.getApi()
                .getProfile(token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call,
                                           @NonNull Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();

                            etName.setText(user.getFullName());
                            etEmail.setText(user.getEmail());
                            etPhone.setText(user.getPhone());
                            etAddress.setText(user.getAddress());
                        }else {
                            try {
                                String errorBody = response.errorBody().string();
                                Toast.makeText(CheckoutActivity.this,
                                        "Code: " + response.code() + " - " + errorBody,
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(CheckoutActivity.this,
                                        "Code: " + response.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Toast.makeText(CheckoutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadOrderSummary() {
        List<CartItem> items = Cartmanager.getInstance(this).getItems();
        if (items.isEmpty()) return;

        CartItem first   = items.get(0);
        Product product = first.getProduct();

        ImageView ivThumb    = findViewById(R.id.iv_product_thumb);
        TextView  tvName     = findViewById(R.id.tv_order_name);
        TextView  tvDetail   = findViewById(R.id.tv_order_detail);
        TextView  tvPrice    = findViewById(R.id.tv_order_price);
        TextView  tvSubtotal = findViewById(R.id.tv_subtotal);
        TextView tvTax      = findViewById(R.id.tv_tax);
        TextView  tvTotal    = findViewById(R.id.tv_total);

        tvName.setText(product.getName());
        tvDetail.setText(product.getType() != null ? product.getType() : "");
        tvPrice.setText(first.getProduct().getFormattedPrice());

        String imageUrl = product.getFirstImage();
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(new ColorDrawable(0xFF70D8F0))
                    .centerCrop()
                    .into(ivThumb);
        }

        double subtotal = Cartmanager.getInstance(this).getSubtotal();
        double tax      = subtotal * 0.07; // 7% tax
        double total    = subtotal + tax;

        tvSubtotal.setText(String.format("$%,.2f", subtotal));
        tvTax.setText(String.format("$%,.2f", tax));
        tvTotal.setText(String.format("$%,.2f", total));

        if (items.size() > 1) {
            tvDetail.setText(tvDetail.getText() + " +" + (items.size() - 1) + " more items");
        }
    }

    private void handleProceed() {
        String name    = etName.getText().toString().trim();
        String email   = etEmail.getText().toString().trim();
        String phone   = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Cartmanager.getInstance(this).getItems().isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        List<CartItem> items = Cartmanager.getInstance(this).getItems();
        Ordermanager.getInstance(this).saveOrdersFromCart(items);

        Cartmanager.getInstance(this).clear();
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}