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
import com.example.studassistant.adapters.DatetimeListAdapter;
import com.example.studassistant.adapters.TutorsListAdapter;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.ConsultDatetime;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.GetRequestManager;

public class DateTimeFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView[] appointmentFields;
    private Appointment appointment;
    private Spinner datetimeList;
    private GetRequestManager getRequestManager;
    private Context context;

    public DateTimeFragment(Appointment appointment, TextView[] appointmentFields, Context context){
        this.appointment = appointment;
        this.appointmentFields = appointmentFields;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_datetime, container, false);

        datetimeList = view.findViewById(R.id.datetimeList);

        getRequestManager = new GetRequestManager(context, ArrayType.DATES, datetimeList, null, appointment.getTutorId() + "", ExtraType.TUTOR_ID);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, new String[]{"Загрузка..."});
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapter.notifyDataSetChanged();

        datetimeList.setAdapter(adapter);
        datetimeList.setOnItemSelectedListener(this);

        view.findViewById(R.id.dateTimeOkButton).setOnClickListener(this);

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
            if (datetimeList.getSelectedItem().toString().equalsIgnoreCase("Загрузка...") ||
                    datetimeList.getSelectedItem().toString().equalsIgnoreCase("Информация не найдена!"))
                appointment.setDatetime(null);
            else{
                appointment.setDatetime(datetimeList.getSelectedItem().toString());
                DatetimeListAdapter adapter = (DatetimeListAdapter) datetimeList.getAdapter();
                appointment.setConsultId(adapter.getItemByIndex(datetimeList.getSelectedItemPosition()).getId());
            }

            if (appointment.getDatetime() != null){
                appointmentFields[4].setText(appointment.getDatetime());

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
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
