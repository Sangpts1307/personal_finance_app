package com.example.financebackend.controller;

import com.example.financebackend.dto.ApiResponse;
import com.example.financebackend.dto.UserDTO;
import com.example.financebackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller quản lý thông tin người dùng đã đăng nhập.
 * Endpoint: /api/users/** (yêu cầu Firebase Token trong header)
 *
 * Lấy Firebase UID từ SecurityContext (đã được FirebaseAuthFilter đặt vào).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users/me
     * Lấy thông tin profile của user hiện tại.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(Authentication authentication) {
        String firebaseUid = authentication.getName();
        UserDTO userDTO = userService.getUserByFirebaseUid(firebaseUid);
        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }

    /**
     * PUT /api/users/me
     * Cập nhật thông tin cá nhân: fullName, phone, avatarUrl.
     * Body: { "fullName": "...", "phone": "...", "avatarUrl": "..." }
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            Authentication authentication,
            @RequestBody Map<String, String> body) {

        String firebaseUid = authentication.getName();
        String fullName = body.get("fullName");
        String phone = body.get("phone");
        String avatarUrl = body.get("avatarUrl");

        UserDTO updatedUser = userService.updateProfile(firebaseUid, fullName, phone, avatarUrl);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", updatedUser));
    }
}
