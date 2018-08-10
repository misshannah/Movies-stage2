package com.olukoye.hannah.movies_stage2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.olukoye.hannah.movies_stage2.database.FavMoviesTable;

import java.util.List;

/**
 * Created by hannaholukoye on 10/08/2018.
 */

public class MyViewModel extends ViewModel {
    private LiveData<List<Movie>> movies;

    public LiveData<List<Movie>> getPopularResults() {
        return movies;
    }
}
