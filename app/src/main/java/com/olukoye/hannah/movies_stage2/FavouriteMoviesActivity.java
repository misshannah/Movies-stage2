package com.olukoye.hannah.movies_stage2;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.olukoye.hannah.movies_stage2.database.AppDatabase;
import com.olukoye.hannah.movies_stage2.database.DaoAccess;
import com.olukoye.hannah.movies_stage2.database.FavMoviesTable;
import com.olukoye.hannah.movies_stage2.adapter.FavMovieAdapter;
import com.olukoye.hannah.movies_stage2.databinding.ActivityFavouriteMoviesBinding;

import java.util.List;


public class FavouriteMoviesActivity extends AppCompatActivity {
    private ActivityFavouriteMoviesBinding favBinding;
    private DaoAccess favDao;
    private FavMovieAdapter favMovieAdapter;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourite_movies);

        favBinding.favrecyclerView.setHasFixedSize(true);
        if (favBinding.favrecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            favBinding.favrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (favBinding.favrecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            favBinding.favrecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        favDao = AppDatabase.getInstance(getApplicationContext()).message();
        favDao.fetchAllMovies().observe(this, (List<FavMoviesTable> favmovie) -> {
            favMovieAdapter = new FavMovieAdapter(FavouriteMoviesActivity.this, favmovie);
            favBinding.favrecyclerView.setAdapter(favMovieAdapter);

        });


    }
}
