package com.example.chatapplication.models;

import com.example.chatapplication.LoginResponse;
import com.example.chatapplication.RegisterRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import retrofit2.http.Part;


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
    Call<ResponseBody> uploadProfilePicture(
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part image
    );
    //    @Multipart
//    @POST("/api/upload-profile-picture")
//    Call<ResponseBody> uploadProfilePicture(
//            @Part("email") RequestBody email,
//            @Part MultipartBody.Part image
//    );

    @POST("/api/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);


    @POST("/api/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);


//    @POST("register")
//    Call<ResponseBody> registerUser(@Body RegisterRequest request);
//    @POST("/api/register")
//    Call<LoginResponse> registerUser(@Body RegisterRequest request);
}
