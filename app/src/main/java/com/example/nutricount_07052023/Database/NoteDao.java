package com.example.nutricount_07052023.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote (NoteEntity noteEntity);

    @Query("SELECT * FROM notes WHERE username = :username")
    List<NoteEntity> getNotesByUser (String username);

    @Query("SELECT * FROM notes WHERE username = :username AND day = :day")
    List<NoteEntity> getNotesByUserDay (String username, String day);
}
