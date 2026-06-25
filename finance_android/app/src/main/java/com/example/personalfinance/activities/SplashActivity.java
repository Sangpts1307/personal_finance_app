package com.example.personalfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalfinance.MainActivity;
import com.example.personalfinance.R;
import com.example.personalfinance.utils.SharedPrefManager;

/**
 * Màn hình Splash: Kiểm tra trạng thái đăng nhập.
 * - Nếu đã đăng nhập → chuyển sang MainActivity
 * - Nếu chưa → chuyển sang LoginActivity
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 1500; // 1.5 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Ẩn ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPrefManager prefManager = new SharedPrefManager(this);

            Intent intent;
            if (prefManager.isLoggedIn()) {
                // Đã đăng nhập → vào màn hình chính
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Chưa đăng nhập → vào màn hình Login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish(); // Không cho quay lại Splash
        }, SPLASH_DELAY);
    }
}
