package com.example.nutricount_07052023.Database;

import java.util.ArrayList;
import java.util.List;

public class SportManager {
    private List<SportEntity> sportList;

    public SportManager() {
        sportList = new ArrayList<>();
        initializeSportList();
    }

    private void initializeSportList() {
        // Füge deine Sportarten mit den Kalorienwerten hinzu
        sportList.add(new SportEntity("Cardio 30min", 200));
        sportList.add(new SportEntity("Cardio 1h", 220));
        sportList.add(new SportEntity("Running 30min", 100));
        sportList.add(new SportEntity("Running 1h", 150));
    }

    public List<String> getSportNames() {
        List<String> sportNames = new ArrayList<>();
        for (SportEntity sportEntity : sportList) {
            sportNames.add(sportEntity.getName());
        }
        return sportNames;
    }

    public int getSportCalories(String sportName) {
        for (SportEntity sportEntity : sportList) {
            if (sportEntity.getName().equals(sportName)) {
                return sportEntity.getCalories();
            }
        }
        return 0;
    }
}
