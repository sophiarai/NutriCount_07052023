package com.example.nutricount_07052023.Database;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodManager {
    private Map<String, Integer> fruitMap;
    private Map<String, Integer> mealMap;

    private Map<String, Integer> breakfastMap;
    private Map<String, Integer> sweetsMap;
    private Map<String, Integer> lunchMap;


    public FoodManager() {
        fruitMap = new HashMap<>();
        mealMap = new HashMap<>();
        breakfastMap=new HashMap<>();
        sweetsMap=new HashMap<>();
        lunchMap=new HashMap<>();
        initializeFoodItemList();
    }

    private void initializeFoodItemList() {
        fruitMap.put("Apfel", 95);
        fruitMap.put("Banane", 105);
        fruitMap.put("Orange", 62);
        fruitMap.put("Mango", 60);
        fruitMap.put("Ananas",80);
        fruitMap.put("Kiwi",42);
        fruitMap.put("Birne", 102);
        fruitMap.put("1 Tasse Heidelbeeren", 85);

        mealMap.put("Lasagne", 400);
        mealMap.put("Toast", 150);
        mealMap.put("Gemischter grüner Salat mit Hühnerbruststreifen",300);
        mealMap.put("Vollkorn-Sandwich mit Putenbrust, Gemüse und Senf",350);
        mealMap.put("Quinoa-Salat mit Gurken, Tomaten und Feta-Käse ",450);
        mealMap.put("Vegetarische Reispfanne mit verschiedenen Gemüsesorten",350);
        mealMap.put("Vollkornwraps mit gebratenem Gemüse, Hähnchen",350);
        mealMap.put("Tunfischsalat", 300);
        mealMap.put("Gebratener Lachs", 450);
        mealMap.put("Vollkornnudeln mit Tomatensauce, Gemüse und Parmesankäse", 350);
        mealMap.put("Gebratener Reis mit Gemüse und Ei",400);

        breakfastMap.put("Kaffee mit Milch",21);
        breakfastMap.put("Gekochtes Ei",78);
        breakfastMap.put("Brot(Scheibe", 124);
        breakfastMap.put("Banane", 134);
        breakfastMap.put("Weizenbrötchen",163);
        breakfastMap.put("Kaffe(Tasse)",2);
        breakfastMap.put("Kaffee mit Milch 1,5% (200ml)", 60);
        breakfastMap.put("Gurke",12);
        breakfastMap.put("Tomate", 18);
        breakfastMap.put("Vollkornbrot(Scheibe),55g", 108);
        breakfastMap.put("Gouda Käse 45% (Scheibe),30g", 109);
        breakfastMap.put("Haferflocken", 372);
        breakfastMap.put("Vollkornbrot mit Avocado und Tomate", 350);

        sweetsMap.put("Bounty (100g)", 470);
        sweetsMap.put("Erdnussriegel (100g)",523);
        sweetsMap.put("Gummibärchen (100g)", 348);
        sweetsMap.put("Lakritze (100g)", 391);
        sweetsMap.put("Mars (100g)", 504);
        sweetsMap.put("Oreo (100g)", 471);
        sweetsMap.put("Toffifee (100g)", 515);

    }

    public List<String> getFruitNames() {
        return new ArrayList<>(fruitMap.keySet());
    }

    public List<String> getMealNames() {
        return new ArrayList<>(mealMap.keySet());
    }
    public List<String> getBreakfastNames() {
        return new ArrayList<>(breakfastMap.keySet());
    }
    public List<String> getSweetsNames() {
        return new ArrayList<>(sweetsMap.keySet());
    }


    public int getFruitCalories(String fruitName) {
        Integer calories = fruitMap.get(fruitName);
        return (calories != null) ? calories : 0;
    }

    public int getMealCalories(String mealName) {
        Integer calories = mealMap.get(mealName);
        return (calories != null) ? calories : 0;
    }
    public int getBreakfastCalories(String breakfastName) {
        Integer calories = breakfastMap.get(breakfastName);
        return (calories != null) ? calories : 0;
    }
    public int getSweetsCalories(String sweetsName) {
        Integer calories = sweetsMap.get(sweetsName);
        return (calories != null) ? calories : 0;
    }
}