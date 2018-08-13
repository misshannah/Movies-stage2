package com.olukoye.hannah.movies_stage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olukoye.hannah.movies_stage2.Interfaces.MoviesInterfaceApi;
import com.olukoye.hannah.movies_stage2.Interfaces.RatedMoviesInterfaceApi;
import com.olukoye.hannah.movies_stage2.adapter.FavMovieAdapter;
import com.olukoye.hannah.movies_stage2.adapter.MovieAdapter;
import com.olukoye.hannah.movies_stage2.database.AppDatabase;
import com.olukoye.hannah.movies_stage2.database.DaoAccess;
import com.olukoye.hannah.movies_stage2.database.FavMoviesTable;
import com.olukoye.hannah.movies_stage2.databinding.ActivityMainBinding;

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
    SharedPreferences pref;
    String movieApiKey = BuildConfig.MOVIE_API_KEY;
    private DaoAccess favDao;
    private FavMovieAdapter favMovieAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        pref = getSharedPreferences("MoviePref", MODE_PRIVATE);


        binding.recyclerView.setHasFixedSize(true);
            if (binding.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else if (binding.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            }


       mAdapter = new MovieAdapter(this);
        binding.recyclerView.setAdapter(mAdapter);
        movies = new ArrayList<>();


        Gson gson = new Gson();
        String savedList = pref.getString("Movies" , "");
        movies = gson.fromJson(savedList,
                new TypeToken<List<Movie>>(){}.getType());

        mAdapter.setMovieList(movies);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.internet_check, Toast.LENGTH_SHORT).show();
        }

    }

    private void ratingOrder() {
        //Using the retrofit library for top rated movies
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.movie_url_base))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", movieApiKey);
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

                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                Gson gson = new Gson();
                String json = gson.toJson(movieResult.getRatedResults());
                edit.putString("Movies", json);
                edit.apply();
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
                        request.addEncodedQueryParam("api_key", movieApiKey);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MoviesInterfaceApi service = restAdapter.create(MoviesInterfaceApi.class);
        service.getPopularMovies(new Callback<Movie.PopularMovieResult>() {
            @Override
            public void success(Movie.PopularMovieResult movieResult, Response response) {
                mAdapter.setMovieList(movieResult.getPopularResults());
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                Gson gson = new Gson();
                String json = gson.toJson(movieResult.getPopularResults());
                edit.putString("Movies", json);
                edit.apply();

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void showFavourites() {


        favDao = AppDatabase.getInstance(getApplicationContext()).message();
        favDao.fetchAllMovies().observe(this, (List<FavMoviesTable> favmovie) -> {
            favMovieAdapter = new FavMovieAdapter(MainActivity.this, favmovie);

            binding.recyclerView.setAdapter(favMovieAdapter);

        });
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


}
