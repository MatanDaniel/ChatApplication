package com.example.chatapplication.models;

import com.example.chatapplication.LoginResponse;
import com.example.chatapplication.RegisterRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface UserApi {
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Multipart
    @POST("/api/register")
    Call<ResponseBody> registerUser(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part image
    );
    @Multipart
    @POST("/api/profile/upload")
    Call<ResponseBody> uploadProfileImage(
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part image
    );

    @POST("/api/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);


    @POST("/api/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
    @GET("/api/users")
    Call<List<User>> getAllUsers(@Query("currentUserId") Long userId);




}
