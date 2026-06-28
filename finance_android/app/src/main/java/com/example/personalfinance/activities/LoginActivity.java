package com.example.personalfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalfinance.MainActivity;
import com.example.personalfinance.R;
import com.example.personalfinance.viewmodels.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Màn hình đăng nhập.
 * Nhập email + password → Firebase Auth → gọi backend → vào MainActivity.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnLoginGoogle, btnLoginFacebook;
    private TextView tvError, tvGoToRegister;
    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ẩn ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Bind views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        tvError = findViewById(R.id.tvError);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        progressBar = findViewById(R.id.progressBar);

        // ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Sinh tự động bởi google-services plugin
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Đăng ký Activity Result Launcher cho Google Sign-In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleGoogleSignInResult(task);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Hủy đăng nhập Google", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Observe kết quả
        authViewModel.getUserLiveData().observe(this, user -> {
            // Đăng nhập thành công → chuyển sang MainActivity
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            tvError.setText(error);
            tvError.setVisibility(View.VISIBLE);
        });

        authViewModel.getLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
            btnLoginGoogle.setEnabled(!isLoading);
            btnLoginFacebook.setEnabled(!isLoading);
        });

        // Nút đăng nhập email/password
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Validate
            if (email.isEmpty()) {
                edtEmail.setError("Please enter email");
                edtEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                edtPassword.setError("Please enter password");
                edtPassword.requestFocus();
                return;
            }

            tvError.setVisibility(View.GONE);
            authViewModel.login(email, password);
        });

        // Nút Google
        btnLoginGoogle.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Nút Facebook (placeholder — sẽ tích hợp sau)
        btnLoginFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Facebook Sign-In coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Chuyển sang màn hình đăng ký
        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String idToken = account.getIdToken();
                // Gửi Google ID Token sang AuthViewModel để đăng nhập Firebase
                authViewModel.loginWithGoogle(idToken);
            }
        } catch (ApiException e) {
            progressBar.setVisibility(View.GONE);
            tvError.setText("Google sign in failed: " + e.getStatusCode());
            tvError.setVisibility(View.VISIBLE);
        }
    }
}
