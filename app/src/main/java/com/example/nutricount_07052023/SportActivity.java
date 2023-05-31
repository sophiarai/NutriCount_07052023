package com.example.nutricount_07052023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nutricount_07052023.Database.SportDao;
import com.example.nutricount_07052023.Database.SportDatabase;
import com.example.nutricount_07052023.Database.SportEntity;
import com.example.nutricount_07052023.Database.SportManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;



public class SportActivity extends AppCompatActivity implements SensorEventListener {

    private static final String PREF_SELECTED_SPORTS = "selected_sports";
    private static final String PREF_TOTAL_CALORIES_SPORT = "total_calories_sport";
    private static final String PREF_TEXT_VIEW_SPORT = "text_view_sport";
    private ArrayList<Integer> selectedSportsList;
    private int totalCalories;
    TextView textViewSport, textViewkcalSport, textViewSteps;
    Button btnResetSteps;
    ImageButton imageButtonLogout, imageButtonPersonal;
    MaterialCardView selectCardSport;
    boolean[] selectedsport;
    private List<SportEntity> sports;

    private SensorManager sensorManager;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        imageButtonLogout=findViewById(R.id.imageButton_logout);
        imageButtonPersonal = findViewById(R.id.btnPersonal);

        btnResetSteps = findViewById(R.id.button_steps);
        textViewSteps = findViewById(R.id.tv_stepsTaken);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor == null) {
            Toast.makeText(this, "This device does not support step counting.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This device does support step counting.", Toast.LENGTH_SHORT).show();
        }
        btnResetSteps.setOnClickListener(view -> resetSteps());

        textViewkcalSport = findViewById(R.id.kcal_sport);
        selectCardSport = findViewById(R.id.selectCardSport);
        textViewSport = findViewById(R.id.tvSport);

        // Room Database initialisieren
        SportDatabase database = Room.databaseBuilder(getApplicationContext(), SportDatabase.class, "app-db")
                .allowMainThreadQueries()
                .build();
        SportDao sportDao = database.sportDao();
        sports = sportDao.getAllSports();

        if (sports == null || sports.isEmpty()) {
            SportManager sportManager = new SportManager();
            List<String> sportNames = sportManager.getSportNames();
            for (String sportName : sportNames) {
                SportEntity sportEntity = new SportEntity(sportName, sportManager.getSportCalories(sportName));
                sportDao.insertSport(sportEntity);
            }
            sports = sportDao.getAllSports();
        }
        selectedsport = new boolean[sports.size()];

        selectCardSport.setOnClickListener(view -> showSportDialog());

        // Wiederherstellen der gespeicherten Daten aus SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        selectedSportsList = getSelectedSportsListFromPrefs(sharedPreferences);
        totalCalories = sharedPreferences.getInt(PREF_TOTAL_CALORIES_SPORT, 0);
        textViewSport.setText(sharedPreferences.getString(PREF_TEXT_VIEW_SPORT, ""));
        textViewkcalSport.setText("Lost calories: " + totalCalories);


        imageButtonPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        // BottomNavigation wird in SportActivity angezeigt:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_sport);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_sport:
                    return true;
                case R.id.bottom_food:
                    startActivity(new Intent(getApplicationContext(), FoodActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_notes:
                    startActivity(new Intent(getApplicationContext(), NotesActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
    }
    private void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("LogOut");
        alertDialog.setMessage("Do you really want to Logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SportActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.show();
    }

    private void showSportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SportActivity.this);
        builder.setTitle("Select Food");
        builder.setCancelable(false);

        // Konvertiere die Liste der Sportarten in ein Array
        String[] sportArray = new String[sports.size()];
        for (int i = 0; i < sports.size(); i++) {
            sportArray[i] = sports.get(i).getName();
        }

        // Erzeuge ein boolean-Array, um den Zustand der ausgewählten Sportarten zu speichern
        boolean[] selectedSports = new boolean[sportArray.length];
        for (int i = 0; i < selectedSports.length; i++) {
            if (selectedSportsList.contains(i)) {
                selectedSports[i] = true;
            }
        }

        builder.setMultiChoiceItems(sportArray, selectedSports, (dialogInterface, which, isChecked) -> {
            if (isChecked) {
                if (!selectedSportsList.contains(which)) {
                    selectedSportsList.add(which);
                }
            } else {
                selectedSportsList.remove(Integer.valueOf(which));
            }
        }).setPositiveButton("OK", (dialogInterface, which) -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < selectedSportsList.size(); i++) {
                int sportIndex = selectedSportsList.get(i);
                stringBuilder.append(sportArray[sportIndex]);

                if (i != selectedSportsList.size() - 1) {
                    //when i value not equal to food list, then add comma
                    stringBuilder.append(", ");
                }
                textViewSport.setText(stringBuilder.toString());
                updateTotalCalories();
            }
            dialogInterface.dismiss();
        }).setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss()).setNeutralButton("Clear all", (dialogInterface, which) -> {
            selectedSportsList.clear();
            textViewSport.setText("");
            updateTotalCalories();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTotalCalories() {
        int totalCalories2 = calculateTotalCalories();
        textViewkcalSport.setText("Lost calories: " + totalCalories2);

        // Speichern des gesamten Kalorienwerts in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_TOTAL_CALORIES_SPORT, totalCalories2);
        editor.putString(PREF_TEXT_VIEW_SPORT, textViewSport.getText().toString());
        editor.apply();

        totalCalories = totalCalories2;
    }

    private int calculateTotalCalories() {
        int totalCalories = 0;
        for (int sportIndex : selectedSportsList) {
            totalCalories += sports.get(sportIndex).getCalories();
        }
        // Anzahl der Schritte abrufen und in die Kalorienberechnung einbeziehen
        int stepCount = Integer.parseInt(textViewSteps.getText().toString());
        int caloriesPerStep = 0; // Setze hier den Kalorienverbrauch pro Schritt
        int caloriesFromSteps = stepCount * caloriesPerStep;
        // Addiere Kalorienverbrauch aus den Schritten zu den Gesamtkalorien
        totalCalories += caloriesFromSteps;

        return totalCalories;
    }

    @Override
    protected void onPause() {
        super.onPause();
        running=false;
        sensorManager.unregisterListener(this);

        // Speichern des aktuellen Zustands in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_SELECTED_SPORTS, convertListToString(selectedSportsList));
        editor.putInt(PREF_TOTAL_CALORIES_SPORT, totalCalories);
        editor.putString(PREF_TEXT_VIEW_SPORT, textViewSport.getText().toString());
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        running=true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }

        // Wiederherstellen des gespeicherten Zustands aus SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        selectedSportsList = getSelectedSportsListFromPrefs(sharedPreferences);
        totalCalories = sharedPreferences.getInt(PREF_TOTAL_CALORIES_SPORT, 0);
        textViewSport.setText(sharedPreferences.getString(PREF_TEXT_VIEW_SPORT, ""));
        textViewkcalSport.setText("Lost calories: " + totalCalories);


        // Aktualisiert den Zustand der ausgewählten Sportarten im Dialogfenster
        if (selectedSportsList != null) {
            for (int sportIndex : selectedSportsList) {
                if (sportIndex >= 0 && sportIndex < selectedsport.length) {
                    selectedsport[sportIndex] = true;
                }}}
    }

    private ArrayList<Integer> getSelectedSportsListFromPrefs(SharedPreferences sharedPreferences) {
        String selectedSportsJson = sharedPreferences.getString(PREF_SELECTED_SPORTS, null);
        if (selectedSportsJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(selectedSportsJson);
                ArrayList<Integer> selectedSportsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    selectedSportsList.add(jsonArray.getInt(i));
                }
                return selectedSportsList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private String convertListToString(List<Integer> list) {
        JSONArray jsonArray = new JSONArray();
        for (Integer item : list) {
            jsonArray.put(item);
        }
        return jsonArray.toString();
    }

    private void resetSteps() {
        textViewSteps.setText("0");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Hier kann entsprechend auf Veränderungen der Genauigkeit reagiert werden
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            textViewSteps.setText(String.valueOf(event.values[0]));
            updateTotalCalories();
        }
    }
}