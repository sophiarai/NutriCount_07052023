package com.example.nutricount_07052023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private ListView notes_listView;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_notes);

        bottomNavigationView.setOnItemSelectedListener(item->{
            switch (item.getItemId()){
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
                    startActivity(new Intent(getApplicationContext(), FoodActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_notes:
                    return true;
            }
            return false;
        });

        notes_listView = findViewById(R.id.notes_listView);

        notesList = new ArrayList<>();

       notesAdapter = new NotesAdapter(this, notesList);
        notes_listView.setAdapter((ListAdapter) notesAdapter);

        //Datum
        Calendar kalender = Calendar.getInstance();
        SimpleDateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy");
        String datum = datumsFormat.format(kalender.getTime());

        notesList.add(new Note(datum, "Kalorien Gesamt: "+"400" ,"Verbrannte Kalorien: "+"150", "Gegessene Kalorien: "+"250"));
        notesList.add(new Note(datum, "Kalorien Gesamt: "+"500" ,"Verbrannte Kalorien: "+"150", "Gegessene Kalorien: "+"250"));




    }
}