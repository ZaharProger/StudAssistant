package com.example.studassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studassistant.fragments.AboutFragment;
import com.example.studassistant.fragments.StartFragment;
import com.example.studassistant.managers.EmailManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private StartFragment startFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.mainWindow);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,
                R.string.options_opened, R.string.options_closed);

        NavigationView navigationView = findViewById(R.id.optionsMenu);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBarDrawerToggle.setToolbarNavigationClickListener(this);
        actionBarDrawerToggle.syncState();

        startFragment = new StartFragment();
        aboutFragment = new AboutFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, startFragment).commit();
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onBackPressed();

        if (item.getItemId() == R.id.about_option)
            getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, aboutFragment).commit();

        return true;
    }
}