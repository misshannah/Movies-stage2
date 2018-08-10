package com.olukoye.hannah.movies_stage2;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.olukoye.hannah.movies_stage2.Interfaces.MoviesInterfaceApi;
import com.olukoye.hannah.movies_stage2.Interfaces.RatedMoviesInterfaceApi;
import com.olukoye.hannah.movies_stage2.adapter.MovieAdapter;
import com.olukoye.hannah.movies_stage2.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {
    private MovieAdapter mAdapter;
    private ActivityMainBinding binding;
    private List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOnline();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.recyclerView.setHasFixedSize(true);
        if(binding.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (binding.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }


        mAdapter = new MovieAdapter(this);
        binding.recyclerView.setAdapter(mAdapter);
        movies = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }

        mAdapter.setMovieList(movies);

        if (savedInstanceState == null) {
            popularOrder();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putSerializable("list-state", (Serializable) movies);

    }
    //To verify internet connection is available
    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            Toast.makeText(getApplicationContext(), "Connected to the Internet!", Toast.LENGTH_SHORT).show();

            return reachable;
        } catch (Exception e) {

            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Check Internet connection!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void ratingOrder() {
        //Using the retrofit library for top rated movies
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.movie_url_base))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", getString(R.string.movie_api_key));
                        request.addEncodedQueryParam("language", getString(R.string.en_language));
                        request.addEncodedQueryParam("sort_by", getString(R.string.sort_by_date));
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RatedMoviesInterfaceApi sortRating = restAdapter.create(RatedMoviesInterfaceApi.class);
        sortRating.getTopRatedMovies(new Callback<Movie.RatedMovieResult>() {
            @Override
            public void success(Movie.RatedMovieResult movieResult, Response response) {
                mAdapter.setMovieList(movieResult.getRatedResults());
                Log.i("rating output", movies.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void popularOrder() {
        //Using the retrofit library for popular movies
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.movie_url_base))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", getString(R.string.movie_api_key));
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MoviesInterfaceApi service = restAdapter.create(MoviesInterfaceApi.class);
        service.getPopularMovies(new Callback<Movie.PopularMovieResult>() {
            @Override
            public void success(Movie.PopularMovieResult movieResult, Response response) {
                mAdapter.setMovieList(movieResult.getPopularResults());

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void showFavourites() {
        Intent openFavourites = new Intent(getApplication(), FavouriteMoviesActivity.class);
        startActivity(openFavourites);
    }

    //Show Settings Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // bring up the settings dialog if the settings menu option is selected
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            popularOrder();
            return true;
        }
        if (id == R.id.action_rating) {
            ratingOrder();
            return true;
        }
        if (id == R.id.action_favourites) {
            showFavourites();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getSerializable("list-state");
    }


    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        popularOrder();

    }
    @Override
    protected void onStart() {
        super.onStart();
        popularOrder();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
