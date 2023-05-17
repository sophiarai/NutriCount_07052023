package com.example.nutricount_07052023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SportActivity extends AppCompatActivity {


    Button btndate;
    TextView textViewSport, textViewDate, textViewkcalSport;
    MaterialCardView selectCardSport;
    boolean[] selectedsport;
    ArrayList<Integer> sportlist = new ArrayList<>();

    String[] sportArray={"Cardio 30min", "Cardio 1h", "Running 30min", "Running 1h"};
    int[] sportCaloriesArray = {200, 220, 100, 150};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        textViewkcalSport=findViewById(R.id.kcal_sport);

        selectCardSport=findViewById(R.id.selectCardSport);
        textViewSport=findViewById(R.id.tvSport);
        selectedsport= new boolean[sportArray.length];
        selectCardSport.setOnClickListener(view -> {
            showSportDialog();
        });



        //BottomNavigation wird in SportActivity angezeigt:
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_sport);

        bottomNavigationView.setOnItemSelectedListener(item->{
            switch (item.getItemId()){
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

    }
    private void showSportDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(SportActivity.this);
        builder.setTitle("Select Food");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(sportArray, selectedsport, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    sportlist.add(which);
                }else{
                    sportlist.remove(Integer.valueOf(which));
                }}
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder=new StringBuilder();
                for(int i=0; i< sportlist.size(); i++){
                    stringBuilder.append(sportArray[sportlist.get(i)]);

                    if(i!=sportlist.size()-1){
                        //when i value not equal to food list, then add comma
                        stringBuilder.append(", ");
                    }
                    textViewSport.setText(stringBuilder.toString());
                 }
                updateTotalCalories();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for(int i=0; i< selectedsport.length; i++) {
                    selectedsport[i] = false;
                }
                sportlist.clear();
                textViewSport.setText("");
                updateTotalCalories();

            }
        });
        builder.show();
    }
    private void updateTotalCalories() {
        int totalCalories2 = calculateTotalCalories();
        textViewkcalSport.setText("Lost calories: " + totalCalories2);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("totalCalories2", totalCalories2);
        editor.apply();
    }
    private int calculateTotalCalories() {
        int totalCalories = 0;
        for (int sportIndex : sportlist) {
            totalCalories += sportCaloriesArray[sportIndex];
        }
        return totalCalories;
    }


//aller letzte Klammer

}