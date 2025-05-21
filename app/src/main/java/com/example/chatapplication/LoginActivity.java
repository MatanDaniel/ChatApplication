package com.example.chatapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapplication.databinding.ActivityLoginBinding;

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


            }
        });
    }
}