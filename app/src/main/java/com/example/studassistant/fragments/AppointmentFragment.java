package com.example.studassistant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studassistant.R;

public class AppointmentFragment extends Fragment implements View.OnClickListener {

    private ImageButton dateButton;
    private ImageButton timeButton;
    private Button confirmButton;
    private DateFragment dateFragment;
    private TimeFragment timeFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment, container, false);

        dateButton = view.findViewById(R.id.dateButton);
        timeButton = view.findViewById(R.id.timeButton);
        confirmButton = view.findViewById(R.id.confirmButton);

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        dateFragment = new DateFragment();
        timeFragment = new TimeFragment();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dateButton)
            dateFragment.show(getParentFragmentManager(), "Date");
        else if (view.getId() == R.id.timeButton)
            timeFragment.show(getParentFragmentManager(), "Time");
    }
}
