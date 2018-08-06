package com.olukoye.hannah.movies_stage2.Interfaces;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.olukoye.hannah.movies_stage2.MoviesTable;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleMovie (MoviesTable moviestable);
    @Insert
    void insertMultipleMovies (List<MoviesTable> moviesList);
    @Query("SELECT * FROM MoviesTable WHERE movieId = :movieId")
    MoviesTable fetchOneMoviesbyMovieId (int movieId);
    @Query("SELECT * FROM MoviesTable")
    List<MoviesTable> fetchAllMovies ();
    @Update
    void updateMovie (MoviesTable moviestable);
    @Delete
    void deleteMovie (MoviesTable moviestable);
}
