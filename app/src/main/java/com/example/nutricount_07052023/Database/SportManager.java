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
        sportList.add(new SportEntity("Cardio 30min", 300));
        sportList.add(new SportEntity("Cardio 1h", 400));
        sportList.add(new SportEntity("Running 30min", 300));
        sportList.add(new SportEntity("Running 1h", 700));
        sportList.add(new SportEntity("Swimming 30min", 350));
        sportList.add(new SportEntity("Swimming 1h", 600));
        sportList.add(new SportEntity("Biking 30min", 220));
        sportList.add(new SportEntity("Biking 1h", 500));
        sportList.add(new SportEntity("Dancing 30min", 230));
        sportList.add(new SportEntity("Dancing 1h", 470));
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
