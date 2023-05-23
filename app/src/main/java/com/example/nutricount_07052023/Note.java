package com.example.nutricount_07052023;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private Date date;

    private String allCalories;
    private String burnedCalories;
    private String consumedCalories;

    public Note(Date date,String allCalories, String burnedCalories, String consumedCalories) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAllCalories() {
        return allCalories;
    }

    public void setAllCalories(String allCalories) {
        this.allCalories = allCalories;
    }


}
