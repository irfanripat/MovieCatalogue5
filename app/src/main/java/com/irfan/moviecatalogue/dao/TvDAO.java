package com.irfan.moviecatalogue.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.irfan.moviecatalogue.model.TVShow;

import java.util.List;

@Dao
public interface TvDAO {
    @Query("SELECT * FROM TVShow WHERE id = :id")
    TVShow findTvById(int id);

    @Insert
    long addTv(TVShow tvShow);

    @Delete
    int delTv(TVShow tvShow);

    @Query("SELECT * FROM TVShow")
    List<TVShow> getFavouriteTvList();
}
