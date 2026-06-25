package com.example.financebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO truyền tải thông tin user ra ngoài API.
 * Không chứa các field nhạy cảm như passwordHash, firebaseUid.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String authProvider;

    /**
     * Chuyển đổi từ Entity sang DTO.
     */
    public static UserDTO fromEntity(com.example.financebackend.model.User user) {
        return new UserDTO(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatarUrl(),
                user.getAuthProvider()
        );
    }
}
