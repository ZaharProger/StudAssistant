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
import com.example.studassistant.managers.GetRequestManager;


public class GroupFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {
    private Spinner groupList;
    private Appointment appointment;
    private TextView[] appointmentFields;
    private Context context;
    private GetRequestManager getRequestManager;
    private EditText groupFilter;

    public GroupFragment(Appointment appointment, TextView[] appointmentFields, Context context){
        this.appointment = appointment;
        this.appointmentFields = appointmentFields;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_group, container, false);

        groupFilter = view.findViewById(R.id.groupFilter);
        groupFilter.addTextChangedListener(this);

        groupList = view.findViewById(R.id.groupList);
        groupList.setOnItemSelectedListener(this);

        getRequestManager = new GetRequestManager(context, ArrayType.GROUPS, groupList, null, null);
        if (appointment.getGroup() != null)
            groupFilter.setText(appointment.getGroup());
        else
            groupFilter.setText("");

        if (!getRequestManager.checkConnection()){
            Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
            onDestroy();
        }
        else
            getRequestManager.createRequest();

        view.findViewById(R.id.personalOkButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (getRequestManager.checkConnection()){
            if (groupList.getSelectedItem() != null){
                if (groupList.getSelectedItem().toString().equalsIgnoreCase("Информация не найдена!"))
                    appointment.setGroup(null);
                else
                    appointment.setGroup(groupList.getSelectedItem().toString());

                if (!(appointment.getGroup() == null)){
                    appointmentFields[2].setText(appointment.getGroup());

                    onDestroy();
                }
                else
                    Toast.makeText(getContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
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
            String userData = groupFilter.getText().toString();
            if (userData.equalsIgnoreCase(""))
                userData = "~";
            getRequestManager.setDataToRemember(userData);
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
