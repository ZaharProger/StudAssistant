package com.example.studassistant;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText nameField;
    EditText surnameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File personalFile = new File(getApplicationContext().getFilesDir() + "/personal.txt");
        if (personalFile.exists()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else{
            setContentView(R.layout.activity_login);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setTitle("Авторизация");

            nameField = findViewById(R.id.nameField);
            surnameField = findViewById(R.id.surnameField);

            nameField.setText("");
            surnameField.setText("");

            findViewById(R.id.loginButton).setOnClickListener(this);
        }

    }
    @Override
    public void onClick(View view) {
        if (!(nameField.getText().toString().equalsIgnoreCase("") ||
        surnameField.getText().toString().equalsIgnoreCase(""))){

            File personalFile = new File(getApplicationContext().getFilesDir() + "/personal.txt");
            if (!personalFile.exists()){
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(getApplicationContext().getFilesDir() + "/personal.txt"))){
                    writer.write(nameField.getText() + "\n" + surnameField.getText());
                }
                catch(IOException exception){

                }
            }

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else
            Toast.makeText(getApplicationContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
    }
}
