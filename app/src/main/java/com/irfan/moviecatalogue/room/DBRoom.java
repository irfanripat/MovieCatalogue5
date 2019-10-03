package com.irfan.moviecatalogue.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.irfan.moviecatalogue.dao.MovieDAO;
import com.irfan.moviecatalogue.dao.TvDAO;
import com.irfan.moviecatalogue.model.Movie;
import com.irfan.moviecatalogue.model.TVShow;

@Database(entities = {Movie.class, TVShow.class}, version = 100, exportSchema = false)
public abstract class DBRoom extends RoomDatabase {
    private static DBRoom dbRoom;

    public static DBRoom getInstance(Context context) {
        if (dbRoom == null) {
            dbRoom = Room.databaseBuilder(context, DBRoom.class, "db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dbRoom;
    }

    public abstract MovieDAO movieDao();

    public abstract TvDAO tvDao();

}
