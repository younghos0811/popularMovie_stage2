package com.younghos0811.github.popular_movies.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.younghos0811.github.popular_movies.data.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY updated_at")
    LiveData<List<Movie>> loadAllMovies();

    @Insert
    void insertMovie(Movie newMovie);

    @Delete
    void deleteMovie(Movie taskMovie);

    @Query("SELECT * FROM Movie WHERE id = :id")
    LiveData<Movie> loadMovieById(String id);
}
