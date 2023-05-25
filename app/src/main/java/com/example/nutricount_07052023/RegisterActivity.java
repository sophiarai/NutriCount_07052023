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

import com.example.nutricount_07052023.Database.UserDatabase;
import com.example.nutricount_07052023.Database.UserEntity;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonregister, buttonlogin;
    private EditText username, email, password, repassword;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.inputUsername);
        email = findViewById(R.id.inputEmailRegister);
        password = findViewById(R.id.inputPasswordRegister);
        repassword = findViewById(R.id.inputConformPasswordRegister);
        buttonregister = findViewById(R.id.loginRegister);
        buttonlogin = findViewById(R.id.buttonRegister);
        userDatabase = UserDatabase.getUserDatabase(this);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Überprüfe, ob der Benutzer bereits vorhanden ist
                                UserEntity existingUser = userDatabase.userDao().getUserByUsername(user);
                                if (existingUser != null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "User already exists, please sign up", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // Erstelle eine Instanz der UserEntity und setze die Werte
                                    UserEntity userEntity = new UserEntity();
                                    userEntity.setUsername(user);
                                    userEntity.setUseremail(email.getText().toString());
                                    userEntity.setUserpassword(pass);
                                    userEntity.setUserrepassword(repass);

                                    // Füge den Benutzer zur Datenbank hinzu
                                    userDatabase.userDao().registerUser(userEntity);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, BottomNavActivity.class);
                                            intent.putExtra("USERNAME", user); // Übergebe den Benutzernamen an die Intent
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }).start();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Validiere das Passwort
    private boolean validatePassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-_])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }
}


