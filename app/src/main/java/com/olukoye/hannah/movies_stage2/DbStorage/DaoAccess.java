package com.olukoye.hannah.movies_stage2.DbStorage;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.olukoye.hannah.movies_stage2.FavMovie;

import java.util.List;

@Dao
public interface DaoAccess {

    //Add Movie to Favourite List
    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    void insertOnlySingleMovie(FavMoviesTable... favMoviesTable);

    @Query("SELECT * FROM FavMoviesTable WHERE movieId = :movieId")
    FavMoviesTable fetchOneMoviesbyMovieId (int movieId);

    //Get All Movies on Favourite List
    @Query("SELECT movieId FROM FavMoviesTable")
    List<FavMovie> fetchAllMovies();


    //FavMoviesTable fetchAllMovies ();

    @Query("SELECT * FROM FavMoviesTable")
    List<FavMoviesTable> fetchAllMoviesold ();

    //Delete Movie from Favourite List
    @Delete
    void deleteFavMovies(FavMoviesTable... favMoviesTable);

}
