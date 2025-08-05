package com.example.chatapplication.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.adapters.UsersAdapter;
import com.example.chatapplication.databinding.ActivityMainBinding;
import com.example.chatapplication.loginpages.LoginActivity;
import com.example.chatapplication.models.ApiClient;
import com.example.chatapplication.models.User;
import com.example.chatapplication.models.UserApi;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  MainActivity extends AppCompatActivity implements UsersAdapter.OnUserClickListener{
    ActivityMainBinding mainBinding;

    private void retrieveUsersFromDatabase() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        Long currentUserId = prefs.getLong("userId", -1L);  // get current user ID
        UserApi userApi = ApiClient.getClient().create(UserApi.class);
        Call<List<User>> call = userApi.getAllUsers(currentUserId);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    for (User u : users) {
                        Log.d("MainActivity", "User in list: " + u.getUsername());
                    }

                    Log.d("MainActivity", "Fetched users: " + users.size());

                    UsersAdapter adapter = new UsersAdapter(new ArrayList<>(users),MainActivity.this);
                    RecyclerView recyclerView = findViewById(R.id.userRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Failed to fetch users or response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();


            }
        });
    }




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

        retrieveUsersFromDatabase();


        mainBinding.toolbarMain.setOverflowIcon(AppCompatResources.getDrawable(this,R.drawable.more_vert));

        mainBinding.toolbarMain.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.editProfileItem){
                Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.signOutItem){
                // Clear any saved login state (if we store it)
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
    @Override
    public void onUserClicked(User user) {
            Log.d("CLICK_TEST", "onUserClicked CALLED for user: " + user.getUsername());
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);

        //  Target user
        intent.putExtra("targetUserId", String.valueOf(user.getId()));
        intent.putExtra("targetUserName", user.getUsername());
        intent.putExtra("targetUserImageUrl", user.getImageUrl() == null ? "null" : user.getImageUrl());

        //  Current logged in user
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        Long currentUserId = prefs.getLong("userId", -1L);  // Convert Long -> String
        String currentUserName = prefs.getString("username", "");

        Log.d("CURRENT_USER_TEST",
                "====================================\n" +
                        "   LOADED currentUserId FROM PREFS = " + currentUserId + "\n" +
                        "====================================");
        intent.putExtra("currentUserId", currentUserId.longValue());
        intent.putExtra("currentUserName", currentUserName);

        startActivity(intent);
    }
}

