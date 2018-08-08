package com.olukoye.hannah.movies_stage2.DbStorage;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    void insertOnlySingleMovie(FavMoviesTable... favMoviesTable);
    @Insert
    void insertMultipleMovies (List<FavMoviesTable> moviesList);
    @Query("SELECT * FROM FavMoviesTable WHERE movieId = :movieId")
    FavMoviesTable fetchOneMoviesbyMovieId (int movieId);
    @Query("SELECT * FROM FavMoviesTable")
    List<FavMoviesTable> fetchAllMovies ();
    @Update
    void updateMovie (FavMoviesTable favmoviestable);
    @Delete
    void deleteMovie (FavMoviesTable favmoviestable);

}
