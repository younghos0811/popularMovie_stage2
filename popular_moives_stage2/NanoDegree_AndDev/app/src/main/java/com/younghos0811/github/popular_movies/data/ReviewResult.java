package com.younghos0811.github.popular_movies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResult {
    @SerializedName("results")
    private List<Review> reviewList;

    @SerializedName("cod")
    private String code;

    public List<Review> getReviewList() {
        return reviewList;
    }

    public String getCode() {
        return code;
    }
}
