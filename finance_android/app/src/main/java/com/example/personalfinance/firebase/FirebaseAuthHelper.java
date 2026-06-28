package com.example.personalfinance.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Helper quản lý đăng ký / đăng nhập Email+Password qua Firebase Auth SDK.
 * Sau khi thao tác thành công, lấy Firebase ID Token để gửi lên backend.
 */
public class FirebaseAuthHelper {

    private static final String TAG = "FirebaseAuthHelper";
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthHelper() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Đăng nhập Firebase bằng tài khoản Google sử dụng Google ID Token.
     */
    public void loginWithGoogle(String googleIdToken, FirebaseAuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        Log.d(TAG, "Firebase login with Google successful");
                        getIdToken(callback);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Đăng nhập Google với Firebase thất bại";
                        Log.e(TAG, "Firebase login with Google failed: " + error);
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Đăng ký tài khoản mới bằng Email + Password.
     */
    public void register(String email, String password, FirebaseAuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        getIdToken(callback);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Đăng ký thất bại";
                        Log.e(TAG, "Register failed: " + error);
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Đăng nhập bằng Email + Password.
     */
    public void login(String email, String password, FirebaseAuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        getIdToken(callback);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Đăng nhập thất bại";
                        Log.e(TAG, "Login failed: " + error);
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Lấy Firebase ID Token từ user hiện tại.
     * Token này sẽ được gửi lên backend để verify.
     */
    public void getIdToken(FirebaseAuthCallback callback) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            callback.onFailure("Chưa đăng nhập");
            return;
        }

        user.getIdToken(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult().getToken();
                        Log.d(TAG, "Got ID Token successfully");
                        callback.onSuccess(token);
                    } else {
                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Không lấy được token";
                        callback.onFailure(error);
                    }
                });
    }

    /**
     * Đăng xuất.
     */
    public void logout() {
        firebaseAuth.signOut();
    }

    /**
     * Kiểm tra user đã đăng nhập chưa.
     */
    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /**
     * Lấy email của user hiện tại.
     */
    public String getCurrentEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
}
