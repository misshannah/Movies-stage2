package com.olukoye.hannah.movies_stage2.DbStorage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FavMoviesTable {
    @NonNull
    @PrimaryKey
    private String movieId;
    private String movieName;

    public FavMoviesTable() {
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getMovieName() { return movieName; }
    public void setMovieName (String movieName) { this.movieName = movieName; }
}
