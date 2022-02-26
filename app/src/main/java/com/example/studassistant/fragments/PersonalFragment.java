package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.studassistant.managers.NetworkManager;


public class PersonalFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private EditText nameField;
    private EditText surnameField;
    private Spinner groupList;
    private Appointment appointment;
    private TextView appointmentLabel;
    private Context context;
    private NetworkManager networkManager;

    public PersonalFragment(Appointment appointment, TextView appointmentLabel, Context context){
        this.appointment = appointment;
        this.appointmentLabel = appointmentLabel;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal, container, false);

        nameField = view.findViewById(R.id.nameField);
        surnameField = view.findViewById(R.id.surnameField);
        groupList = view.findViewById(R.id.groupList);

        networkManager = new NetworkManager(context, ArrayType.GROUPS, groupList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, new String[]{"Загрузка..."});
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter.notifyDataSetChanged();
        groupList.setAdapter(adapter);

        groupList.setOnItemSelectedListener(this);

        if (!networkManager.checkConnection()){
            Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            dismiss();
        }
        else{
            networkManager.getData();
            restoreData();
        }

        view.findViewById(R.id.personalOkButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (networkManager.checkConnection()){
            appointment.setName(nameField.getText().toString().trim());
            appointment.setSurname(surnameField.getText().toString().trim());
            appointment.setGroup(groupList.getSelectedItem().toString());

            if (!(appointment.getName().equals("") || appointment.getSurname().equals("") || appointment.getGroup().equals(""))){
                String currentAppointment = appointmentLabel.getText().toString();
                StringBuilder preparedAppointment = new StringBuilder();

                if (currentAppointment.length() == 0)
                    preparedAppointment.append(appointment.getName()).append("\n").append(appointment.getSurname())
                            .append("\n").append(appointment.getGroup());
                else{
                    String[] splittedAppointment = currentAppointment.trim().split("[\n]+");
                    splittedAppointment[0] = appointment.getName();
                    splittedAppointment[1] = appointment.getSurname();
                    splittedAppointment[2] = appointment.getGroup();
                    appointment.setTutor(null);
                    appointment.setDatetime(null);

                    for (int i = 0; i < 3; ++i)
                        preparedAppointment.append(splittedAppointment[i]).append((i == 2)? "" : "\n");

                }

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
        if (currentAppointment.length >= 3){
            nameField.setText(currentAppointment[0]);
            surnameField.setText(currentAppointment[1]);
            if (!networkManager.checkConnection())
                Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            else
                networkManager.getDataToRestore(currentAppointment[2]);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
