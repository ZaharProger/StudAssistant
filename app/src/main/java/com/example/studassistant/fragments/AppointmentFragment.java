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
import com.example.studassistant.entities.ConsultDatetime;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.CodeGenerator;
import com.example.studassistant.managers.RequestManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AppointmentFragment extends Fragment implements View.OnClickListener {
    private Appointment appointment;
    private TextView[] appointmentFields;
    private RequestManager requestManager;
    private String dataToRestore;

    public AppointmentFragment(String dataToRestore){
        this.dataToRestore = dataToRestore;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment, container, false);

        appointmentFields = new TextView[5];
        appointmentFields[0] = view.findViewById(R.id.nameResultField);
        appointmentFields[1] = view.findViewById(R.id.surnameResultField);
        appointmentFields[2] = view.findViewById(R.id.groupResultField);
        appointmentFields[3] = view.findViewById(R.id.tutorResultField);
        appointmentFields[4] = view.findViewById(R.id.datetimeResultField);

        view.findViewById(R.id.personalButton).setOnClickListener(this);
        view.findViewById(R.id.tutorButton).setOnClickListener(this);
        view.findViewById(R.id.dateTimeButton).setOnClickListener(this);
        view.findViewById(R.id.confirmButton).setOnClickListener(this);

        appointment = new Appointment();
        if (dataToRestore != null){
            appointmentFields[3].setText(dataToRestore);
            appointment.setTutor(dataToRestore);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(getContext().getFilesDir() + "/personal.txt"))){
            ArrayList<String> personalData = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null)
                personalData.add(line);

            appointment.setName(personalData.get(0));
            appointment.setSurname(personalData.get(1));

            appointmentFields[0].setText(appointment.getName());
            appointmentFields[1].setText(appointment.getSurname());
        }
        catch(IOException exception){

        }

        requestManager = new RequestManager(getContext(), ArrayType.APPOINTMENTS);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.personalButton){
            new GroupFragment(appointment, appointmentFields, getContext()).show(getParentFragmentManager(), "Personal");
        }
        else if (view.getId() == R.id.tutorButton){
            new TutorFragment(appointment, appointmentFields, getContext()).show(getParentFragmentManager(), "Tutor");
        }
        else if (view.getId() == R.id.dateTimeButton){
            if (appointment.getTutor() == null)
                Toast.makeText(getContext(), R.string.section_error_text, Toast.LENGTH_LONG).show();
            else
                new DateTimeFragment(appointment, appointmentFields, getContext()).show(getParentFragmentManager(), "DateTime");
        }
        else if (view.getId() == R.id.confirmButton){
            if (appointment.getName() == null || appointment.getSurname() == null || appointment.getGroup() == null ||
                    appointment.getTutor() == null || appointment.getDatetime() == null)
                Toast.makeText(getContext(), R.string.confirm_error_text, Toast.LENGTH_LONG).show();
            else{
                appointment.setId(Math.abs(CodeGenerator.NUM_GENERATOR.nextLong()));

                if (requestManager.checkConnection())
                    new PostConfirmationFragment(appointment, getContext(), AppointmentFragment.this).show(getParentFragmentManager(), "PostConfirmation");
                else
                    Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onResume(){
        super.onResume();
        getActivity().setTitle("Оформить запись");
    }

    public void clearData(){
        appointment.setTutor(null);
        appointment.setDatetime(null);

        appointmentFields[3].setText("");
        appointmentFields[4].setText("");
    }
}
