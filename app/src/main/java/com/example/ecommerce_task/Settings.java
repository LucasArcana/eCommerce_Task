package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce_task.API.TokenManager;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
        }
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupClickListeners() {
        findViewById(R.id.btn_edit_profile).setOnClickListener(v -> 
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show());
        
        findViewById(R.id.btn_change_password).setOnClickListener(v -> 
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show());
            
        findViewById(R.id.btn_privacy_policy).setOnClickListener(v -> 
            Toast.makeText(this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show());
            
        findViewById(R.id.btn_terms).setOnClickListener(v -> 
            Toast.makeText(this, "Terms of Service clicked", Toast.LENGTH_SHORT).show());
            
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            new TokenManager(this).clearToken();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
