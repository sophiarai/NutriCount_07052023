package com.example.nutricount_07052023.Database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface FoodDao {
    @Insert
    void insertFoodItems(List<FoodEntity> foodItems);

    @Query("SELECT * FROM food_items")
    List<FoodEntity> getAllFoodItems();
}
