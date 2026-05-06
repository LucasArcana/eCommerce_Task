package com.example.ecommerce_task;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecommerce_task.Cart.Cartmanager;
import com.example.ecommerce_task.Product.ImageSliderAdapter;
import com.example.ecommerce_task.Product.Product;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class ProductDetail extends AppCompatActivity {
    public static final String EXTRA_PRODUCT = "extra_product";
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String productJson = getIntent().getStringExtra(EXTRA_PRODUCT);
        Product product = new Gson().fromJson(productJson, Product.class);

        ViewPager2 vpImages   = findViewById(R.id.vp_images);
        TextView tvBadge    = findViewById(R.id.tv_badge);
        TextView       tvName     = findViewById(R.id.tv_name);
        TextView       tvPrice    = findViewById(R.id.tv_price);
        TextView       tvDesc     = findViewById(R.id.tv_description);
        TextView       tvQty      = findViewById(R.id.tv_quantity);
        MaterialButton btnMinus   = findViewById(R.id.btn_minus);
        MaterialButton btnPlus    = findViewById(R.id.btn_plus);
        MaterialButton btnAddCart = findViewById(R.id.btn_add_cart);
        ImageButton btnBack    = findViewById(R.id.btn_back);

        if (product != null) {
            tvName.setText(product.getName());
            tvPrice.setText(product.getFormattedPrice());
            if (tvDesc != null)     tvDesc.setText(product.getType() + "\n" + product.getMarterial());

            String badge = product.getBadge();
            if (badge != null) {
                tvBadge.setVisibility(View.VISIBLE);
                tvBadge.setText(badge);
            }

            if (product.getImage() != null && !product.getImage().isEmpty()) {
                ImageSliderAdapter adapter = new ImageSliderAdapter(product.getImage());
                vpImages.setAdapter(adapter);
            }
        }

        btnBack.setOnClickListener(v -> finish());

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQty.setText(String.valueOf(quantity));
            }
        });
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQty.setText(String.valueOf(quantity));
        });

        btnAddCart.setOnClickListener(v -> {
            Cartmanager.getInstance(this).addItem(product, quantity);
            Toast.makeText(this, "Added " + quantity + " item(s) to cart!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}