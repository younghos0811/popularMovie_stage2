package com.younghos0811.github.popular_movies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.younghos0811.github.popular_movies.data.Movie;
import com.younghos0811.github.popular_movies.data.Review;
import com.younghos0811.github.popular_movies.data.ReviewResult;
import com.younghos0811.github.popular_movies.data.Video;
import com.younghos0811.github.popular_movies.data.VideoResult;
import com.younghos0811.github.popular_movies.data.database.MovieDatabase;
import com.younghos0811.github.popular_movies.utiles.excutor.AppExecutor;
import com.younghos0811.github.popular_movies.utiles.network.ApiService;
import com.younghos0811.github.popular_movies.utiles.network.MovieInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {
    final private String API_KEY;

    private Movie mMovie;
    private LiveData<Movie> mFavoriteMovie;
    private MovieInterface mMovieInterface = ApiService.getService();
    private MutableLiveData<List<Review>> mReviewList;
    private MutableLiveData<List<Video>> mVideoList;
    private MovieDatabase mMovieDatabase;



    public DetailViewModel(MovieDatabase movieDatabase,Movie movie,String apiKey) {
        this.mReviewList = new MutableLiveData<>();
        this.mVideoList = new MutableLiveData<>();
        this.mMovie = movie;
        this.API_KEY =apiKey;
        this.mMovieDatabase = movieDatabase;

        if(movie != null) {
            this.mFavoriteMovie = movieDatabase.movieDao().loadMovieById(movie.getId());
            loadVideo(movie.getId());
            loadReview(movie.getId());
        }
    }


    public LiveData<List<Review>> getReviewList() {
        return mReviewList;
    }


    public LiveData<List<Video>> getVideoList() {
        return mVideoList;
    }


    public LiveData<Movie> getFavoriteMovie() {
        return mFavoriteMovie;
    }


    /**
     * @Method
     * Load Review Data From Network Using Retrofit2
     *
     * @param movieId
     */
    public void loadReview(String movieId) {
        Call<ReviewResult> reviewCall = mMovieInterface.doGetMovieReviewList(movieId , API_KEY);

        reviewCall.enqueue(new Callback<ReviewResult>() {

            @Override
            public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                if (response.isSuccessful()) {
                    mReviewList.setValue(response.body().getReviewList());
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<ReviewResult> call, Throwable t) {
            }

        });
    }


    /**
     * @Method
     * Load Video Data From Network Using Retrofit2
     *
     * @param movieId
     */
    public void loadVideo(String movieId) {
        Call<VideoResult> videoCall = mMovieInterface.doGetMovieVideoList(movieId , API_KEY);

        videoCall.enqueue(new Callback<VideoResult>() {

            @Override
            public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
                if (response.isSuccessful()) {
                    mVideoList.setValue(response.body().getVideoList());
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<VideoResult> call, Throwable t) {
            }
        });
    }


    /**
     * @Method
     * Insert Favorite Movie Using Room
     */
    public void insertFavorite() {

        AppExecutor.getInstance().diskIO().execute(() ->
                mMovieDatabase.movieDao().insertMovie(mMovie.newMovie()));
    }


    /**
     * @Method
     * Delete Favorite Movie Using Room
     */
    public void deleteFavorite() {
        AppExecutor.getInstance().diskIO().execute(() ->
                mMovieDatabase.movieDao().deleteMovie(mFavoriteMovie.getValue()));
    }

}
