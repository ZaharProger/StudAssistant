package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.GetRequestManager;

public class TutorFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {
    private TextView appointmentLabel;
    private Appointment appointment;
    private Spinner tutorsList;
    private GetRequestManager getRequestManager;
    private Context context;
    private EditText tutorFilter;

    public TutorFragment(Appointment appointment, TextView appointmentLabel, Context context){
        this.appointment = appointment;
        this.appointmentLabel = appointmentLabel;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tutor, container, false);

        tutorFilter = view.findViewById(R.id.tutorFilter);
        tutorFilter.addTextChangedListener(this);

        tutorsList = view.findViewById(R.id.tutorsList);
        tutorsList.setOnItemSelectedListener(this);

        getRequestManager = new GetRequestManager(context, ArrayType.TUTORS, tutorsList, null, null, ExtraType.SURNAME);
        getRequestManager.setRequestExtra("");

        view.findViewById(R.id.tutorOkButton).setOnClickListener(this);

        if (!getRequestManager.checkConnection()){
            Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            onDestroy();
        }
        else
            getRequestManager.createRequest();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (getRequestManager.checkConnection()){
            if (tutorsList.getSelectedItem().toString().equalsIgnoreCase("Информация не найдена!"))
                appointment.setTutor(null);
            else
                appointment.setTutor(tutorsList.getSelectedItem().toString());

            if (appointment.getTutor() != null){
                String[] currentAppointment = appointmentLabel.getText().toString().split("[\n]+");
                StringBuilder preparedAppointment = new StringBuilder();
                appointment.setDatetime(null);

                for (int i = 0; i < 3; ++i)
                    preparedAppointment.append(currentAppointment[i].trim()).append("\n");

                preparedAppointment.append(appointment.getTutor().trim());

                appointmentLabel.setText(preparedAppointment.toString());

                onDestroy();
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (getRequestManager.checkConnection()){
            getRequestManager.setRequestExtra(tutorFilter.getText().toString());
            getRequestManager.createRequest();
        }
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
