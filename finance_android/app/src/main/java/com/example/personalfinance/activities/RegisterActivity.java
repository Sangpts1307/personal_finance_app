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
 * Màn hình đăng ký tài khoản mới.
 * Nhập fullName + email + password + confirm → Firebase Auth → gọi backend → vào MainActivity.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvError, tvGoToLogin;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ẩn ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Bind views
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvError = findViewById(R.id.tvError);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
        progressBar = findViewById(R.id.progressBar);

        // ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe kết quả
        authViewModel.getUserLiveData().observe(this, user -> {
            // Đăng ký thành công → chuyển sang MainActivity
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
            btnRegister.setEnabled(!isLoading);
        });

        // Nút đăng ký
        btnRegister.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Validate
            if (fullName.isEmpty()) {
                edtFullName.setError("Please enter your name");
                edtFullName.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                edtEmail.setError("Please enter email");
                edtEmail.requestFocus();
                return;
            }
            if (password.isEmpty() || password.length() < 6) {
                edtPassword.setError("Password must be at least 6 characters");
                edtPassword.requestFocus();
                return;
            }
            if (!password.equals(confirmPassword)) {
                edtConfirmPassword.setError("Passwords do not match");
                edtConfirmPassword.requestFocus();
                return;
            }

            tvError.setVisibility(View.GONE);
            authViewModel.register(email, password, fullName);
        });

        // Quay lại đăng nhập
        tvGoToLogin.setOnClickListener(v -> {
            finish(); // Quay lại LoginActivity
        });
    }
}
