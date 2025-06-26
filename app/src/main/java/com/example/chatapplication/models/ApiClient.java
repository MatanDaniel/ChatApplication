package com.example.chatapplication.models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* What This Does:

    It creates a singleton Retrofit client.

    getClient() returns the initialized Retrofit instance.

    Retrofit uses it to create the UserApi interface.*/
public class ApiClient {

    public static final String BASE_URL = "http://10.0.2.2:8080/api/"; // or your backend IP
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

