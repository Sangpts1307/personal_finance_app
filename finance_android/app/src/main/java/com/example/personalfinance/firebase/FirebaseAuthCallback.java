package com.example.personalfinance.firebase;

/**
 * Interface callback cho các thao tác Firebase Auth.
 * Dùng để thông báo kết quả đăng nhập/đăng ký cho ViewModel.
 */
public interface FirebaseAuthCallback {

    /**
     * Gọi khi thao tác thành công.
     * @param idToken Firebase ID Token để gửi lên backend
     */
    void onSuccess(String idToken);

    /**
     * Gọi khi thao tác thất bại.
     * @param errorMessage Thông báo lỗi
     */
    void onFailure(String errorMessage);
}
