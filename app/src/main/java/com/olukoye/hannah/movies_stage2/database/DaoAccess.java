package com.olukoye.hannah.movies_stage2.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.olukoye.hannah.movies_stage2.Movie;

import java.util.List;

@Dao
public interface DaoAccess {

    //Add Movie to Favourite List
    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    void insertOnlySingleMovie(FavMoviesTable... favMoviesTable);

    //Get All Movies on Favourite List
    @Query("SELECT movieId FROM FavMoviesTable")
    LiveData<List<FavMoviesTable>> fetchAllMoviesIds();

    @Query("SELECT * FROM FavMoviesTable")
    LiveData<List<FavMoviesTable>> fetchAllMovies();


    //Delete Movie from Favourite List
    @Delete
    void deleteFavMovies(FavMoviesTable... favMoviesTable);

}
