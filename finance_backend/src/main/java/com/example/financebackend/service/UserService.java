package com.example.financebackend.service;

import com.example.financebackend.dto.UserDTO;
import com.example.financebackend.model.User;
import com.example.financebackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service xử lý nghiệp vụ liên quan đến User.
 * - Đồng bộ Firebase user vào MySQL (tạo mới nếu chưa tồn tại)
 * - Lấy và cập nhật thông tin user
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Đồng bộ user từ Firebase vào MySQL.
     * Nếu user đã tồn tại (theo firebaseUid) → trả về user cũ.
     * Nếu chưa tồn tại → tạo user mới và lưu vào DB.
     */
    @Transactional
    public UserDTO syncFirebaseUser(String firebaseUid, String email, String fullName) {
        Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);

        if (existingUser.isPresent()) {
            logger.info("User đã tồn tại: UID={}", firebaseUid);
            return UserDTO.fromEntity(existingUser.get());
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setFirebaseUid(firebaseUid);
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setAuthProvider("firebase");

        User savedUser = userRepository.save(newUser);
        logger.info("Tạo user mới thành công: UID={}, email={}", firebaseUid, email);

        return UserDTO.fromEntity(savedUser);
    }

    /**
     * Lấy thông tin user theo Firebase UID.
     */
    public UserDTO getUserByFirebaseUid(String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với UID: " + firebaseUid));
        return UserDTO.fromEntity(user);
    }

    /**
     * Cập nhật thông tin cá nhân của user.
     */
    @Transactional
    public UserDTO updateProfile(String firebaseUid, String fullName, String phone, String avatarUrl) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với UID: " + firebaseUid));

        if (fullName != null) user.setFullName(fullName);
        if (phone != null) user.setPhone(phone);
        if (avatarUrl != null) user.setAvatarUrl(avatarUrl);

        User updatedUser = userRepository.save(user);
        logger.info("Cập nhật profile thành công: UID={}", firebaseUid);

        return UserDTO.fromEntity(updatedUser);
    }
}
