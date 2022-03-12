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
import com.example.studassistant.managers.GetRequestManager;

public class DateTimeFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView appointmentLabel;
    private Appointment appointment;
    private Spinner datetimeList;
    private GetRequestManager getRequestManager;
    private Context context;

    public DateTimeFragment(Appointment appointment, TextView appointmentLabel, Context context){
        this.appointment = appointment;
        this.appointmentLabel = appointmentLabel;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_datetime, container, false);

        datetimeList = view.findViewById(R.id.datetimeList);

        getRequestManager = new GetRequestManager(context, ArrayType.DATETIME, datetimeList, getDataToRemember());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, new String[]{"Загрузка..."});
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter.notifyDataSetChanged();
        datetimeList.setAdapter(adapter);

        datetimeList.setOnItemSelectedListener(this);

        view.findViewById(R.id.dateTimeOkButton).setOnClickListener(this);

        if (!getRequestManager.checkConnection()){
            Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            dismiss();
        }
        else{
            getRequestManager.createRequest();
            restoreData();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (getRequestManager.checkConnection()){
            if (datetimeList.getSelectedItem().toString().equalsIgnoreCase("Загрузка..."))
                appointment.setDatetime(null);
            else
                appointment.setDatetime(datetimeList.getSelectedItem().toString());

            if (appointment.getDatetime() != null){
                String[] currentAppointment = appointmentLabel.getText().toString().split("[\n]+");
                StringBuilder preparedAppointment = new StringBuilder();

                for (int i = 0; i < 4; ++i)
                    preparedAppointment.append(currentAppointment[i]).append("\n");

                preparedAppointment.append(appointment.getDatetime());

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
        if (currentAppointment.length >= 5){
            if (!getRequestManager.checkConnection())
                Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            else
                getRequestManager.getDataToRestore(currentAppointment[4]);
        }
    }

    private String getDataToRemember() {
        String[] currentAppointment = appointmentLabel.getText().toString().split("[\n]+");

        return currentAppointment[3];
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
