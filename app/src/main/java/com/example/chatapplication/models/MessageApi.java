package com.example.chatapplication.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageApi {


    //This allows our Android app to send a Message object to the backend via HTTP.
    @POST("/api/messages/send")
    Call<Message> sendMessage(@Body Message message);


    //This allows to fetch chat history between two users from the backend via HTTP
    @GET("/api/messages/history")
    Call<List<Message>> getMessages(
            @Query("user1") String user1,
            @Query("user2") String user2
    );

    //This will call /messages?senderId=1&receiverId=2 and get the list.
    @GET("messages")
    Call<List<Message>> getConversation(
            @Query("senderId") Long senderId,
            @Query("receiverId") Long receiverId
    );


}
