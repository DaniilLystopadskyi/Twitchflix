package com.example.twitchflix.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebInterface {
    @GET("/videos")
    Call<List<VideoData>> getVideoCatalog();

    @GET("/videos/{videoId}")
    Call<VideoData> getVideo(@Path("videoId") String id, @Query("ext") String format);

    @POST("/user/new")
    Call<UserData> addUser(@Body UserData user);

    @POST("/user/delete")
    Call<UserData> deleteUser(@Body UserData user);

    @POST("/user/login")
    Call<UserData> loginUser(@Body UserData user);

    @GET("/live")
    Call<List<StreamData>> getStreamCatalog();

    @GET("/live/{streamId}")
    Call<StreamData> getStream(@Path("streamId") String id);

    @POST("/live/start")
    Call<StreamData> goLive(@Query("title") String title, @Query("img") String img, @Body UserData user);

    @POST("/live/stop")
    Call<StreamData> closeStream(@Query("streamId") String streamId, @Body UserData user);
}
