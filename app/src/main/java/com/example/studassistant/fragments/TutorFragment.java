package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.NetworkManager;

public class TutorFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView appointmentLabel;
    private Appointment appointment;
    private Spinner tutorsList;
    private NetworkManager networkManager;
    private Context context;

    public TutorFragment(Appointment appointment, TextView appointmentLabel, Context context){
        this.appointment = appointment;
        this.appointmentLabel = appointmentLabel;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tutor, container, false);

        tutorsList = view.findViewById(R.id.tutorsList);

        networkManager = new NetworkManager(context, ArrayType.TUTORS, tutorsList, getDataToRemember());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, new String[]{"Загрузка..."});
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter.notifyDataSetChanged();
        tutorsList.setAdapter(adapter);

        tutorsList.setOnItemSelectedListener(this);

        view.findViewById(R.id.tutorOkButton).setOnClickListener(this);

        if (!networkManager.checkConnection()){
            Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            dismiss();
        }
        else{
            networkManager.getData();
            restoreData();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (networkManager.checkConnection()){
            appointment.setTutor(tutorsList.getSelectedItem().toString());

            if (!appointment.getTutor().equals("")){
                String[] currentAppointment = appointmentLabel.getText().toString().split("[\n]+");
                StringBuilder preparedAppointment = new StringBuilder();
                appointment.setDatetime(null);

                for (int i = 0; i < 3; ++i)
                    preparedAppointment.append(currentAppointment[i]).append("\n");

                preparedAppointment.append(appointment.getTutor());

                appointmentLabel.setText(preparedAppointment.toString());

                dismiss();
            }
            else
                Toast.makeText(getContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void restoreData(){
        String[] currentAppointment = appointmentLabel.getText().toString().split("[\n]+");

        if (currentAppointment.length >= 4){
            if (!networkManager.checkConnection())
                Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            else
                networkManager.getDataToRestore(currentAppointment[3]);
        }
    }

    private String getDataToRemember() {
        String[] currentAppointment = appointmentLabel.getText().toString().split("[\n]+");

        return currentAppointment[2];
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
