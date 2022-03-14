package com.example.studassistant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.GetRequestManager;

public class MyAppointmentFragment extends Fragment{
    private RecyclerView appointmentsList;
    private GetRequestManager getRequestManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actitivty_my_appointment, container, false);

        appointmentsList = view.findViewById(R.id.appointmentsList);
        appointmentsList.setVisibility(View.INVISIBLE);
        appointmentsList.setHasFixedSize(true);
        appointmentsList.setLayoutManager(new LinearLayoutManager(getContext()));

        getRequestManager = new GetRequestManager(getContext(), ArrayType.APPOINTMENTS, null, appointmentsList);

        getRequestManager.createRequest();

        return view;
    }
}
