package com.example.studassistant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.studassistant.R;

public class TimeFragment extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.time_window, container, false);

        Button confirmTimeButton = view.findViewById(R.id.confirmTimeButton);
        confirmTimeButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
