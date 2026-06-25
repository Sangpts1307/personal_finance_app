package com.example.financebackend.repository;

import com.example.financebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository truy vấn bảng 'users'.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Tìm user theo Firebase UID (dùng khi verify token).
     */
    Optional<User> findByFirebaseUid(String firebaseUid);

    /**
     * Tìm user theo email (dùng khi kiểm tra trùng tài khoản).
     */
    Optional<User> findByEmail(String email);

    /**
     * Kiểm tra email đã tồn tại chưa.
     */
    boolean existsByEmail(String email);
}
