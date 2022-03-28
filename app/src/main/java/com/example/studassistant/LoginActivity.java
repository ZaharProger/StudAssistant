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

        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Авторизация");

        nameField = findViewById(R.id.nameField);
        surnameField = findViewById(R.id.surnameField);

        findViewById(R.id.loginButton).setOnClickListener(this);

        File personalFile = new File(getApplicationContext().getFilesDir() + "/personal.txt");
        if (personalFile.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(getApplicationContext().getFilesDir() + "/personal.txt"))){
                ArrayList<String> personalData = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null)
                    personalData.add(line);

                nameField.setText(personalData.get(0));
                surnameField.setText(personalData.get(1));

                onClick(findViewById(R.id.loginButton));
            }
            catch(IOException exception){

            }
        }
        else{
            nameField.setText("");
            surnameField.setText("");
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