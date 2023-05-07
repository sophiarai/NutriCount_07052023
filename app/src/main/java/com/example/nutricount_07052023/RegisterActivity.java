package com.example.nutricount_07052023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    public Button button;
    public TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button= (Button) findViewById(R.id.loginRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }});



        button=(Button) findViewById(R.id.buttonInfo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegisterActivity.this, InfoActivity.class);
                startActivity(intent);
            }});

        textView=(TextView) findViewById(R.id.bmiTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starte die zweite Aktivität
                Intent intent = new Intent(RegisterActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        button=(Button) findViewById(R.id.buttonRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegisterActivity.this, BottomNavActivity.class);
                startActivity(intent);
            }});



    }}



