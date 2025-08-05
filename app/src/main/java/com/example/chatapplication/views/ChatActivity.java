package com.example.chatapplication.views;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.R;
import com.example.chatapplication.adapters.MessageAdapter;
import com.example.chatapplication.databinding.ActivityChatBinding;
import com.example.chatapplication.models.ApiClient;
import com.example.chatapplication.models.Message;
import com.example.chatapplication.models.MessageApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;


    ActivityChatBinding chatBinding;

    String targetUserName;
    String targetUserId;
    String targetUserImageUrl;
    String currentUserName;
    String currentUserId;
    String message;

    private Handler pollingHandler = new Handler();
    private Runnable pollingRunnable;
    private static final long POLLING_INTERVAL_MS = 3000; // 3 seconds



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());

        //Retrieving the values from MainActivity.
        targetUserId = getIntent().getStringExtra("targetUserId");
        targetUserName = getIntent().getStringExtra("targetUserName");

        targetUserImageUrl = getIntent().getStringExtra("targetUserImageUrl");

        //Handle profile image
        if (targetUserImageUrl == null || targetUserImageUrl.equals("null")) {
            chatBinding.imageViewFriendProfile.setImageResource(R.drawable.default_avatar_background);
        } else {
            Picasso.get().load(targetUserImageUrl).into(chatBinding.imageViewFriendProfile);
        }

        //Retrieve current logged-in user as primitive long
        long currentUserIdLong = getIntent().getLongExtra("currentUserId", -1L);

        //If you want to keep it as String for DB
        currentUserId = String.valueOf(currentUserIdLong);

        currentUserName = getIntent().getStringExtra("currentUserName");

        chatBinding.textViewFriendName.setText(targetUserName);
        if (targetUserImageUrl.equals("null")){
            chatBinding.imageViewFriendProfile.setImageResource(R.drawable.default_avatar_background);
        }
        else{
            Picasso.get().load(targetUserImageUrl).into(chatBinding.imageViewFriendProfile);
        }
        chatBinding.imageViewGoMain.setOnClickListener(v ->{
            finish();
        });
        chatBinding.imageViewSendMessage.setOnClickListener(v ->{

            message = chatBinding.editTextMessage.getText().toString();
            if (message.isEmpty()){
                Toast.makeText(this,"Please write a message",Toast.LENGTH_SHORT).show();
            }
            else{
                sendMessage();
            }
        });

        //This connects the adapter to our XML RecyclerView
        // (@+id/recyclerViewMessage in activity_chat.xml).
        messageAdapter = new MessageAdapter(messageList, currentUserId);
        chatBinding.recycleViewMessage.setAdapter(messageAdapter);
        chatBinding.recycleViewMessage.setLayoutManager(new LinearLayoutManager(this));


        loadConversation();

        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                loadConversation(); // reusing loadConversation every 3 seconds
                pollingHandler.postDelayed(this, POLLING_INTERVAL_MS); // schedule next poll
            }
        };
    }
     // these override methods are part of the Android activity lifecycle —
     // and they control when polling starts/stops based on whether you're inside the chat screen or not.
    @Override
    protected void onResume() {
        super.onResume();
        pollingHandler.post(pollingRunnable); // start polling when activity is visible
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollingHandler.removeCallbacks(pollingRunnable); // stop polling when activity is not visible
    }



    public void sendMessage(){


        MessageApi messageApi = ApiClient.getClient().create(MessageApi.class);

        //Creating a Message object
        Message newMessage = new Message();

        // Filling it with sender ID, receiver ID, content, and timestamp
        newMessage.setSenderId(currentUserId);
        newMessage.setReceiverId(targetUserId);
        newMessage.setContent(message); // this is the message typed
        newMessage.setTimestamp(System.currentTimeMillis());

        //Sending it via Retrofit to your backend using messageApi.sendMessage()
        Call<Message> call = messageApi.sendMessage(newMessage);
        call.enqueue(new Callback<Message>() {
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    chatBinding.editTextMessage.setText(""); // Clear after send
                    // ✅ Add the new message to the list
                    messageList.add(response.body());
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    // ✅ Scroll to bottom
                    chatBinding.recycleViewMessage.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Send failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


    // loadConversation fetches the entire conversation and show it in the RecyclerView
    private void loadConversation() {
        MessageApi messageApi = ApiClient.getClient().create(MessageApi.class);

        Call<List<Message>> call = messageApi.getConversation(
                Long.valueOf(currentUserId),
                Long.valueOf(targetUserId)
        );

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                    chatBinding.recycleViewMessage.scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}