package com.tokoku.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tokoku.app.api.ApiClient;
import com.tokoku.app.api.ApiService;
import com.tokoku.app.databinding.ActivityLoginBinding;
import com.tokoku.app.model.LoginRequest;
import com.tokoku.app.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Kalau sudah login langsung ke MainActivity
        SharedPreferences prefs = getSharedPreferences("tokoku_prefs", MODE_PRIVATE);
        if (!prefs.getString("token", "").isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> doLogin());
        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void doLogin() {
        String email    = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnLogin.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                binding.btnLogin.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    getSharedPreferences("tokoku_prefs", MODE_PRIVATE)
                            .edit()
                            .putString("token", response.body().accessToken)
                            .apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Email atau password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.btnLogin.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,
                        "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}