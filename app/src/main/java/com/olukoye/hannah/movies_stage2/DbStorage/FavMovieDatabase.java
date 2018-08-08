package com.olukoye.hannah.movies_stage2.DbStorage;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {FavMoviesTable.class}, version = 1, exportSchema = false)

public abstract class FavMovieDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;

}

