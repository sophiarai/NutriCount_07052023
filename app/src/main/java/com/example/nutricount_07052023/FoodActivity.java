package com.example.nutricount_07052023;


import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FoodActivity extends AppCompatActivity {
    Button btnScan;
    ImageButton imageButtonLogout, imageButtonPersonal;
    TextView textViewFruit, textViewMeals, textViewCalories, textViewBreakfast, textViewSweets;
    MaterialCardView  selectCardMeals, selectCards, selectCardBreakfast, selectCardSweets;
    boolean[] selectedFruits, selectedMeals, selectedBreakfast, selectedSweets;
    private FoodManager foodManager;

    // Konstanten für die Verwendung in SharedPreferences
    private static final String PREF_SELECTED_FRUITS = "selected_fruits";
    private static final String PREF_SELECTED_MEALS = "selected_meals";
    private static final String PREF_SELECTED_BREAKFAST = "selected_breakfast";
    private static final String PREF_SELECTED_SWEETS = "selected_sweets";
    private static final String PREF_TOTAL_CALORIES_FOOD="total:calories_food";


    private static final int REQUEST_CODE_SCAN = 1;

    Set<String> selectedFruitsSet;
    Set<String> selectedMealsSet;
    Set<String>selectedBreakfastSet;
    Set<String>selectedSweetsSet;
    private int totalCalories;
    private FoodDatabase database;
    private FoodDao foodDao;
    private List<FoodEntity>food;
    private List<String> fruitNames; // Liste der Obstnamen
    private List<String> mealNames; // Liste der Mahlzeitenamen
    private List<String > breakfastNames;
    private List<String> sweetsName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        textViewFruit = findViewById(R.id.tvFood);
        textViewMeals = findViewById(R.id.tvMeals);
        textViewCalories = findViewById(R.id.kcal_textview);
        imageButtonLogout=findViewById(R.id.imageButton_logout);
        imageButtonPersonal = findViewById(R.id.btnPersonal);
        textViewBreakfast=findViewById(R.id.tvBreakfast);
        textViewSweets=findViewById(R.id.tvSweets);


        foodManager = new FoodManager();

        // Initialize the selectedFruits and selectedMeals arrays
        selectedFruits = new boolean[foodManager.getFruitNames().size()];
        selectedMeals = new boolean[foodManager.getMealNames().size()];
        selectedBreakfast= new boolean[foodManager.getBreakfastNames().size()];
        selectedSweets= new boolean[foodManager.getSweetsNames().size()];

        selectedFruitsSet = new HashSet<>();
        selectedMealsSet = new HashSet<>();
        selectedBreakfastSet=new HashSet<>();
        selectedSweetsSet= new HashSet<>();


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
                mealNames = foodManager.getMealNames();
                showMealDialog();
            }
        });

        // Select Card for Breakfast
        selectCardBreakfast=findViewById(R.id.selectCardsBreakfast);
        selectCardBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                breakfastNames=foodManager.getBreakfastNames();
                showBreakfastDialog();
            }
        });
        // Select Card for Sweets
        selectCardSweets=findViewById(R.id.selectCardSweets);
        selectCardSweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sweetsName=foodManager.getSweetsNames();
                showSweetsDialog();
            }
        });
        selectCardBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                breakfastNames=foodManager.getBreakfastNames();
                showBreakfastDialog();
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

        imageButtonPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodActivity.this, PersonalActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(FoodActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.show();
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
        fruitNames = foodManager.getFruitNames();
        String[] fruitArray = fruitNames.toArray(new String[0]);

        // Get the selected fruits from SharedPreferences
        selectedFruitsSet = getSelectedFruitsFromPrefs();

        boolean[] checkedItems = new boolean[fruitArray.length];
        for (int i = 0; i < fruitArray.length; i++) {
            checkedItems[i] = selectedFruitsSet.contains(fruitArray[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Food");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(fruitArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                String fruitName = fruitArray[which];
                if (isChecked) {
                    selectedFruitsSet.add(fruitName);
                } else {
                    selectedFruitsSet.remove(fruitName);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                saveSelectedFruitsToPrefs();
                updateSelectedFruitsTextView();
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
                selectedFruitsSet.clear();
                saveSelectedFruitsToPrefs();
                updateSelectedFruitsTextView();
                calculateTotalCalories();
            }
        });
        builder.show();
    }


    private void showMealDialog() {
        mealNames = foodManager.getMealNames();
        String[] mealArray = mealNames.toArray(new String[0]);
        selectedMealsSet = getSelectedMealsFromPrefs();
        boolean[] checkedItems = new boolean[mealArray.length];
        for (int i = 0; i < mealArray.length; i++) {
            checkedItems[i] = selectedMealsSet.contains(mealArray[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Meals");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(mealArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                String mealName = mealArray[which];
                if (isChecked) {
                    selectedMealsSet.add(mealName);
                } else {
                    selectedMealsSet.remove(mealName);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                saveSelectedMealsToPrefs();
                updateSelectedMealsTextView();
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
                selectedMealsSet.clear();
                saveSelectedMealsToPrefs();
                updateSelectedMealsTextView();
                calculateTotalCalories();
            }
        });
        builder.show();
    }

    private void showBreakfastDialog() {
        breakfastNames = foodManager.getBreakfastNames();
        String[] breakfastArray = breakfastNames.toArray(new String[0]);
        selectedBreakfastSet = getSelectedBreakfastFromPrefs();
        boolean[] checkedItems = new boolean[breakfastArray.length];
        for (int i = 0; i < breakfastArray.length; i++) {
            checkedItems[i] = selectedBreakfastSet.contains(breakfastArray[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Breakfast");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(breakfastArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                String breakfastName = breakfastArray[which];
                if (isChecked) {
                    selectedBreakfastSet.add(breakfastName);
                } else {
                    selectedBreakfastSet.remove(breakfastName);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                saveSelectedBreakfastToPrefs();
                updateSelectedBreakfastTextView();
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
                selectedBreakfastSet.clear();
                saveSelectedBreakfastToPrefs();
                updateSelectedBreakfastTextView();
                calculateTotalCalories();
            }
        });
        builder.show();
    }

    private void showSweetsDialog() {
        sweetsName = foodManager.getSweetsNames();
        String[] sweetArray = sweetsName.toArray(new String[0]);

        // Get the selected sweets from SharedPreferences
        selectedSweetsSet = getSelectedSweetsFromPrefs();

        boolean[] checkedItems = new boolean[sweetArray.length];
        for (int i = 0; i < sweetArray.length; i++) {
            checkedItems[i] = selectedSweetsSet.contains(sweetArray[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Sweets");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(sweetArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                String sweetName = sweetArray[which];
                if (isChecked) {
                    selectedSweetsSet.add(sweetName);
                } else {
                    selectedSweetsSet.remove(sweetName);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                saveSelectedSweetsToPrefs();
                updateSelectedSweetsTextView();
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
                selectedSweetsSet.clear();
                saveSelectedSweetsToPrefs();
                updateSelectedSweetsTextView();
                calculateTotalCalories();
            }
        });
        builder.show();
    }


    private void calculateTotalCalories() {
        int fruitCalories = 0;
        int mealCalories = 0;
        int breakfastCalories=0;
        int sweetsCalories=0;

        // Calculate the calories of selected fruits
        for (String fruitName : selectedFruitsSet) {
            fruitCalories += foodManager.getFruitCalories(fruitName);
        }
        // Calculate the calories of selected meals
        for (String mealName : selectedMealsSet) {
            mealCalories += foodManager.getMealCalories(mealName);
        }//breakfast
        for(String breakfastName: selectedBreakfastSet){
            breakfastCalories+=foodManager.getBreakfastCalories(breakfastName);
        }//sweets
        for(String sweetsName: selectedSweetsSet){
            sweetsCalories+=foodManager.getSweetsCalories(sweetsName);
        }

        // Sum up the total calories
        int totalCalories = fruitCalories + mealCalories + breakfastCalories + sweetsCalories;

        // Display the total calories in the TextView
        textViewCalories.setText("Total Calories: " + totalCalories);

        // Save the total calories in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("totalCalories", totalCalories);
        editor.apply();
        saveTotalCaloriesToPrefs();
    }

    private void saveSelectedFruitsToPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_FRUITS, selectedFruitsSet);
        editor.apply();
    }

    private void saveSelectedMealsToPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_MEALS, selectedMealsSet);
        editor.apply();
    }
    private void saveSelectedBreakfastToPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_BREAKFAST, selectedBreakfastSet);
        editor.apply();
    }
    private void saveSelectedSweetsToPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_SWEETS, selectedSweetsSet);
        editor.apply();
    }

    private void updateSelectedFruitsTextView() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String fruitName : selectedFruitsSet) {
            stringBuilder.append(fruitName).append(", ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        textViewFruit.setText(stringBuilder.toString());
    }

    private void updateSelectedMealsTextView() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String mealName : selectedMealsSet) {
            stringBuilder.append(mealName).append(", ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        textViewMeals.setText(stringBuilder.toString());
    }
    private void updateSelectedBreakfastTextView() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String breakfastName : selectedBreakfastSet) {
            stringBuilder.append(breakfastName).append(", ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        textViewBreakfast.setText(stringBuilder.toString());
    }
    private void updateSelectedSweetsTextView() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String sweetsName : selectedSweetsSet) {
            stringBuilder.append(sweetsName).append(", ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        textViewSweets.setText(stringBuilder.toString());
    }

    private Set<String> getSelectedFruitsFromPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getStringSet(PREF_SELECTED_FRUITS, new HashSet<>());
    }
    private Set<String> getSelectedMealsFromPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getStringSet(PREF_SELECTED_MEALS, new HashSet<>());
    }
    private Set<String> getSelectedBreakfastFromPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getStringSet(PREF_SELECTED_BREAKFAST, new HashSet<>());
    }
    private Set<String> getSelectedSweetsFromPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getStringSet(PREF_SELECTED_SWEETS, new HashSet<>());
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Speichern der ausgewählten Früchte, Mahlzeiten, Frühstuck, Süßigkeiten und Gesamtkalorien in den SharedPreferences
        saveSelectedFruitsToPrefs();
        saveSelectedMealsToPrefs();
        saveSelectedBreakfastToPrefs();
        saveSelectedSweetsToPrefs();
        saveTotalCaloriesToPrefs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Wiederherstellen der ausgewählten Früchte, Mahlzeiten und Gesamtkalorien aus den SharedPreferences
        selectedFruitsSet = getSelectedFruitsFromPrefs();
        selectedMealsSet = getSelectedMealsFromPrefs();
        selectedBreakfastSet=getSelectedBreakfastFromPrefs();
        selectedSweetsSet=getSelectedSweetsFromPrefs();
        totalCalories = getTotalCaloriesFromPrefs();

        // Aktualisieren der TextViews mit den wiederhergestellten Werten
        updateSelectedFruitsTextView();
        updateSelectedMealsTextView();
        updateSelectedBreakfastTextView();
        updateSelectedSweetsTextView();
        textViewCalories.setText("Total Calories: " + totalCalories);
        calculateTotalCalories();
    }

    private void saveTotalCaloriesToPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_TOTAL_CALORIES_FOOD, totalCalories);
        editor.apply();
    }

    private int getTotalCaloriesFromPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(PREF_TOTAL_CALORIES_FOOD, 0);
    }




    //aller letzte Klammer
}