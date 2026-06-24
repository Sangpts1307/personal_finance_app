package com.example.personalfinance.api;

import android.content.Context;

import com.example.personalfinance.utils.Constants;
import com.example.personalfinance.utils.SharedPrefManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton khởi tạo Retrofit client.
 * Tự động đính kèm Firebase token qua TokenInterceptor.
 */
public class RetrofitClient {

    private static Retrofit retrofit;
    private static ApiService apiService;

    private RetrofitClient() {
    }

    public static synchronized ApiService getApiService(Context context) {
        if (apiService == null) {
            SharedPrefManager prefManager = new SharedPrefManager(context.getApplicationContext());

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(prefManager))
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    /**
     * Reset client (gọi khi đổi token hoặc logout).
     */
    public static synchronized void reset() {
        retrofit = null;
        apiService = null;
    }
}
