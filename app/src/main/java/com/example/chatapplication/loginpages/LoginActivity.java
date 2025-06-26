package com.example.chatapplication.loginpages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.LoginResponse;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityLoginBinding;
import com.example.chatapplication.models.ApiClient;
import com.example.chatapplication.models.LoginRequest;
import com.example.chatapplication.models.UserApi;
import com.example.chatapplication.views.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        loginBinding.buttonLogin.setOnClickListener(v -> {
            /*When the login button is clicked,
              grab the text from the email input field,
              convert it to a String,
              remove any leading/trailing spaces,
              and store it in a variable called email*/
            String email = loginBinding.editTextEmailLogin.getText().toString().trim();
            String password = loginBinding.editTextPasswordLogin.getText().toString().trim();
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
            } else {
                LoginRequest loginRequest = new LoginRequest(email, password);
                UserApi userApi = ApiClient.getClient().create(UserApi.class);

                userApi.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // ✅ Save session
                            getSharedPreferences("user_session", MODE_PRIVATE)
                                    .edit()
                                    .putString("email", email)
                                    .apply();

                            // ✅ Navigate to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        loginBinding.textViewSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    public void loginClicked(View view){

        EditText emailField = findViewById(R.id.editTextEmailLogin);
        EditText passwordField = findViewById(R.id.editTextPasswordLogin);

        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Check fields are not empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
    }


//    private void performLoginRequest(String email, String password) {
//        UserApi userApi = ApiClient.getClient().create(UserApi.class);
//
//        Call<LoginResponse> call = userApi.login(email, password);
//        call.enqueue(new Callback<LoginResponse>() {
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
//                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                    // Redirect to main activity
//                } else {
//                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}