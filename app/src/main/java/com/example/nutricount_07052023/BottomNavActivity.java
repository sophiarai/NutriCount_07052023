package com.example.nutricount_07052023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BottomNavActivity extends AppCompatActivity {
    TextView diffTextView;
    Button btn_date, btn_submit;
    TextView textView_datum, textView_welcome, bottomNavTotalCaloriesTextView, bottomNavTotalLostCalories;

ImageButton imageButtonPersonal, imageButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        btn_submit=findViewById(R.id.buttonsubmit);
        diffTextView=findViewById(R.id.textViewDifference);

        // Gesamtkalorien (gained) aus den SharedPreferences abrufen und anzeigen
         bottomNavTotalCaloriesTextView = findViewById(R.id.textViewTryFood);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int totalCalories = sharedPreferences.getInt("totalCalories", 0);
        bottomNavTotalCaloriesTextView.setText("Gained calories: " + totalCalories);

        // Gesamtkalorien (lost) aus den SharedPreferences abrufen und anzeigen
         bottomNavTotalLostCalories= findViewById(R.id.textViewTrySport);
         SharedPreferences sharedPreferences1=PreferenceManager.getDefaultSharedPreferences(this);
         int totalCalories2= sharedPreferences1.getInt("totalCalories2", 0);
         bottomNavTotalLostCalories.setText("Lost calories: "+ totalCalories2);

         btn_submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // Gesamtkalorien (gained) aus den SharedPreferences abrufen
                 SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BottomNavActivity.this);
                 int gainedCalories = sharedPreferences.getInt("totalCalories", 0);

                 // Gesamtkalorien (lost) aus den SharedPreferences abrufen
                 SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(BottomNavActivity.this);
                 int lostCalories = sharedPreferences1.getInt("totalCalories2", 0);

                 // Differenz berechnen
                 int result = gainedCalories - lostCalories;

                 // Ergebnis anzeigen
                 diffTextView.setText("Total gained calories: " + result);
             }});


        imageButtonLogout=(ImageButton)findViewById(R.id.imageButton_logout);


        String username=getIntent().getStringExtra("USERNAME");
        textView_welcome= (TextView) findViewById(R.id.welcome_message);
        textView_welcome.setText("Welcome "+username+"!");


        String username2=getIntent().getStringExtra("username");
        textView_welcome= (TextView) findViewById(R.id.welcome_message);
        textView_welcome.setText("Welcome "+username2+"!");



        //Heutiges Datum anzeigen lassen
        textView_datum = (TextView) findViewById(R.id.textView_date2);
        btn_date = (Button) findViewById(R.id.button_date);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalender = Calendar.getInstance();
                SimpleDateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy");
                textView_datum.setText(datumsFormat.format(kalender.getTime()));
            }
        });


        //TextView burnedKcalTextView = findViewById(R.id.textViewTrySport);
        //int burned_Calories = BurnedCalories.getInstance().getBurnedCalories();
       // burnedKcalTextView.setText(String.format("Burned calories: %d kcal", burned_Calories));


       // TextView gainedKcalTextView = findViewById(R.id.textViewTryFood);
        //int gained_calories = GainedCalories.getInstance().getGainedCalories();
        //gainedKcalTextView.setText(String.format("Gained calories: %d kcal", gained_calories));

        //int netto_kalorien = gained_calories - burned_Calories;
       // diffTextView = (TextView)findViewById(R.id.textViewDifference);
        //diffTextView.setText(String.format("Netto-Kalorien: %d kcal", netto_kalorien));




        imageButtonPersonal=(ImageButton) findViewById(R.id.btnPersonal);
        imageButtonPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent (BottomNavActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item->{
            switch (item.getItemId()){
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_sport:
                    startActivity(new Intent(getApplicationContext(), SportActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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
                Intent intent = new Intent(BottomNavActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.show();
}

//letzte Klammer
}