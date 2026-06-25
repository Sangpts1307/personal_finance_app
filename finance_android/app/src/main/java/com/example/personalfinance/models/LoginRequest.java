package com.example.personalfinance.models;

import com.google.gson.annotations.SerializedName;

/**
 * DTO gửi lên backend khi đăng nhập / đăng ký.
 * Mapping với LoginRequest.java phía backend.
 */
public class LoginRequest {

    @SerializedName("idToken")
    private String idToken;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    public LoginRequest(String idToken, String email, String fullName) {
        this.idToken = idToken;
        this.email = email;
        this.fullName = fullName;
    }

    public String getIdToken() { return idToken; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
}
