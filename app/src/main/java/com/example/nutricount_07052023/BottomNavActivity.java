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

        bottomNavTotalLostCalories = findViewById(R.id.textViewTrySport);
        bottomNavTotalCaloriesTextView = findViewById(R.id.textViewTryFood);
        btn_submit = findViewById(R.id.buttonsubmit);
        diffTextView = findViewById(R.id.textViewDifference);

        // Gesamtkalorien (gained) aus den SharedPreferences abrufen und anzeigen
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int totalCalories = sharedPreferences.getInt("totalCalories", 0);
        bottomNavTotalCaloriesTextView.setText("Gained calories: " + totalCalories);

        // Gesamtkalorien (lost) aus den SharedPreferences abrufen und anzeigen
        int totalCaloriesLost = sharedPreferences.getInt("total_calories_sport", 0);
        bottomNavTotalLostCalories.setText("Lost calories: " + totalCaloriesLost);

        // Überprüfen, ob ein übergebener Kalorienwert vorhanden ist
        if (getIntent().hasExtra("totalCaloriesSport")) {
            int totalCaloriesSport = getIntent().getIntExtra("totalCaloriesSport", 0);
            bottomNavTotalLostCalories.setText("Lost calories: " + totalCaloriesSport);

            // Speichern des übergebenen Kalorienwerts in den SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("total_calories_sport", totalCaloriesSport);
            editor.apply();
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gesamtkalorien (gained) aus den SharedPreferences abrufen
                int gainedCalories = sharedPreferences.getInt("totalCalories", 0);

                // Gesamtkalorien (lost) aus den SharedPreferences abrufen
                int lostCalories = sharedPreferences.getInt("total_calories_sport", 0);

                // Differenz berechnen
                int result = gainedCalories - lostCalories;

                // Ergebnis anzeigen
                diffTextView.setText("Total gained calories: " + result);
            }
        });

        imageButtonLogout = findViewById(R.id.imageButton_logout);

        String username = getIntent().getStringExtra("USERNAME");
        textView_welcome = findViewById(R.id.welcome_message);
        textView_welcome.setText("Welcome " + username + "!");

        //Heutiges Datum anzeigen lassen
        textView_datum = findViewById(R.id.textView_date2);
        btn_date = findViewById(R.id.button_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalender = Calendar.getInstance();
                SimpleDateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy");
                textView_datum.setText(datumsFormat.format(kalender.getTime()));
            }
        });

        imageButtonPersonal = findViewById(R.id.btnPersonal);
        imageButtonPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BottomNavActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
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
}