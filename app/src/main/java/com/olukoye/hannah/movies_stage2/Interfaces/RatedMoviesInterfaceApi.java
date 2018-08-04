package com.olukoye.hannah.movies_stage2.Interfaces;

import com.olukoye.hannah.movies_stage2.Movie;

import retrofit.Callback;
import retrofit.http.GET;


public interface RatedMoviesInterfaceApi {
    @GET("/movie/top_rated")
    void getTopRatedMovies(Callback<Movie.RatedMovieResult> rb);

}