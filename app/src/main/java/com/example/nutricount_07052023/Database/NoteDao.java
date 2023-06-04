package com.example.nutricount_07052023.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NoteDao {

    @Insert
    void insertNote (NoteEntity noteEntity);

    @Query("SELECT * FROM notes WHERE username = :username")
    NoteEntity getNotesByUser (String username);

    @Query("SELECT * FROM notes WHERE username = :username AND day = :day")
    NoteEntity getNotesByUserDay (String username, String day);
}
