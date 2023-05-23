package com.example.nutricount_07052023;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private String date;

    private String allCalories;
    private String burnedCalories;
    private String consumedCalories;

    public Note(String date,String allCalories, String burnedCalories, String consumedCalories) {
        this.date = date;
        this.allCalories = allCalories;
        this.burnedCalories = burnedCalories;
        this.consumedCalories = consumedCalories;
    }

    public String getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(String burnedCalories) {
        this.burnedCalories = burnedCalories;
    }

    public String getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(String consumedCalories) {
        this.consumedCalories = consumedCalories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAllCalories() {
        return allCalories;
    }

    public void setAllCalories(String allCalories) {
        this.allCalories = allCalories;
    }


}
