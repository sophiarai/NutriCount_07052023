package com.example.nutricount_07052023.Database;

import androidx.room.Database;

import androidx.room.RoomDatabase;


@Database(entities = {SportEntity.class}, version = 1)
public abstract class SportDatabase extends RoomDatabase {
    public abstract SportDao sportDao();
}
