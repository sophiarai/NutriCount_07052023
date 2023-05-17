package com.example.nutricount_07052023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

public class PersonalActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME="MyPrefsFile";
   public Button btninfo, btnback, btnsubmit, btncalculate;
   EditText editTextheight, editTextweight;
   Spinner spinner;
   DatePicker datePicker;
   RadioButton radioButton;
   TextView textViewresult, textViewKaloriengrenze;
   RadioGroup radioGroup;

   SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        editTextheight=(EditText)findViewById(R.id.inputGröße);
        editTextweight=(EditText)findViewById(R.id.inputGewicht);
        textViewresult=(TextView)findViewById(R.id.bmiTextView);
        btnsubmit=(Button) findViewById(R.id.btnSubmit);
        datePicker=(DatePicker)findViewById(R.id.datePicker);
        radioGroup=(RadioGroup)findViewById(R.id.genderRadioGroup);
        textViewKaloriengrenze=(TextView)findViewById(R.id.inputKaloriengrenze);
        prefs=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        btncalculate=(Button) findViewById(R.id.buttoncalculate);

        // Lade die gespeicherten Daten, falls vorhanden
        String heightText = prefs.getString("height", "");
        String weightText = prefs.getString("weight", "");
        int day = prefs.getInt("day", 1);
        int month = prefs.getInt("month", 1);
        int year = prefs.getInt("year", 2000);
        int genderId = prefs.getInt("gender", -1);

        // Setze die gespeicherten Daten in die Views
        editTextheight.setText(heightText);
        editTextweight.setText(weightText);
        datePicker.updateDate(year, month, day);
        if (genderId != -1) {
            radioGroup.check(genderId);
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String heightText= editTextheight.getText().toString();
                String weightText=editTextweight.getText().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                int genderId = radioGroup.getCheckedRadioButtonId();
                if(!TextUtils.isEmpty(heightText)&& !TextUtils.isEmpty(weightText)){
                    double height = Double.parseDouble(heightText);
                    double weight= Double.parseDouble(weightText);

                    // Berechne den BMI im Hintergrund
                    double bmi = calculateBMI(height, weight);

                    // Zeige das Ergebnis im TextView an
                    textViewresult.setText(String.format("Your BMI: %.1f", bmi));


                    // Speichere die Daten in den SharedPreferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("height", heightText);
                    editor.putString("weight", weightText);
                    editor.putInt("day", day);
                    editor.putInt("month", month);
                    editor.putInt("year", year);
                    editor.putInt("gender", genderId);
                    editor.putFloat("bmi", (float)bmi);
                    editor.apply();
                }

            }
        });

        btninfo=(Button) findViewById(R.id.buttonInfo);
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, InfoActivity.class);
                startActivity(intent);
            }});

        btnback=(Button) findViewById(R.id.buttonBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, BottomNavActivity.class);
                startActivity(intent);
            }});

        btncalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateCalorie();
            }
        });


    }
    private double calculateBMI(double height, double weight) {
        // Berechne den BMI: Gewicht in Kilogramm geteilt durch das Quadrat der Größe in Metern
        double heightInMeters = height / 100.0;
        double bmi = weight / (heightInMeters * heightInMeters);

        // Überprüfe, ob der berechnete BMI größer als 25 ist
        if (bmi > 25) {
            // Erstelle einen Alert Dialog mit der Meldung "Achtung BMI zu hoch"
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attention");
            builder.setMessage("BMI too high");
            builder.setPositiveButton("OK", null);
            builder.create().show();
        }

        return bmi;
    }
    private void calculateCalorie() {
        double weight = Double.parseDouble(editTextweight.getText().toString());
        double height = Double.parseDouble(editTextheight.getText().toString());
        int age = calculateAge(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        double bmr = calculateBMR(weight, height, age);

        double calorieLimit = bmr * 1.2; // multiply BMR by 1.2 to get calorie limit

        textViewKaloriengrenze.setText("calorie limit per day: " + String.format("%.0f", calorieLimit) );
        textViewKaloriengrenze.setVisibility(View.VISIBLE);
    }

    private int calculateAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private double calculateBMR(double weight, double height, int age) {
        return 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
    }
}

