package com.olukoye.hannah.movies_stage2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.olukoye.hannah.movies_stage2.Interfaces.FavouritesInterfaceApi;
import com.olukoye.hannah.movies_stage2.Interfaces.MoviesInterfaceApi;
import com.olukoye.hannah.movies_stage2.databinding.ActivityFavouriteMoviesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FavouriteMoviesActivity extends AppCompatActivity {
    private ActivityFavouriteMoviesBinding favBinding;
    private MovieAdapter mAdapter;
    private List<Movie> movies;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourite_movies);
        //Bundle bundle = getIntent().getExtras();
        //id = bundle.getString("33333");
        id = "233";

        favBinding.favrecyclerView.setHasFixedSize(true);
        if (favBinding.favrecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            favBinding.favrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (favBinding.favrecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            favBinding.favrecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }


        mAdapter = new MovieAdapter(this);
        favBinding.favrecyclerView.setAdapter(mAdapter);
        movies = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }

        //mAdapter.setMovieList(movies);
        showPosters();
    }
public void showPosters () {
    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(getString(R.string.movie_url_base))
            .setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addEncodedQueryParam("api_key", getString(R.string.movie_api_key));
                    request.addPathParam("movie-id",id);
                }
            })
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    FavouritesInterfaceApi service = restAdapter.create(FavouritesInterfaceApi.class);

    Callback<Movie.FavResult> callback = new Callback<Movie.FavResult>() {
        @Override
        public void success(Movie.FavResult movieResult, Response response) {
            mAdapter.setMovieList(movieResult.getFavResult());
            Log.i("Callback", movieResult.toString());

        }

        @Override
        public void failure(RetrofitError retrofitError) {

        }
    };
    service.getFavMovies(id, callback);
}
}
