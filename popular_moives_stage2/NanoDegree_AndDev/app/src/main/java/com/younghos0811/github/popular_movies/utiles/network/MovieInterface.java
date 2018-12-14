package com.younghos0811.github.popular_movies.utiles.network;

import com.younghos0811.github.popular_movies.data.MovieResult;
import com.younghos0811.github.popular_movies.data.ReviewResult;
import com.younghos0811.github.popular_movies.data.VideoResult;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {

    @GET("{sortType}/")
    Call<MovieResult> doGetMovieList(@Path("sortType") String sortType, @Query("page") int page , @Query("api_key")String apiKey);

    @GET("{id}/videos")
    Call<VideoResult> doGetMovieVideoList(@Path("id") String movieId , @Query("api_key")String apiKey);

    @GET("{id}/reviews")
    Call<ReviewResult> doGetMovieReviewList(@Path("id") String movieId , @Query("api_key")String apiKey);

}
