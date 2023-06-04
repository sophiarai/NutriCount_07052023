package com.example.nutricount_07052023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;



import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.nutricount_07052023.Database.SportDao;
import com.example.nutricount_07052023.Database.SportDatabase;
import com.example.nutricount_07052023.Database.SportEntity;
import com.example.nutricount_07052023.Database.SportManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SportActivity extends AppCompatActivity implements SensorEventListener {

    private static final String PREF_SELECTED_SPORTS = "selected_sports";
    private static final String PREF_TOTAL_CALORIES_SPORT = "total_calories_sport";
    private static final String PREF_TEXT_VIEW_SPORT = "text_view_sport";
    private static final String PREF_LAST_DATE = "last_date";
    private ArrayList<Integer> selectedSportsList;
    private int totalCalories;
    TextView textViewSport, textViewkcalSport, textViewSteps;
    //Button btnResetSteps;
    ImageButton imageButtonLogout, imageButtonPersonal;
    MaterialCardView selectCardSport;
    boolean[] selectedsport;
    private List<SportEntity> sports;

    private SensorManager sensorManager;
    private Sensor mStepCounter;
    int stepCount=0;
    private static final String PREF_STEP_COUNT = "step_count";


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        imageButtonLogout=findViewById(R.id.imageButton_logout);
        imageButtonPersonal = findViewById(R.id.btnPersonal);
        textViewSteps = findViewById(R.id.tv_stepsTaken);
        textViewkcalSport = findViewById(R.id.kcal_sport);
        selectCardSport = findViewById(R.id.selectCardSport);
        textViewSport = findViewById(R.id.tvSport);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        selectedSportsList = new ArrayList<>();




        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }else{
            textViewSteps.setText("Counter Sensor is not Present");
        }

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





        imageButtonPersonal.setOnClickListener(view -> {
            Intent intent = new Intent(SportActivity.this, PersonalActivity.class);
            startActivity(intent);
        });

        // BottomNavigation wird in SportActivity angezeigt:
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

        imageButtonLogout.setOnClickListener(view -> showLogoutDialog());
    }
    private void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("LogOut");
        alertDialog.setMessage("Do you really want to Logout?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(SportActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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

        return totalCalories;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this,mStepCounter);

        // Speichern des aktuellen Kalorienwerts in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TEXT_VIEW_SPORT, textViewSport.getText().toString());
        editor.putString(PREF_TOTAL_CALORIES_SPORT, textViewkcalSport.getText().toString());
        editor.apply();


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Wiederherstellen des gespeicherten Schrittzählerwerts aus SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        stepCount = sharedPreferences.getInt(PREF_STEP_COUNT, 0);
        textViewSteps.setText(String.valueOf(stepCount));

        // Wiederherstellen des gespeicherten Sport-Texts aus SharedPreferences
        String sportText = sharedPreferences.getString(PREF_TEXT_VIEW_SPORT, "");
        textViewSport.setText(sportText);

        // Wiederherstellen des gespeicherten Kalorienwerts aus SharedPreferences
        int calories = sharedPreferences.getInt("total_calories", 0);
        String caloriesText = String.valueOf(calories);
        textViewkcalSport.setText(caloriesText);

        // Aktualisiert den Zustand der ausgewählten Sportarten im Dialogfenster
        if (selectedSportsList != null) {
            for (int sportIndex : selectedSportsList) {
                if (sportIndex >= 0 && sportIndex < selectedsport.length) {
                    selectedsport[sportIndex] = true;
                }
            }
        }
    }

    private String convertListToString(List<Integer> list) {
        JSONArray jsonArray = new JSONArray();
        for (Integer item : list) {
            jsonArray.put(item);
        }
        return jsonArray.toString();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == mStepCounter) {
            int previousStepCount = stepCount;
            stepCount = (int) sensorEvent.values[0];
            textViewSteps.setText(String.valueOf(stepCount));


            textViewkcalSport.setText("Lost calories: " + totalCalories);

            // Speichern des geänderten Gesamtkalorienwerts in SharedPreferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PREF_TOTAL_CALORIES_SPORT, totalCalories);
            editor.apply();


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}