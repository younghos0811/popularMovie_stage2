package com.younghos0811.github.popular_movies.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.younghos0811.github.popular_movies.data.Movie;
import com.younghos0811.github.popular_movies.data.database.MovieDatabase;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDatabase mDb;
    private final String apiKey;
    private final Movie movie;

    public DetailViewModelFactory(MovieDatabase database ,Movie movie ,String apiKey) {
        this.mDb = database;
        this.apiKey = apiKey;
        this.movie = movie;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mDb,movie,apiKey);
    }
}
