package com.irfan.moviecatalogue.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;

import com.irfan.moviecatalogue.model.Movie;

import java.util.List;

@Dao
public interface MovieDAO {
    @Insert
    long addMovie(Movie movie);

    @Delete
    int delMovie(Movie movie);

    @Update
    int updateMovie(Movie movie);

    @Query("SELECT * FROM Movie WHERE id = :id")
    Movie findMovieById(int id);

    @Query("SELECT * FROM Movie")
    Cursor getFavouriteMovieCursor();

    @Query("SELECT * FROM Movie")
    List<Movie> getFavouriteMovieList();

    @Query("DELETE FROM Movie WHERE id = :id")
    int deleteById(long id);
}
