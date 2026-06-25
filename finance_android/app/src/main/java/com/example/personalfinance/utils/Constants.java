package com.example.personalfinance.utils;

/**
 * Hằng số dùng chung trong toàn bộ ứng dụng Android.
 */
public class Constants {

    /**
     * Base URL của Backend API.
     * - Emulator Android: dùng 10.0.2.2 (ánh xạ tới localhost máy host)
     * - Thiết bị thật: đổi thành IP LAN của máy tính (ví dụ: 192.168.1.x)
     */
    public static final String BASE_URL = "http://localhost:8080/";

    // SharedPreferences keys
    public static final String PREF_NAME = "personal_finance_prefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FIREBASE_TOKEN = "firebase_token";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
}
