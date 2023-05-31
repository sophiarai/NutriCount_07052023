package com.example.nutricount_07052023.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SportDao {
    @Query("SELECT * FROM sports")
    List<SportEntity> getAllSports();

    @Insert
    void insertSport(SportEntity sportEntity);
}
