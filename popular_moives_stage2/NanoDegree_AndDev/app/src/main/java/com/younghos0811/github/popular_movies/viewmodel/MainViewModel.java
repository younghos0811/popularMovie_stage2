package com.younghos0811.github.popular_movies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Build;

import com.younghos0811.github.popular_movies.BuildConfig;
import com.younghos0811.github.popular_movies.R;
import com.younghos0811.github.popular_movies.data.Movie;
import com.younghos0811.github.popular_movies.data.MovieResult;
import com.younghos0811.github.popular_movies.data.database.MovieDatabase;
import com.younghos0811.github.popular_movies.utiles.network.ApiService;
import com.younghos0811.github.popular_movies.utiles.network.MovieInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private MovieInterface movieInterface = ApiService.getService();
    private MutableLiveData<List<Movie>> movieList;
    private LiveData<List<Movie>> favoriteMovieList;
    private SingleLiveEvent<String> errorData;
    private List<Movie> orginMovieList;
    private Call<MovieResult> call;
    private int pageNum = 1;
    private String sortType;
    private String apiKey;
    private MovieDatabase mMovieDatabase;



    public MainViewModel(Application application) {
        super(application);

        movieList = new MutableLiveData<>();
        orginMovieList = new ArrayList<>();
        errorData = new SingleLiveEvent<>();
        apiKey = BuildConfig.API_KEY;
        sortType = getApplication().getResources().getString(R.string.init_sort_option);
        mMovieDatabase = MovieDatabase.getInstance(getApplication().getApplicationContext());
        favoriteMovieList = mMovieDatabase.movieDao().loadAllMovies();
    }


    /**
     * @Method
     * Load Movie Data From Network Using Retrofit2
     */
    public void loadMovie() {
        call  = movieInterface.doGetMovieList(sortType, pageNum, apiKey);

        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                if (response.isSuccessful()) {
                    List<Movie> addingMovieList = response.body().getMovieList();
                    if(orginMovieList.size() > 0) {
                        orginMovieList.remove(orginMovieList.size()-1);
                    }

                    orginMovieList.addAll(addingMovieList);
                    movieList.setValue(orginMovieList);
                    pageNum++;
                }
                else {
                    errorData.setValue("Error");
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                if(!call.isCanceled()) {
                    errorData.setValue(t.getMessage());
                }
            }
        });
    }


    /**
     * @Method
     * Notify Favorite Movie List to MainActivity
     */
    public void setFavoriteListAtMovieList() {
        movieList.setValue(favoriteMovieList.getValue());
    }


    public LiveData<List<Movie>> getMovieList() {
        return movieList;
    }


    public LiveData<List<Movie>> getFavoriteMovieList() {
        return favoriteMovieList;
    }


    public LiveData<String> getError() {
        return errorData;
    }


    public void setSortType(String sortType) {
        this.sortType = sortType;
    }


    /**
     * @Method
     * Initialize ViewModel Data
     */
    public void initData() {
        pageNum = 1;
        orginMovieList = new ArrayList<>();
        call.cancel(); //if loading , so call need canceld
    }
}
