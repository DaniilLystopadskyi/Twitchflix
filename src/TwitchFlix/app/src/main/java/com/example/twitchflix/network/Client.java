package com.example.twitchflix.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://35.204.39.122:80";
    private static final String BASE_RTMP = "rtmp://35.204.39.122:1935/live/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getBaseUrl(){
        return BASE_URL;
    }

    public static String getBaseRTMP(){
        return BASE_RTMP;
    }

}
