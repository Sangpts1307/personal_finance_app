package com.example.personalfinance.repositories;

import android.content.Context;
import android.util.Log;

import com.example.personalfinance.api.ApiService;
import com.example.personalfinance.api.RetrofitClient;
import com.example.personalfinance.models.ApiResponse;
import com.example.personalfinance.models.LoginRequest;
import com.example.personalfinance.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository gọi API backend cho các thao tác Auth.
 * Đóng vai trò trung gian giữa ViewModel và ApiService.
 */
public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private final ApiService apiService;

    public AuthRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    /**
     * Callback interface cho kết quả gọi API.
     */
    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    /**
     * Gọi API đăng nhập: POST /api/auth/login
     */
    public void login(String idToken, String email, AuthCallback callback) {
        LoginRequest request = new LoginRequest(idToken, email, null);

        apiService.login(request).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Lỗi đăng nhập";
                    callback.onFailure(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Log.e(TAG, "Login API error: " + t.getMessage());
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Gọi API đăng ký: POST /api/auth/register
     */
    public void register(String idToken, String email, String fullName, AuthCallback callback) {
        LoginRequest request = new LoginRequest(idToken, email, fullName);

        apiService.register(request).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "Lỗi đăng ký";
                    callback.onFailure(msg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Log.e(TAG, "Register API error: " + t.getMessage());
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
