package com.example.personalfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalfinance.MainActivity;
import com.example.personalfinance.R;
import com.example.personalfinance.viewmodels.AuthViewModel;

/**
 * Màn hình đăng nhập.
 * Nhập email + password → Firebase Auth → gọi backend → vào MainActivity.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnLoginGoogle, btnLoginFacebook;
    private TextView tvError, tvGoToRegister;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ẩn ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Bind views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        tvError = findViewById(R.id.tvError);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        progressBar = findViewById(R.id.progressBar);

        // ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe kết quả
        authViewModel.getUserLiveData().observe(this, user -> {
            // Đăng nhập thành công → chuyển sang MainActivity
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            tvError.setText(error);
            tvError.setVisibility(View.VISIBLE);
        });

        authViewModel.getLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
        });

        // Nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Validate
            if (email.isEmpty()) {
                edtEmail.setError("Please enter email");
                edtEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                edtPassword.setError("Please enter password");
                edtPassword.requestFocus();
                return;
            }

            tvError.setVisibility(View.GONE);
            authViewModel.login(email, password);
        });

        // Nút Google (placeholder — sẽ tích hợp sau)
        btnLoginGoogle.setOnClickListener(v -> {
            Toast.makeText(this, "Google Sign-In coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Nút Facebook (placeholder — sẽ tích hợp sau)
        btnLoginFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Facebook Sign-In coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Chuyển sang màn hình đăng ký
        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
