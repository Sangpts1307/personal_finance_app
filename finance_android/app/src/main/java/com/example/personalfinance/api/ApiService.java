package com.example.personalfinance.api;

import com.example.personalfinance.models.ApiResponse;
import com.example.personalfinance.models.LoginRequest;
import com.example.personalfinance.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Khai báo tất cả API endpoints.
 * Sẽ bổ sung thêm ở các Phase sau.
 */
public interface ApiService {

    // === Phase 2: Auth ===

    @POST("api/auth/login")
    Call<ApiResponse<User>> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<ApiResponse<User>> register(@Body LoginRequest request);

    @GET("api/users/me")
    Call<ApiResponse<User>> getProfile();

    // === Các endpoint sẽ được thêm dần ở các Phase tiếp theo ===

}
