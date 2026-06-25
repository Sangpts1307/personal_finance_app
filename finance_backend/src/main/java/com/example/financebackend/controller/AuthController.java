package com.example.financebackend.controller;

import com.example.financebackend.dto.ApiResponse;
import com.example.financebackend.dto.LoginRequest;
import com.example.financebackend.dto.UserDTO;
import com.example.financebackend.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý đăng ký / đăng nhập.
 * Endpoint: /api/auth/** (permitAll, không cần token trong header)
 *
 * Luồng hoạt động:
 * 1. Android gửi Firebase ID Token lên backend
 * 2. Backend verify token qua Firebase Admin SDK
 * 3. Tạo/lấy user trong MySQL
 * 4. Trả về thông tin user
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    public AuthController(FirebaseAuth firebaseAuth, UserService userService) {
        this.firebaseAuth = firebaseAuth;
        this.userService = userService;
    }

    /**
     * POST /api/auth/login
     * Client gửi Firebase ID Token → Backend verify → trả về user info.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody LoginRequest request) {
        try {
            // Verify Firebase ID Token
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(request.getIdToken());
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            // Đồng bộ user vào MySQL (tạo mới nếu chưa có)
            UserDTO userDTO = userService.syncFirebaseUser(uid, email, request.getFullName());

            logger.info("Login thành công: email={}", email);
            return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", userDTO));

        } catch (FirebaseAuthException e) {
            logger.warn("Login thất bại - Token không hợp lệ: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token không hợp lệ: " + e.getMessage()));
        }
    }

    /**
     * POST /api/auth/register
     * Giống login — client đã đăng ký trên Firebase rồi, backend chỉ cần đồng bộ user vào MySQL.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody LoginRequest request) {
        try {
            // Verify Firebase ID Token
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(request.getIdToken());
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String fullName = request.getFullName();

            // Đồng bộ user vào MySQL
            UserDTO userDTO = userService.syncFirebaseUser(uid, email, fullName);

            logger.info("Register thành công: email={}", email);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Đăng ký thành công", userDTO));

        } catch (FirebaseAuthException e) {
            logger.warn("Register thất bại - Token không hợp lệ: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token không hợp lệ: " + e.getMessage()));
        }
    }
}
