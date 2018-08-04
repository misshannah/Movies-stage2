package com.olukoye.hannah.movies_stage2.Interfaces;

import com.olukoye.hannah.movies_stage2.Movie;

import retrofit.Callback;
import retrofit.http.GET;


public interface MoviesInterfaceApi {
    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.PopularMovieResult> cb);

}