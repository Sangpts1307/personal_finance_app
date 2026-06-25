package com.example.financebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO nhận dữ liệu đăng nhập / đăng ký từ Android client.
 * - idToken: Firebase ID Token (bắt buộc)
 * - email: email người dùng
 * - fullName: tên hiển thị (dùng khi đăng ký)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String idToken;
    private String email;
    private String fullName;
}
