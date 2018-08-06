package com.olukoye.hannah.movies_stage2;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.olukoye.hannah.movies_stage2.Interfaces.DaoAccess;

@Database(entities = {MoviesTable.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
