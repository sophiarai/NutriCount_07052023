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

import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FoodActivity extends AppCompatActivity {
    Button  btnscan, btndate;

    TextView textViewFruit, textViewDate, textViewMeals, textViewCalories;

    MaterialCardView selectCard, selectCardMeals;
    boolean [] selectedfruits, selectedmeals;

    ArrayList<Integer> fruitslist= new ArrayList<>();
    ArrayList<Integer> mealList= new ArrayList<>();
    String[] fruitArray={"Apple", "Banana", "Orange", "Mango"};
    String [] mealArray={"Spaghetti", "Fish", "Lasagne", "Toast"};

    int[] fruitCaloriesArray = {50, 100, 70, 60}; // Beispiel-Kalorienwerte für Früchte
    int[] mealCaloriesArray = {300, 200, 400, 150}; // Beispiel-Kalorienwerte für Mahlzeiten

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        //fruits
        selectCard= findViewById(R.id.selectCard);
        textViewFruit=findViewById(R.id.tvFood);
        selectedfruits=new boolean[fruitArray.length];
        selectCard.setOnClickListener(v ->{
            showFruitDialog();

            // meals
            selectCardMeals=findViewById(R.id.selectCardsMeal);
            textViewMeals=findViewById(R.id.tvMeals);
            selectedmeals= new boolean[mealArray.length];
            selectCardMeals.setOnClickListener(view -> {
                showMealDialog();
            });
            calculateTotalCalories();
        } );
        textViewCalories = findViewById(R.id.kcal_textview);

        //QRCode Scanner
        btnscan=(Button)findViewById(R.id.buttonScanner);
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });




        //Heutiges Datum anzeigen lassen
        //textViewDate = (TextView) findViewById(R.id.textView_date2);
       // btndate = (Button) findViewById(R.id.button_date);
       // btndate.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
               // Calendar kalender= Calendar.getInstance();
                //SimpleDateFormat datumsFormat=new SimpleDateFormat("dd.MM.yyyy");
                //textViewDate.setText(datumsFormat.format(kalender.getTime()));
            //}});



        //BottomNav in Activity anzeigen
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_food);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
                    startActivity(new Intent(getApplicationContext(),NotesActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

    }
    //ende von oncreate
    //-> hier kommen alle neue Methoden:
    private void scanCode() {
        ScanOptions options= new ScanOptions();
        options.setPrompt("Volume up to flash on!");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher= registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents()!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }});

    private void showFruitDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Food");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(fruitArray, selectedfruits, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    fruitslist.add(which);
                }else{
                    fruitslist.remove(Integer.valueOf(which));
                }}
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder=new StringBuilder();
                for(int i=0; i< fruitslist.size(); i++){
                    stringBuilder.append(fruitArray[fruitslist.get(i)]);

                    if(i!=fruitslist.size()-1){
                        //when i value not equal to food list, then add comma
                        stringBuilder.append(", ");
                    }
                    textViewFruit.setText(stringBuilder.toString());
                    calculateTotalCalories();
                }}
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for(int i=0; i< selectedfruits.length; i++) {
                    selectedfruits[i] = false;
                }
                    fruitslist.clear();
                    textViewFruit.setText("");
                calculateTotalCalories();
            }
        });
        builder.show();
    }

    private void showMealDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Select Meals");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(mealArray, selectedmeals, new DialogInterface.OnMultiChoiceClickListener() {
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
                for (int i = 0; i < selectedmeals.length; i++) {
                    selectedmeals[i] = false;
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

        // Berechne die Kalorien der ausgewählten Früchte
        for (int fruitIndex : fruitslist) {
            fruitCalories += fruitCaloriesArray[fruitIndex];
        }

        // Berechne die Kalorien der ausgewählten Mahlzeiten
        for (int mealIndex : mealList) {
            mealCalories += mealCaloriesArray[mealIndex];
        }

        // Summiere die Gesamtkalorien
        int totalCalories = fruitCalories + mealCalories;

        // Zeige die Gesamtkalorien im TextView an
        textViewCalories.setText("Total Calories: " + totalCalories);

        // Speichere die Gesamtkalorien in den SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("totalCalories", totalCalories);
        editor.apply();

    }





    //aller letzte Klammer
}
