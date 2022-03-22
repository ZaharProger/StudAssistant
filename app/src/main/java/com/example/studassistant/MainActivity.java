package com.example.studassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studassistant.fragments.AboutFragment;
import com.example.studassistant.fragments.AppointmentFragment;
import com.example.studassistant.fragments.LikedFragment;
import com.example.studassistant.fragments.MyAppointmentFragment;
import com.example.studassistant.fragments.StartFragment;
import com.example.studassistant.managers.RequestManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private StartFragment startFragment;
    private AboutFragment aboutFragment;
    private AppointmentFragment appointmentFragment;
    private MyAppointmentFragment myAppointmentFragment;
    private LikedFragment likedFragment;
    private RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Студ Ассистент");

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
        appointmentFragment = new AppointmentFragment(null);
        myAppointmentFragment = new MyAppointmentFragment();
        likedFragment = new LikedFragment();

        requestManager = new RequestManager(getApplicationContext(), null);

        getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, startFragment).commit();
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (!startFragment.isAdded()){
            if (likedFragment.isHidden()){
                getSupportFragmentManager().popBackStack();
                likedFragment.updateList();
                likedFragment.onResume();

                getSupportFragmentManager().beginTransaction().show(likedFragment).commit();
            }
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, startFragment).commit();
        }
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

        if (likedFragment.isHidden())
            getSupportFragmentManager().popBackStack();

        if (item.getItemId() == R.id.about_option)
            getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, aboutFragment).commit();
        else if (item.getItemId() == R.id.appointment_option)
            getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, appointmentFragment).commit();
        else if (item.getItemId() == R.id.my_appointment_option){
            if (requestManager.checkConnection())
                getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, myAppointmentFragment).commit();
            else
                Toast.makeText(getApplicationContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
        }
        else if (item.getItemId() == R.id.liked_option){
            if (likedFragment.isHidden()){
                likedFragment.updateList();
                likedFragment.onResume();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.windowContainer, likedFragment)
                                                        .show(likedFragment)
                                                        .commit();
        }

        return true;
    }
}