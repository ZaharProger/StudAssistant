package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.GetRequestManager;

public class ViewFragment extends DialogFragment implements View.OnLayoutChangeListener, View.OnClickListener {
    private int tutorId;
    private RecyclerView tutorDates;
    private ProgressBar viewProgressBar;
    private GetRequestManager getRequestManager;

    public ViewFragment(int tutorId){
        this.tutorId = tutorId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view, container, false);

        view.findViewById(R.id.updateButton).setOnClickListener(this);

        viewProgressBar = view.findViewById(R.id.viewProgressBar);
        viewProgressBar.setVisibility(View.VISIBLE);

        tutorDates = view.findViewById(R.id.tutorDates);
        tutorDates.addOnLayoutChangeListener(this);
        tutorDates.setVisibility(View.INVISIBLE);
        tutorDates.setHasFixedSize(true);
        tutorDates.setLayoutManager(new LinearLayoutManager(getContext()));

        getRequestManager = new GetRequestManager(getContext(), ArrayType.DATES, null, tutorDates, tutorId + "", ExtraType.TUTOR_ID);
        if (!getRequestManager.checkConnection()){
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
            onDestroy();
        }
        else
            getRequestManager.createRequest();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (!getRequestManager.checkConnection()){
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
            onDestroy();
        }
        else{
            tutorDates.setVisibility(View.INVISIBLE);
            viewProgressBar.setVisibility(View.VISIBLE);
            getRequestManager.createRequest();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        viewProgressBar.setVisibility(View.INVISIBLE);
        tutorDates.setVisibility(View.VISIBLE);
    }
}
