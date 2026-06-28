package com.example.personalfinance.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.personalfinance.firebase.FirebaseAuthCallback;
import com.example.personalfinance.firebase.FirebaseAuthHelper;
import com.example.personalfinance.models.User;
import com.example.personalfinance.repositories.AuthRepository;
import com.example.personalfinance.utils.SharedPrefManager;

/**
 * ViewModel quản lý trạng thái đăng nhập/đăng ký.
 * Kết nối FirebaseAuthHelper (Firebase SDK) với AuthRepository (Backend API).
 *
 * Luồng: UI → ViewModel → FirebaseAuthHelper → lấy token → AuthRepository → gọi backend
 */
public class AuthViewModel extends AndroidViewModel {

    private final FirebaseAuthHelper firebaseAuthHelper;
    private final AuthRepository authRepository;
    private final SharedPrefManager prefManager;

    // LiveData cho UI observe
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.firebaseAuthHelper = new FirebaseAuthHelper();
        this.authRepository = new AuthRepository(application);
        this.prefManager = new SharedPrefManager(application);
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getLoadingLiveData() { return loadingLiveData; }

    /**
     * Đăng nhập: Firebase Auth → lấy token → gọi backend API
     */
    public void login(String email, String password) {
        loadingLiveData.setValue(true);

        firebaseAuthHelper.login(email, password, new FirebaseAuthCallback() {
            @Override
            public void onSuccess(String idToken) {
                // Có token → gọi backend để đồng bộ user
                authRepository.login(idToken, email, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {
                        // Lưu thông tin vào SharedPreferences
                        prefManager.saveToken(idToken);
                        prefManager.saveUserId(user.getUserId());
                        prefManager.saveUserEmail(user.getEmail());
                        prefManager.saveUserName(user.getFullName() != null ? user.getFullName() : "");
                        prefManager.setLoggedIn(true);

                        loadingLiveData.postValue(false);
                        userLiveData.postValue(user);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        loadingLiveData.postValue(false);
                        errorLiveData.postValue(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    /**
     * Đăng nhập bằng Google: Gửi Google ID Token lên Firebase -> Lấy Firebase ID Token -> Đồng bộ với Backend API
     */
    public void loginWithGoogle(String googleIdToken) {
        loadingLiveData.setValue(true);

        firebaseAuthHelper.loginWithGoogle(googleIdToken, new FirebaseAuthCallback() {
            @Override
            public void onSuccess(String firebaseIdToken) {
                String email = firebaseAuthHelper.getCurrentEmail();
                // Có Firebase ID token -> gọi backend để đồng bộ Google user
                authRepository.login(firebaseIdToken, email, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {
                        // Lưu thông tin vào SharedPreferences
                        prefManager.saveToken(firebaseIdToken);
                        prefManager.saveUserId(user.getUserId());
                        prefManager.saveUserEmail(user.getEmail());
                        prefManager.saveUserName(user.getFullName() != null ? user.getFullName() : "");
                        prefManager.setLoggedIn(true);

                        loadingLiveData.postValue(false);
                        userLiveData.postValue(user);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        loadingLiveData.postValue(false);
                        errorLiveData.postValue(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    /**
     * Đăng ký: Firebase Auth → lấy token → gọi backend API
     */
    public void register(String email, String password, String fullName) {
        loadingLiveData.setValue(true);

        firebaseAuthHelper.register(email, password, new FirebaseAuthCallback() {
            @Override
            public void onSuccess(String idToken) {
                // Có token → gọi backend để tạo user mới trong MySQL
                authRepository.register(idToken, email, fullName, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {
                        prefManager.saveToken(idToken);
                        prefManager.saveUserId(user.getUserId());
                        prefManager.saveUserEmail(user.getEmail());
                        prefManager.saveUserName(user.getFullName() != null ? user.getFullName() : "");
                        prefManager.setLoggedIn(true);

                        loadingLiveData.postValue(false);
                        userLiveData.postValue(user);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        loadingLiveData.postValue(false);
                        errorLiveData.postValue(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(errorMessage);
            }
        });
    }

    /**
     * Đăng xuất: xóa dữ liệu local + Firebase signOut
     */
    public void logout() {
        firebaseAuthHelper.logout();
        prefManager.clear();
        com.example.personalfinance.api.RetrofitClient.reset();
    }
}
