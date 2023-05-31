package com.example.nutricount_07052023.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "food_items")
public class FoodEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int calories;

    public FoodEntity(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName(){return name;}
    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}