package com.example.nutricount_07052023;

public class Note {

    private String day;

    private String allCalories;
    private String burnedCalories;
    private String consumedCalories;

    public Note(String date, String allCalories, String burnedCalories, String consumedCalories) {
        this.day = date;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAllCalories() {
        return allCalories;
    }

    public void setAllCalories(String allCalories) {
        this.allCalories = allCalories;
    }


}
