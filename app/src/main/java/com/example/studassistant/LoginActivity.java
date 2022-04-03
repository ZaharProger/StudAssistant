package com.example.studassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studassistant.constants.UserDataValues;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText nameField;
    EditText surnameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(UserDataValues.PREFERENCES_NAME, MODE_PRIVATE);
        if (preferences.contains(UserDataValues.USER_NAME) && preferences.contains(UserDataValues.USER_SURNAME)){
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

            SharedPreferences preferences = getSharedPreferences(UserDataValues.PREFERENCES_NAME, MODE_PRIVATE);
            preferences.edit().putString(UserDataValues.USER_NAME, nameField.getText().toString().trim()).apply();
            preferences.edit().putString(UserDataValues.USER_SURNAME, surnameField.getText().toString().trim()).apply();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else
            Toast.makeText(getApplicationContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
    }
}
