package com.younghos0811.github.popular_movies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult {

    @SerializedName("results")
    private List<Movie> movieList;

    @SerializedName("cod")
    private String code;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public String getCode() {
        return code;
    }
}
