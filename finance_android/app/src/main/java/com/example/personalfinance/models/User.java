package com.example.personalfinance.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model user nhận từ API backend.
 * Mapping với UserDTO.java phía backend.
 */
public class User {

    @SerializedName("userId")
    private int userId;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("authProvider")
    private String authProvider;

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getAuthProvider() { return authProvider; }
}
