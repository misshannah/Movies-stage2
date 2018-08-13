package com.olukoye.hannah.movies_stage2.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavMoviesTable.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase appDatabase;

    public abstract DaoAccess message();

    private Context context;

    private static final Object LOCK = new Object();
    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (LOCK) {
                appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "movies_db3")
                        .allowMainThreadQueries()
                        .build();
            }
        }

        return appDatabase;
    }


    public static void destroyInstance() {
        appDatabase = null;
    }
}
