package com.example.personalfinance.api;

import com.example.personalfinance.utils.Constants;
import com.example.personalfinance.utils.SharedPrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp Interceptor: Tự động đính kèm Firebase Token
 * vào Header "Authorization: Bearer <token>" cho mọi request.
 */
public class TokenInterceptor implements Interceptor {

    private final SharedPrefManager sharedPrefManager;

    public TokenInterceptor(SharedPrefManager sharedPrefManager) {
        this.sharedPrefManager = sharedPrefManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = sharedPrefManager.getToken();

        if (token != null && !token.isEmpty()) {
            Request authorized = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(authorized);
        }

        return chain.proceed(original);
    }
}
