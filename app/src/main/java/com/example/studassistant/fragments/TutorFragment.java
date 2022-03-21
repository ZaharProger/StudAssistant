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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.LikedListElement;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.DataBaseManager;
import com.example.studassistant.managers.GetRequestManager;

public class TutorFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {
    private TextView[] appointmentFields;
    private Appointment appointment;
    private Spinner tutorsList;
    private DataBaseManager dataBaseManager;
    private GetRequestManager getRequestManager;
    private Context context;
    private EditText tutorFilter;
    private ImageButton addToLikedButton;

    public TutorFragment(Appointment appointment, TextView[] appointmentFields, Context context){
        this.appointment = appointment;
        this.appointmentFields = appointmentFields;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tutor, container, false);

        tutorFilter = view.findViewById(R.id.tutorFilter);
        tutorFilter.addTextChangedListener(this);

        tutorsList = view.findViewById(R.id.tutorsList);
        tutorsList.setOnItemSelectedListener(this);

        addToLikedButton = view.findViewById(R.id.addToLikedButton);
        addToLikedButton.setOnClickListener(this);

        dataBaseManager = DataBaseManager.create(context);

        getRequestManager = new GetRequestManager(context, ArrayType.TUTORS, tutorsList, null, null);
        if (appointment.getTutor() != null)
            tutorFilter.setText(appointment.getTutor());
        else
            tutorFilter.setText("");

        view.findViewById(R.id.tutorOkButton).setOnClickListener(this);

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
        if (view.getId() == R.id.tutorOkButton){
            if (getRequestManager.checkConnection()){
                if (tutorsList.getSelectedItem().toString().equalsIgnoreCase("Информация не найдена!"))
                    appointment.setTutor(null);
                else
                    appointment.setTutor(tutorsList.getSelectedItem().toString());

                if (appointment.getTutor() != null){
                    appointmentFields[3].setText(appointment.getTutor());
                    appointment.setDatetime(null);
                    appointmentFields[4].setText("");
                    onDestroy();
                }
                else
                    Toast.makeText(getContext(), R.string.ok_error_text, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
        }
        else{
            if (!tutorsList.getSelectedItem().toString().equalsIgnoreCase("Информация не найдена!")){
                LikedListElement sameTutor = dataBaseManager.getData(tutorsList.getSelectedItem().toString());

                if (sameTutor != null){
                    dataBaseManager.remove(sameTutor.getId());
                    addToLikedButton.setImageResource(R.drawable.ic_liked_roundless);

                    Toast.makeText(context, R.string.exclude_from_liked_text, Toast.LENGTH_LONG).show();
                }
                else {
                    dataBaseManager.add(tutorsList.getSelectedItem().toString());
                    addToLikedButton.setImageResource(R.drawable.ic_liked_coloured);

                    Toast.makeText(context, R.string.add_to_liked_text, Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(context, R.string.choose_tutor_text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        LikedListElement sameTutor = dataBaseManager.getData(tutorsList.getSelectedItem().toString());
        if (sameTutor == null)
            addToLikedButton.setImageResource(R.drawable.ic_liked_roundless);
        else
            addToLikedButton.setImageResource(R.drawable.ic_liked_coloured);
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
            String userData = tutorFilter.getText().toString();
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
