package com.example.studassistant.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.GetRequestManager;

public class DateTimeFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnLayoutChangeListener, TextWatcher {

    private TextView[] appointmentFields;
    private Appointment appointment;
    private Spinner datetimeList;
    private TextView spaceAmountField;
    private TextView spaceAmountLabel;
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
        datetimeList.addOnLayoutChangeListener(this);

        spaceAmountField = view.findViewById(R.id.spaceAmountField);
        spaceAmountField.addTextChangedListener(this);
        spaceAmountLabel = view.findViewById(R.id.spaceAmountLabel);

        spaceAmountField.setVisibility(View.INVISIBLE);
        spaceAmountLabel.setVisibility(View.INVISIBLE);

        getRequestManager = new GetRequestManager(context, ArrayType.DATES, datetimeList, null, appointment.getTutorId() + "", ExtraType.TUTOR_ID);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.default_spinner_layout, new String[]{"????????????????..."});
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
            if (datetimeList.getSelectedItem() != null){
                if (datetimeList.getSelectedItem().toString().equalsIgnoreCase("????????????????...") ||
                        datetimeList.getSelectedItem().toString().equalsIgnoreCase("???????????????????? ???? ??????????????!"))
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
            if (datetimeList.getSelectedItem().toString().equalsIgnoreCase("????????????????...") ||
                    datetimeList.getSelectedItem().toString().equalsIgnoreCase("???????????????????? ???? ??????????????!"))
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
        if (spaceAmountField.getVisibility() == View.VISIBLE){
            DatetimeListAdapter adapter = (DatetimeListAdapter) datetimeList.getAdapter();
            spaceAmountField.setText(adapter.getItemByIndex(datetimeList.getSelectedItemPosition()).getSpaceStat());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        try{
            DatetimeListAdapter adapter = (DatetimeListAdapter) datetimeList.getAdapter();
            spaceAmountField.setText(adapter.getItemByIndex(0).getSpaceStat());

            spaceAmountField.setVisibility(View.VISIBLE);
            spaceAmountLabel.setVisibility(View.VISIBLE);
        }
        catch (ClassCastException exception){
            spaceAmountField.setVisibility(View.INVISIBLE);
            spaceAmountLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        DatetimeListAdapter adapter = (DatetimeListAdapter) datetimeList.getAdapter();

        double spaceStat = adapter.getItemByIndex(datetimeList.getSelectedItemPosition()).getOrderedSpace() /
                (1.0 * adapter.getItemByIndex(datetimeList.getSelectedItemPosition()).getMaxSpace());
        if (spaceStat < 0.5)
            spaceAmountField.setBackgroundColor(Color.parseColor("#32CD32"));
        else if (spaceStat < 0.75)
            spaceAmountField.setBackgroundColor(Color.parseColor("#FF8C00"));
        else
            spaceAmountField.setBackgroundColor(Color.parseColor("#FF0000"));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
