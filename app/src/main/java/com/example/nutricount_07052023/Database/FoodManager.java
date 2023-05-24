package com.example.nutricount_07052023.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodManager {
    private Map<String, Integer> fruitMap;
    private Map<String, Integer> mealMap;

    public FoodManager() {
        fruitMap = new HashMap<>();
        mealMap = new HashMap<>();
        initializeFoodItemList();
    }

    private void initializeFoodItemList() {
        fruitMap.put("Apple", 50);
        fruitMap.put("Banana", 100);
        fruitMap.put("Orange", 70);
        fruitMap.put("Mango", 60);

        mealMap.put("Spaghetti", 300);
        mealMap.put("Fish", 200);
        mealMap.put("Lasagne", 400);
        mealMap.put("Toast", 150);
    }

    public List<String> getFruitNames() {
        return new ArrayList<>(fruitMap.keySet());
    }

    public List<String> getMealNames() {
        return new ArrayList<>(mealMap.keySet());
    }

    public int getFruitCalories(String fruitName) {
        Integer calories = fruitMap.get(fruitName);
        return (calories != null) ? calories : 0;
    }

    public int getMealCalories(String mealName) {
        Integer calories = mealMap.get(mealName);
        return (calories != null) ? calories : 0;
    }
}