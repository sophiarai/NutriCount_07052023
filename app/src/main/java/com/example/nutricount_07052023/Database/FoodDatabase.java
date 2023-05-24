package com.example.nutricount_07052023.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {FoodEntity.class}, version = 1, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {

    public abstract FoodDao foodDao();
}
