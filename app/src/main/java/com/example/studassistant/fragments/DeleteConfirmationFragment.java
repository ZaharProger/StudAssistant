package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.adapters.AppointmentsListAdapter;
import com.example.studassistant.adapters.DatetimeListAdapter;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.AppointmentsListElement;
import com.example.studassistant.entities.ConsultDatetime;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.DeleteRequestManager;
import com.example.studassistant.managers.GetRequestManager;
import com.example.studassistant.managers.PutRequestManager;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteConfirmationFragment extends DialogFragment implements View.OnClickListener, TextWatcher {
    private AppointmentsListElement itemToRemove;
    private MyAppointmentFragment fragment;
    private AppointmentsListAdapter adapter;
    private DeleteRequestManager deleteRequestManager;
    private PutRequestManager putRequestManager;
    private Context context;
    private  boolean bySwipe;
    private int indexToRemove;
    private Spinner dataTransfer;
    private ProgressBar deleteProgressBar;
    TextView dataUpdateField;

    public DeleteConfirmationFragment(MyAppointmentFragment fragment, AppointmentsListAdapter adapter, Context context, boolean bySwipe, int indexToRemove, AppointmentsListElement itemToRemove){
        this.fragment = fragment;
        this.adapter = adapter;
        this.context = context;
        this.bySwipe = bySwipe;
        this.indexToRemove = indexToRemove;
        this.itemToRemove = itemToRemove;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delete_confirmation, container, false);

        deleteProgressBar = view.findViewById(R.id.deleteProgressBar);
        deleteProgressBar.setVisibility(View.INVISIBLE);

        dataTransfer = new Spinner(context);
        dataUpdateField = view.findViewById(R.id.dataUpdateField);
        dataUpdateField.addTextChangedListener(this);

        view.findViewById(R.id.delete_yes_button).setOnClickListener(this);
        view.findViewById(R.id.delete_no_button).setOnClickListener(this);

        deleteRequestManager = new DeleteRequestManager(context, ArrayType.APPOINTMENTS, 0);
        putRequestManager = new PutRequestManager(context, ArrayType.DATES, null);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete_yes_button){
            GetRequestManager getRequestManager = new GetRequestManager(context, ArrayType.DATES, dataTransfer, null, null, ExtraType.ID);
            getRequestManager.setMonitorValue(dataUpdateField);
            if (bySwipe)
                getRequestManager.setRequestExtra(itemToRemove.getAppointment().getConsultId() + "");

            getRequestManager.createRequest();
        }
        else{
            if (bySwipe)
                adapter.addItem(indexToRemove, itemToRemove);

            fragment.setVisibilities();
            onDestroy();
        }
    }

    private HashMap<Long, Integer> sortAppointments(ArrayList<Appointment> appointmentsToRemove) {
        HashMap<Long, Integer> sortedAppointments = new HashMap<>();

        for (Appointment appointment : appointmentsToRemove){
            if (!sortedAppointments.containsKey(appointment.getConsultId()))
                sortedAppointments.put(appointment.getConsultId(), 1);
            else
                sortedAppointments.put(appointment.getConsultId(), sortedAppointments.get(appointment.getConsultId()) + 1);
        }

        return sortedAppointments;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean isDataUpdated = dataUpdateField.getText().toString().equalsIgnoreCase("  ");

        if (deleteRequestManager.checkConnection()){
            if (isDataUpdated){
                deleteProgressBar.setVisibility(View.VISIBLE);

                DatetimeListAdapter transferedData = (DatetimeListAdapter) dataTransfer.getAdapter();
                if (!bySwipe){
                    ArrayList<Appointment> appointmentsToRemove = adapter.getAppointmentsToRemove();
                    for (int j = 0; j < appointmentsToRemove.size(); ++j){
                        adapter.removeCheckedItem(appointmentsToRemove.get(j).getId());

                        deleteRequestManager.setIdToDelete(appointmentsToRemove.get(j).getId());
                        deleteRequestManager.createRequest();
                    }

                    HashMap<Long, Integer> sortedAppointmentsToRemove = sortAppointments(appointmentsToRemove);
                    for (Long key : sortedAppointmentsToRemove.keySet()){
                        ConsultDatetime dataToPost = transferedData.getItemById(key);
                        dataToPost.changeOrderedSpace(-sortedAppointmentsToRemove.get(key));

                        putRequestManager.setDataToPost(dataToPost);
                        putRequestManager.createRequest();
                    }
                    adapter.updateCheckStatus();
                }
                else{
                    transferedData.getItemById(itemToRemove.getAppointment().getConsultId()).changeOrderedSpace(-1);
                    putRequestManager.setDataToPost(transferedData.getItemById(itemToRemove.getAppointment().getConsultId()));
                    putRequestManager.createRequest();

                    deleteRequestManager.setIdToDelete(itemToRemove.getAppointment().getId());
                    deleteRequestManager.createRequest();
                }

                Toast.makeText(getContext(), R.string.remove_success_text, Toast.LENGTH_LONG).show();
            }

            onDestroy();
        }
        else{
            if (bySwipe)
                adapter.addItem(indexToRemove, itemToRemove);

            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
