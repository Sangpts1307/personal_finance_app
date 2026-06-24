package com.example.personalfinance.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Quản lý lưu trữ cục bộ: token, thông tin user.
 * Dùng SharedPreferences (key-value store đơn giản trên Android).
 */
public class SharedPrefManager {

    private final SharedPreferences prefs;

    public SharedPrefManager(Context context) {
        this.prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    // === Firebase Token ===

    public void saveToken(String token) {
        prefs.edit().putString(Constants.KEY_FIREBASE_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(Constants.KEY_FIREBASE_TOKEN, null);
    }

    // === User Info ===

    public void saveUserId(int userId) {
        prefs.edit().putInt(Constants.KEY_USER_ID, userId).apply();
    }

    public int getUserId() {
        return prefs.getInt(Constants.KEY_USER_ID, -1);
    }

    public void saveUserEmail(String email) {
        prefs.edit().putString(Constants.KEY_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(Constants.KEY_USER_EMAIL, null);
    }

    public void saveUserName(String name) {
        prefs.edit().putString(Constants.KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(Constants.KEY_USER_NAME, "");
    }

    // === Login State ===

    public void setLoggedIn(boolean loggedIn) {
        prefs.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    // === Clear All (Logout) ===

    public void clear() {
        prefs.edit().clear().apply();
    }
}
