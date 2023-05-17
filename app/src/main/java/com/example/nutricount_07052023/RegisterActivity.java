package com.example.nutricount_07052023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class RegisterActivity extends AppCompatActivity {

    public Button buttonregister, buttonlogin, buttoninfo;
    public TextView textView;
    EditText username, email, password, repassword;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=(EditText) findViewById(R.id.inputUsername);
        email=(EditText) findViewById(R.id.inputEmailRegister);
        password=(EditText) findViewById(R.id.inputPasswordRegister);
        repassword=(EditText) findViewById(R.id.inputConformPasswordRegister);
       buttonregister= (Button) findViewById(R.id.loginRegister);
        buttonlogin=(Button) findViewById(R.id.buttonRegister);
        db=new DatabaseHelper(this);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user= username.getText().toString();
                String pass= password. getText().toString();
                String repass= repassword.getText().toString();

                if(user.equals("")|| pass.equals("")|| repass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if(pass.equals(repass)){
                        Boolean checkuser= db.checkusername(user);
                        if(checkuser==false){
                            if(validatePassword(pass)) { // Validiere das Passwort
                                Boolean insert= db.insertData(user,pass);
                                if(insert==true){
                                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(RegisterActivity.this, BottomNavActivity.class);
                                    intent.putExtra("username", username.getText().toString()); // Pass the username to the intent
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Password must contain at least one uppercase letter, one number, one special character, and be at least 8 characters long", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "User already exists, please sign up", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }});

    }
    // Validiere das Passwort
    private boolean validatePassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-_])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }



}



