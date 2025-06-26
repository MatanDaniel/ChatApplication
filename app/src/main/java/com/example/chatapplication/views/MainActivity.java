package com.example.chatapplication.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityMainBinding;
import com.example.chatapplication.loginpages.LoginActivity;

public class  MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Check if user is logged in
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email == null) {
            // No session → go to login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return; // stop loading the activity
        }
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.toolbarMain.setOverflowIcon(AppCompatResources.getDrawable(this,R.drawable.more_vert));

        mainBinding.toolbarMain.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.editProfileItem){
                Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.signOutItem){
                // Clear any saved login state (if you store it)
                getSharedPreferences("user_session", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else {
                return false;
            }

        });
    }
}

