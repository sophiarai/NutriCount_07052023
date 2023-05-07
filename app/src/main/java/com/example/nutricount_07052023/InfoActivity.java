package com.example.nutricount_07052023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class InfoActivity extends AppCompatActivity {


    String bmilist [] = {"<= 16,0 :     starkes Untergewicht", "16,0-17,0 :     mäßiges Untergewicht", "17,0-18,5 :     leichtes Untergewicht",
            "18,5-25,0 :    NormalGewicht", "25,0-30,0 :    Präadipositas", "30,0-35,0 :    Adipositas Grad 1",
            "35,0-40,0 :    Adipositas Grad 2", ">=40,0 :   Adipositas Grad 3", " ", " ",
            "Kalorienbedarf berechnen->", };
    ListView listView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listView=(ListView) findViewById(R.id.listView);
        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView_listView,bmilist);
        listView.setAdapter(arrayAdapter);

        button=(Button) findViewById(R.id.myButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(InfoActivity.this, RegisterActivity.class);
                startActivity(intent);
            }});

    }}