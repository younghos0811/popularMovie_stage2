package com.younghos0811.github.popular_movies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResult {

    @SerializedName("results")
    private List<Video> videoList;

    @SerializedName("cod")
    private String code;

    public List<Video> getVideoList() {
        return videoList;
    }

    public String getCode() {
        return code;
    }
}
