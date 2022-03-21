package com.example.studassistant.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studassistant.R;

public class StartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_start, container, false);

        ImageView startImage = view.findViewById(R.id.startImage);

        startImage.setBackgroundResource(R.drawable.start_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) startImage.getBackground();
        animationDrawable.start();

        return view;
    }

    public void onResume(){
        super.onResume();
        getActivity().setTitle("Студ Ассистент");
    }
}
