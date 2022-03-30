package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.constants.PinnedDataStorage;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.CodeGenerator;
import com.example.studassistant.managers.GetRequestManager;
import com.example.studassistant.managers.PostRequestManager;
import com.example.studassistant.managers.PutRequestManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class PostConfirmationFragment extends DialogFragment implements View.OnClickListener, TextWatcher {
    private Appointment appointment;
    private PostRequestManager postRequestManager;
    private PutRequestManager putRequestManager;
    private Context context;
    private TextView codeField;
    private TextView spaceCheckField;
    private boolean hasEmptySpace;
    private AppointmentFragment fragment;
    private ProgressBar postProgressButton;

    public PostConfirmationFragment(Appointment appointment, Context context, AppointmentFragment fragment){
        this.appointment = appointment;
        this.context = context;
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_confirmation, container, false);

        postProgressButton = view.findViewById(R.id.postProgressBar);
        postProgressButton.setVisibility(View.INVISIBLE);

        codeField = view.findViewById(R.id.codeField);

        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFilesDir() + "/code.txt"))){
            codeField.setText(reader.lines().collect(Collectors.joining()));
        }
        catch(IOException exception){
            codeField.setText(CodeGenerator.generateCode(10));
        }

        appointment.setUserCode(codeField.getText().toString());

        spaceCheckField = view.findViewById(R.id.spaceCheckField);
        spaceCheckField.addTextChangedListener(this);

        view.findViewById(R.id.post_yes_button).setOnClickListener(this);
        view.findViewById(R.id.post_no_button).setOnClickListener(this);

        postRequestManager = new PostRequestManager(context, ArrayType.APPOINTMENTS, appointment);
        putRequestManager = new PutRequestManager(context, ArrayType.DATES, PinnedDataStorage.pinnedSingleData);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.post_yes_button){
            GetRequestManager getRequestManager = new GetRequestManager(context, ArrayType.DATES, null, null,
                    appointment.getConsultId() + "", ExtraType.ID);
            getRequestManager.setMonitorValue(spaceCheckField);
            if (getRequestManager.checkConnection()){
                postProgressButton.setVisibility(View.VISIBLE);
                getRequestManager.createRequest();
            }
            else
                Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
        }
        else if (view.getId() == R.id.post_no_button)
            onDestroy();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        hasEmptySpace = spaceCheckField.getText().toString().equalsIgnoreCase("  ");

        if (postRequestManager.checkConnection()){
            if (hasEmptySpace){
                PinnedDataStorage.pinnedSingleData.changeOrderedSpace(1);
                putRequestManager.setDataToPost(PinnedDataStorage.pinnedSingleData);
                putRequestManager.createRequest();
                postRequestManager.createRequest();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(context.getFilesDir() + "/code.txt", false))){
                    writer.write(codeField.getText().toString());
                }
                catch(IOException exception){
                }

                fragment.clearDatetime();
                PinnedDataStorage.pinnedSingleData = null;

                Toast.makeText(getContext(), R.string.post_success_text, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(context, R.string.no_space_error, Toast.LENGTH_LONG).show();

            onDestroy();
        }
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
