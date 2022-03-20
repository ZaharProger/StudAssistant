package com.example.studassistant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.CodeGenerator;
import com.example.studassistant.managers.RequestManager;

public class AppointmentFragment extends Fragment implements View.OnClickListener {
    private Appointment appointment;
    private TextView appointmentLabel;
    private RequestManager requestManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment, container, false);

        appointmentLabel = view.findViewById(R.id.appointmentLabel);

        view.findViewById(R.id.personalButton).setOnClickListener(this);
        view.findViewById(R.id.tutorButton).setOnClickListener(this);
        view.findViewById(R.id.dateTimeButton).setOnClickListener(this);
        view.findViewById(R.id.confirmButton).setOnClickListener(this);

        appointment = new Appointment();
        requestManager = new RequestManager(getContext(), ArrayType.APPOINTMENTS);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.personalButton){
            new PersonalFragment(appointment, appointmentLabel, getContext()).show(getParentFragmentManager(), "Personal");
        }
        else if (view.getId() == R.id.tutorButton){
            if (appointment.getName() == null || appointment.getSurname() == null || appointment.getGroup() == null)
                Toast.makeText(getContext(), R.string.section_error_text, Toast.LENGTH_LONG).show();
            else
                new TutorFragment(appointment, appointmentLabel, getContext()).show(getParentFragmentManager(), "Tutor");
        }
        else if (view.getId() == R.id.dateTimeButton){
            if (appointment.getName() == null || appointment.getSurname() == null ||
                    appointment.getGroup() == null || appointment.getTutor() == null)
                Toast.makeText(getContext(), R.string.section_error_text, Toast.LENGTH_LONG).show();
            else
                new DateTimeFragment(appointment, appointmentLabel, getContext()).show(getParentFragmentManager(), "DateTime");
        }
        else if (view.getId() == R.id.confirmButton){
            if (appointment.getName() == null || appointment.getSurname() == null || appointment.getGroup() == null ||
                    appointment.getTutor() == null || appointment.getDatetime() == null)
                Toast.makeText(getContext(), R.string.confirm_error_text, Toast.LENGTH_LONG).show();
            else{
                appointment.setId(Math.abs(CodeGenerator.NUM_GENERATOR.nextLong()));

                if (requestManager.checkConnection())
                    new PostConfirmationFragment(appointment,  getContext()).show(getParentFragmentManager(), "PostConfirmation");
                else
                    Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
            }
        }
    }
}
