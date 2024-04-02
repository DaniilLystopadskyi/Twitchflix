package com.example.twitchflix.network;

import com.example.twitchflix.Data;
import com.google.gson.annotations.SerializedName;

public class VideoData implements Data {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("img")
    private String img;

    @SerializedName("message")
    private String message;

    public VideoData() { }

    public VideoData(String id, String title, String img, String message) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() { return img; }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
