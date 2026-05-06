package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce_task.API.Authresponse;
import com.example.ecommerce_task.API.RetrofitClient;
import com.example.ecommerce_task.API.TokenManager;
import com.example.ecommerce_task.LoginRegister.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registatrion extends AppCompatActivity {
    private EditText etFullName, etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnCreateAccount;
    private TextView tvLogin;
    private ImageView ivTogglePassword;
    private ProgressBar progressBar;
    private boolean isPasswordVisible = false;
    private TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registatrion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tokenManager  = new TokenManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etFullName        = findViewById(R.id.etFullName);
        etUsername        = findViewById(R.id.etUsername);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount  = findViewById(R.id.btnCreateAccount);
        tvLogin           = findViewById(R.id.btn_login);
        ivTogglePassword  = findViewById(R.id.ivTogglePassword);
        progressBar       = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        ivTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            etPassword.setInputType(isPasswordVisible
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setSelection(etPassword.getText().length());
            ivTogglePassword.setImageResource(isPasswordVisible
                    ? android.R.drawable.ic_menu_view
                    : android.R.drawable.ic_secure);
        });

        btnCreateAccount.setOnClickListener(v -> register());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void register() {
        String fullName  = etFullName.getText().toString().trim();
        String username  = etUsername.getText().toString().trim();
        String email     = etEmail.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();
        String confirmPw = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Enter full name"); etFullName.requestFocus(); return;
        }
        if (username.isEmpty()) {
            etUsername.setError("Enter username"); etUsername.requestFocus(); return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email"); etEmail.requestFocus(); return;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters"); etPassword.requestFocus(); return;
        }
        if (!password.equals(confirmPw)) {
            etConfirmPassword.setError("Passwords do not match"); etConfirmPassword.requestFocus(); return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnCreateAccount.setEnabled(false);

        RetrofitClient.getApi()
                .register(new Register(fullName, username, email, password))
                .enqueue(new Callback<Authresponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Authresponse> call,
                                           @NonNull Response<Authresponse> response) {
                        progressBar.setVisibility(View.GONE);
                        btnCreateAccount.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            String token = response.body().getToken();
                            if (token != null) tokenManager.saveToken(token);
                            Toast.makeText(Registatrion.this, "Account created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registatrion.this, NavigationActivity.class));
                            finish();
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                Toast.makeText(Registatrion.this,
                                        "Registration failed: " + errorBody, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(Registatrion.this,
                                        "Registration failed: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Authresponse> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btnCreateAccount.setEnabled(true);
                        Toast.makeText(Registatrion.this,
                                "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}