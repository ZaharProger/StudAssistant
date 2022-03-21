package com.example.studassistant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studassistant.R;
import com.example.studassistant.managers.EmailManager;

public class AboutFragment extends Fragment implements View.OnClickListener {
    private View fragmentView;
    private EmailManager emailManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        emailManager = new EmailManager();

        fragmentView = inflater.inflate(R.layout.activity_about, container, false);
        fragmentView.findViewById(R.id.sendButton).setOnClickListener(this);

        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        EditText message = fragmentView.findViewById(R.id.messageField);
        Intent intent = emailManager.prepareMessage(message.getText().toString());
        startActivity(Intent.createChooser(intent, "Выберите способ отправки"));
    }

    public void onResume(){
        super.onResume();
        getActivity().setTitle("О приложении");
    }
}
