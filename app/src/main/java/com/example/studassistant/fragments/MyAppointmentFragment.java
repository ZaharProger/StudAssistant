package com.example.studassistant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.adapters.AppointmentsListAdapter;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.DeleteRequestManager;
import com.example.studassistant.managers.GetRequestManager;

import java.util.ArrayList;

public class MyAppointmentFragment extends Fragment implements View.OnClickListener{
    private RecyclerView appointmentsList;
    private GetRequestManager getRequestManager;
    private DeleteRequestManager deleteRequestManager;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.actitivty_my_appointment, container, false);

        view.findViewById(R.id.removeButton).setOnClickListener(this);

        appointmentsList = view.findViewById(R.id.appointmentsList);
        appointmentsList.setVisibility(View.INVISIBLE);
        appointmentsList.setHasFixedSize(true);
        appointmentsList.setLayoutManager(new LinearLayoutManager(getContext()));

        getRequestManager = new GetRequestManager(getContext(), ArrayType.APPOINTMENTS, null, appointmentsList);
        deleteRequestManager = new DeleteRequestManager(getContext(), ArrayType.APPOINTMENTS, 0);

        if (getRequestManager.checkConnection())
            getRequestManager.createRequest();
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (deleteRequestManager.checkConnection()){
            AppointmentsListAdapter appointmentsListAdapter = (AppointmentsListAdapter) appointmentsList.getAdapter();

            ArrayList<Long> appointmentsToRemove = appointmentsListAdapter.getAppointmentsToRemove();
            if (appointmentsToRemove != null)
                if (!appointmentsToRemove.isEmpty()){
                    for (long appointmentId : appointmentsToRemove){
                        appointmentsListAdapter.removeItemById(appointmentId);

                        deleteRequestManager.setIdToDelete(appointmentId);
                        deleteRequestManager.createRequest();
                    }

                    Toast.makeText(getContext(), R.string.remove_success_text, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getContext(), R.string.remove_failure_text, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
    }
}
