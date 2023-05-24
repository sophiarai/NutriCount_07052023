package com.example.nutricount_07052023;


import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.example.nutricount_07052023.Database.FoodDao;
import com.example.nutricount_07052023.Database.FoodDatabase;
import com.example.nutricount_07052023.Database.FoodEntity;
import com.example.nutricount_07052023.Database.FoodManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import java.util.ArrayList;
import java.util.List;



public class FoodActivity extends AppCompatActivity {
    Button btnScan;
    TextView textViewFruit, textViewMeals, textViewCalories;
    MaterialCardView  selectCardMeals, selectCards;
    boolean[] selectedFruits, selectedMeals;
    private FoodManager foodManager;
    private List<Integer> fruitsList = new ArrayList<>();
    private List<Integer> mealList = new ArrayList<>();
    // Konstanten für die Verwendung in SharedPreferences
    private static final String PREF_SELECTED_FRUITS = "selected_fruits";
    private static final String PREF_SELECTED_MEALS = "selected_meals";
    private static final String PREF_TEXT_VIEW_FRUIT = "text_view_fruit";
    private static final String PREF_TEXT_VIEW_MEALS = "text_view_meals";

    private List<String> fruitNames; // Liste der Obstnamen
    private List<String> mealNames; // Liste der Mahlzeitenamen

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        textViewFruit = findViewById(R.id.tvFood);
        textViewMeals = findViewById(R.id.tvMeals);
        textViewCalories = findViewById(R.id.kcal_textview);

        foodManager = new FoodManager();

        // Initialize the selectedFruits and selectedMeals arrays
        selectedFruits = new boolean[foodManager.getFruitNames().size()];
        selectedMeals = new boolean[foodManager.getMealNames().size()];



        // Select Card for fruits
        selectCards = findViewById(R.id.selectCard);
        selectCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFruitDialog();
            }
        });

        // Select Card for meals
        selectCardMeals = findViewById(R.id.selectCardsMeal);
        selectCardMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMealDialog();
            }
        });

        // QRCode Scanner
        btnScan = (Button) findViewById(R.id.buttonScanner);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        // BottomNav in Activity anzeigen
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_food);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_sport:
                    startActivity(new Intent(getApplicationContext(), SportActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_food:
                    return true;
                case R.id.bottom_notes:
                    startActivity(new Intent(getApplicationContext(), NotesActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on!");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });

    private void showFruitDialog() {
        // Retrieve the fruit array from FoodManager
        String[] fruitArray = foodManager.getFruitNames().toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Food");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(fruitArray, selectedFruits, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked) {
                    fruitsList.add(which);
                } else {
                    fruitsList.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < fruitsList.size(); i++) {
                    stringBuilder.append(fruitArray[fruitsList.get(i)]);

                    if (i != fruitsList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                textViewFruit.setText(stringBuilder.toString());
                calculateTotalCalories();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < selectedFruits.length; i++) {
                    selectedFruits[i] = false;
                }
                fruitsList.clear();
                textViewFruit.setText("");
                calculateTotalCalories();
            }
        });
        builder.show();
    }

    private void showMealDialog() {
        // Retrieve the meal array from FoodManager
        String[] mealArray = foodManager.getMealNames().toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Meals");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(mealArray, selectedMeals, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked) {
                    mealList.add(which);
                } else {
                    mealList.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mealList.size(); i++) {
                    stringBuilder.append(mealArray[mealList.get(i)]);

                    if (i != mealList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                textViewMeals.setText(stringBuilder.toString());
                calculateTotalCalories();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < selectedMeals.length; i++) {
                    selectedMeals[i] = false;
                }
                mealList.clear();
                textViewMeals.setText("");
                calculateTotalCalories();
            }
        });
        builder.show();
    }

    private void calculateTotalCalories() {
        int fruitCalories = 0;
        int mealCalories = 0;
        foodManager = new FoodManager();

        // Calculate the calories of selected fruits
        for (int fruitIndex : fruitsList) {
            String fruitName = foodManager.getFruitNames().get(fruitIndex);
            fruitCalories += foodManager.getFruitCalories(fruitName);
        }

        // Calculate the calories of selected meals
        for (int mealIndex : mealList) {
            String mealName = foodManager.getMealNames().get(mealIndex);
            mealCalories += foodManager.getMealCalories(mealName);
        }

        // Sum up the total calories
        int totalCalories = fruitCalories + mealCalories;

        // Display the total calories in the TextView
        textViewCalories.setText("Total Calories: " + totalCalories);

        // Save the total calories in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("totalCalories", totalCalories);
        editor.apply();
    }




    //aller letzte Klammer
}
