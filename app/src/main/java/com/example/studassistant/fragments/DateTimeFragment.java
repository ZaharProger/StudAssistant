package com.example.studassistant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;

public class DateTimeFragment extends DialogFragment implements View.OnClickListener {

    private TextView appointmentLabel;
    private Appointment appointment;
    private Spinner datesList;
    private Spinner timeList;

    public DateTimeFragment(Appointment appointment, TextView appointmentLabel){
        this.appointment = appointment;
        this.appointmentLabel = appointmentLabel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_datetime, container, false);

        datesList = view.findViewById(R.id.datesList);
        timeList = view.findViewById(R.id.tutorsList);

        view.findViewById(R.id.dateTimeOkButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
    }
}
