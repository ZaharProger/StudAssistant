package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.CodeGenerator;
import com.example.studassistant.managers.PostRequestManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class ConfirmationFragment extends DialogFragment implements View.OnClickListener{
    private Appointment appointment;
    private PostRequestManager postRequestManager;
    private Context context;
    private TextView codeField;

    public ConfirmationFragment(Appointment appointment, Context context){
        this.appointment = appointment;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_confirmation, container, false);

        codeField = view.findViewById(R.id.codeField);

        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFilesDir() + "/code.txt"))){
            codeField.setText(reader.lines().collect(Collectors.joining()));
        }
        catch(IOException exception){
            codeField.setText(CodeGenerator.generateCode(10));
        }

        appointment.setUserCode(codeField.getText().toString());

        view.findViewById(R.id.yes_button).setOnClickListener(this);
        view.findViewById(R.id.no_button).setOnClickListener(this);

        postRequestManager = new PostRequestManager(context, ArrayType.APPOINTMENTS, appointment);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.yes_button){
            if (postRequestManager.checkConnection()){
                postRequestManager.createRequest();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(context.getFilesDir() + "/code.txt", false))){
                    writer.write(codeField.getText().toString());
                }
                catch(IOException exception){
                }

                Toast.makeText(getContext(), R.string.post_success_text, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
        }

        onDestroy();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
