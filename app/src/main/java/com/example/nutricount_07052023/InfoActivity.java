package com.example.nutricount_07052023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;



public class InfoActivity extends AppCompatActivity {


    String bmilist [] = {"  <= 16,0 :     severely underweight", "  16,0-17,0 :     moderately underweight", "  17,0-18,5 :     leichtes Untergewicht",
            "  18,5-25,0 :    normal weight", "  25,0-30,0 :    pre-obesity", "  30,0-35,0 :    Grade 1 obesity",
            "  35,0-40,0 :    Grade 2 obesity", "  >=40,0 :   Grade 3 obesity",};


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
                Intent intent= new Intent(InfoActivity.this, PersonalActivity.class);
                startActivity(intent);
            }});

    }}