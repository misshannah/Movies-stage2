package com.olukoye.hannah.movies_stage2.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FavMoviesTable {
    @NonNull
    @PrimaryKey
    private String movieId;
    private String movieName;
    private String poster;


    public FavMoviesTable(String movieId, String movieName, String poster) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.poster = poster;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getMovieName() { return movieName; }
    public void setMovieName (String movieName) { this.movieName = movieName; }
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {this.poster = poster;}
}
