package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.managers.Appointment;

public class PersonalFragment extends DialogFragment implements View.OnClickListener {
    private EditText nameField;
    private EditText surnameField;
    private Spinner groupList;
    private Appointment appointment;
    private TextView appointmentLabel;

    public PersonalFragment(Appointment appointment, TextView appointmentLabel){
        this.appointment = appointment;
        this.appointmentLabel = appointmentLabel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal, container, false);



        nameField = view.findViewById(R.id.nameField);
        surnameField = view.findViewById(R.id.surnameField);
        groupList = view.findViewById(R.id.groupList);

        view.findViewById(R.id.personalOkButton).setOnClickListener(this);
        nameField.setOnClickListener(this);
        surnameField.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        appointment.setName(nameField.getText().toString());
        appointment.setSurname(surnameField.getText().toString());

        if (!(appointment.getName().equals("") || appointment.getSurname().equals(""))){
            String currentAppointment = appointmentLabel.getText().toString();
            StringBuilder preparedAppointment = new StringBuilder();
            if (currentAppointment.length() == 0)
                preparedAppointment.append(appointment.getName()).append(" ").append(appointment.getSurname()).append(" ");
            else{
                String[] splittedAppointment = currentAppointment.trim().split("[\\s]+");
                splittedAppointment[0] = appointment.getName();
                splittedAppointment[1] = appointment.getSurname();
                for (int i = 0; i < splittedAppointment.length; ++i)
                    preparedAppointment.append(splittedAppointment[i]).append((i == splittedAppointment.length - 1)? "" : " ");

            }

            appointmentLabel.setText(preparedAppointment.toString());

            dismiss();
        }
        else
            Toast.makeText(getContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
    }
}
