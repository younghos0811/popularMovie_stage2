package com.younghos0811.github.popular_movies.utiles.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiService {

    private static MovieInterface movieInterface;

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static MovieInterface getService() {
        if (movieInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();

            movieInterface = retrofit.create(MovieInterface.class);
        }

        return movieInterface;

    }

}