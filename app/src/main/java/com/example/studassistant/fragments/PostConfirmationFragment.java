package com.example.studassistant.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.studassistant.adapters.DatetimeListAdapter;
import com.example.studassistant.constants.UserDataValues;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.ConsultDatetime;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.CodeGenerator;
import com.example.studassistant.managers.GetRequestManager;
import com.example.studassistant.managers.PostRequestManager;
import com.example.studassistant.managers.PutRequestManager;

public class PostConfirmationFragment extends DialogFragment implements View.OnClickListener, TextWatcher {
    private Appointment appointment;
    private PostRequestManager postRequestManager;
    private PutRequestManager putRequestManager;
    private Context context;
    private TextView codeField;
    private TextView spaceCheckField;
    private AppointmentFragment fragment;
    private ProgressBar postProgressBar;
    private Spinner dataTransfer;

    public PostConfirmationFragment(Appointment appointment, Context context, AppointmentFragment fragment){
        this.appointment = appointment;
        this.context = context;
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_confirmation, container, false);

        dataTransfer = new Spinner(context);

        postProgressBar = view.findViewById(R.id.postProgressBar);
        postProgressBar.setVisibility(View.INVISIBLE);

        codeField = view.findViewById(R.id.codeField);

        SharedPreferences preferences = context.getSharedPreferences(UserDataValues.PREFERENCES_NAME, Context.MODE_PRIVATE);
        codeField.setText(preferences.getString(UserDataValues.USER_CODE, CodeGenerator.generateCode(10)));

        appointment.setUserCode(codeField.getText().toString());

        spaceCheckField = view.findViewById(R.id.spaceCheckField);
        spaceCheckField.addTextChangedListener(this);

        view.findViewById(R.id.post_yes_button).setOnClickListener(this);
        view.findViewById(R.id.post_no_button).setOnClickListener(this);

        postRequestManager = new PostRequestManager(context, ArrayType.APPOINTMENTS, appointment);
        putRequestManager = new PutRequestManager(context, ArrayType.DATES, null);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.post_yes_button){
            GetRequestManager getRequestManager = new GetRequestManager(context, ArrayType.DATES, dataTransfer, null,
                    appointment.getConsultId() + "", ExtraType.ID);
            getRequestManager.setMonitorValue(spaceCheckField);
            if (getRequestManager.checkConnection()){
                postProgressBar.setVisibility(View.VISIBLE);
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
        boolean hasEmptySpace = spaceCheckField.getText().toString().equalsIgnoreCase("  ");

        if (postRequestManager.checkConnection()){
            if (hasEmptySpace){
                DatetimeListAdapter adapter = (DatetimeListAdapter)dataTransfer.getAdapter();
                ConsultDatetime dataToPost = adapter.getItemByIndex(0);
                dataToPost.changeOrderedSpace(1);

                putRequestManager.setDataToPost(dataToPost);
                putRequestManager.createRequest();
                postRequestManager.createRequest();

                SharedPreferences preferences = context.getSharedPreferences(UserDataValues.PREFERENCES_NAME, Context.MODE_PRIVATE);
                if (!preferences.contains(UserDataValues.USER_CODE))
                    preferences.edit().putString(UserDataValues.USER_CODE, codeField.getText().toString()).apply();

                fragment.clearDatetime();

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
