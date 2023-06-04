package com.example.nutricount_07052023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nutricount_07052023.Database.NoteDao;
import com.example.nutricount_07052023.Database.NoteDatabase;
import com.example.nutricount_07052023.Database.NoteEntity;
import com.example.nutricount_07052023.Database.UserDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BottomNavActivity extends AppCompatActivity {

    Button btn_submit;
    TextInputLayout dateInputLayout;
    TextInputEditText dateEditText;
    TextView diffTextView, textView_welcome, bottomNavTotalCaloriesTextView, bottomNavTotalLostCalories;
    private static final String DATE_PREF_KEY = "current_date";
    private static final String DIFF_PREF_KEY = "diff_calories";
    ImageButton imageButtonPersonal, imageButtonLogout;
    SharedPreferences sharedPreferences;

    NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        Log.d("ActivityLifecycle", "BottomNavActivity onCreate");

        imageButtonPersonal = findViewById(R.id.btnPersonal);
        imageButtonLogout = findViewById(R.id.imageButton_logout);
        textView_welcome = findViewById(R.id.welcome_message);
        dateInputLayout = findViewById(R.id.dateInputLayout);
        dateEditText = findViewById(R.id.dateEditText);
        bottomNavTotalLostCalories = findViewById(R.id.textViewTrySport);
        bottomNavTotalCaloriesTextView = findViewById(R.id.textViewTryFood);
        btn_submit = findViewById(R.id.buttonsubmit);
        diffTextView = findViewById(R.id.textViewDifference);

        // Aktuelles Datum erhalten
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        // Datum in das Textfeld einfügen
        dateEditText.setText(currentDate);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Welcome Message
        String username = getIntent().getStringExtra("USERNAME");
        textView_welcome.setText("Welcome " + username + "!");

        noteDatabase = NoteDatabase.getNoteDatabase(this);

        // Willkommensnachricht aus den SharedPreferences abrufen und anzeigen
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        // Gesamtkalorien (gained) aus den SharedPreferences abrufen und anzeigen
        int totalCalories = sharedPreferences.getInt("totalCalories", 0);
        bottomNavTotalCaloriesTextView.setText("Gained calories: " + totalCalories);
        // Gesamtkalorien (lost) aus den SharedPreferences abrufen und anzeigen
        String totalCaloriesLostString = sharedPreferences.getString("total_calories_sport", "");
        int totalCaloriesLost = 0;
        try {
            totalCaloriesLost = Integer.parseInt(totalCaloriesLostString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
                Log.d("ButtonClick", "Button submit clicked");
                /*
                // Gesamtkalorien (gained) aus den SharedPreferences abrufen
                int gainedCalories = sharedPreferences.getInt("totalCalories", 0);
                // Gesamtkalorien (lost) aus den SharedPreferences abrufen
                int lostCalories = sharedPreferences.getInt("total_calories_sport", 0);
                // Differenz berechnen
                int result = gainedCalories - lostCalories;
                Log.d("TotalGainedCalories", "Total gained calories: " + result);
                // Ergebnis anzeigen
                diffTextView.setText("Total gained calories: " + result);
                // Speichern des Ergebniswerts in den SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(DIFF_PREF_KEY, result);
                editor.apply();
                // Hole den gespeicherten Wert des Kalorienlimits aus den SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);

                float calorieLimit = prefs.getFloat("calorieLimit", 0.0f);
                // Überprüfen, ob die gesammelten Kalorien die Kaloriengrenze überschreiten
                if (result > calorieLimit) {
                    // Erstellen und Anzeigen eines AlertDialogs
                    AlertDialog.Builder builder = new AlertDialog.Builder(BottomNavActivity.this);
                    builder.setTitle("Attention");
                    builder.setMessage("You have exceeded your daily calorie limit.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                } else {
                    // Wenn das Ergebnis nicht größer als die Kaloriengrenze ist, wird kein AlertDialog angezeigt
                 */

                submitCalories();
                }});

        // Wiederherstellen des gespeicherten Werts für das diffTextView
        int savedResult = sharedPreferences.getInt(DIFF_PREF_KEY, 0);
        diffTextView.setText("Total gained calories: " + savedResult);
        imageButtonPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BottomNavActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });
        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
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
    }

    private void submitCalories ()
    {
        NoteEntity note;
        NoteDao noteDao = noteDatabase.getNoteDao();
/*
        // Gesamtkalorien (gained) aus den SharedPreferences abrufen
        int gainedCalories = sharedPreferences.getInt("totalCalories", 0);

        // Gesamtkalorien (lost) aus den SharedPreferences abrufen
        String sTotalCaloriesSport = sharedPreferences.getString("total_calories_sport", "");
        int lostCalories = Integer.parseInt(sTotalCaloriesSport);
*/

        String date = dateEditText.getText().toString();
        List<NoteEntity> list = noteDao.getNotesByUserDay("xxx", date);

        if (list.isEmpty())
        {
            int gainedCalories = 999;
            int lostCalories = 666;
            // public NoteEntity (String day, String username, int allCalories, int burnedCalories, int consumedCalories)
            note = new NoteEntity(date, "xxx", gainedCalories - lostCalories, lostCalories, gainedCalories);

            noteDao.insertNote(note);
        }
        else {
            // TODO: Fehler ausgeben
            Log.d("ActivityLifecycle", "Für User " + "'xxx'" + " und Datum=" + date + " existiert bereits ein Eintrag");

        }
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

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(BottomNavActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Das ausgewählte Datum in das Textfeld einfügen
                        String selectedDate = String.format(Locale.getDefault(), "%02d.%02d.%04d",
                                dayOfMonth, month + 1, year);
                        dateEditText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}
