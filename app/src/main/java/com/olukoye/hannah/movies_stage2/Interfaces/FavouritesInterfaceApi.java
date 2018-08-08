package com.olukoye.hannah.movies_stage2.Interfaces;

import com.olukoye.hannah.movies_stage2.Movie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;


public interface FavouritesInterfaceApi {
    @GET("/movie/{movie-id}")
    void getFavMovies(@Path("movie-id") String id,Callback<Movie.FavResult> cb);

}