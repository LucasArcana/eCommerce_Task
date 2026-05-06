package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.ecommerce_task.LoginRegister.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edUsername, edPassword;
    private TextView btn_signup;
    private ProgressBar progressBar;
    private Button btnLogin;
    private FrameLayout loadingOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (new TokenManager(this).isLoggedIn()) {
            goToMain();
            return;
        }

        edUsername = findViewById(R.id.user_email);
        edPassword = findViewById(R.id.ed_password);
        progressBar = findViewById(R.id.progress_bar);
        btnLogin = findViewById(R.id.btn_login);
        loadingOverlay = findViewById(R.id.loading_overlay);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Registatrion.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loadingOverlay.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        RetrofitClient.getApi()
                .login(new Login(username, password))
                .enqueue(new Callback<Authresponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Authresponse> call,
                                           @NonNull Response<Authresponse> response) {
                        progressBar.setVisibility(View.GONE);
                        loadingOverlay.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            String token = response.body().getToken();
                            new TokenManager(LoginActivity.this).saveToken(token);

                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            goToMain();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Authresponse> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        loadingOverlay.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);

                        Toast.makeText(LoginActivity.this,
                                "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMain() {
        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }
}