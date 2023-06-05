package com.example.nutricount_07052023.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    Integer id;

    @ColumnInfo(name="day")
    String day;

    @ColumnInfo(name="all_calories")
    int allCalories;

    @ColumnInfo(name="burned_calories")
    int burnedCalories;

    @ColumnInfo(name="consumed_calories")
    int consumedCalories;

    @ColumnInfo(name="username")
    String username;


    public NoteEntity (String day, String username, int allCalories, int burnedCalories, int consumedCalories)
    {
        this.day = day;
        this.username = username;
        this.allCalories = allCalories;
        this.burnedCalories = burnedCalories;
        this.consumedCalories = consumedCalories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getAllCalories() {
        return allCalories;
    }

    public void setAllCalories(int allCalories) {
        this.allCalories = allCalories;
    }

    public int getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(int burnedCalories) {
        this.burnedCalories = burnedCalories;
    }

    public int getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(int consumedCalories) {
        this.consumedCalories = consumedCalories;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}