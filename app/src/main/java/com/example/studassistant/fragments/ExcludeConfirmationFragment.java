package com.example.studassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studassistant.R;
import com.example.studassistant.adapters.LikedListAdapter;
import com.example.studassistant.entities.LikedListElement;
import com.example.studassistant.managers.DataBaseManager;

import java.util.ArrayList;

public class ExcludeConfirmationFragment extends DialogFragment implements View.OnClickListener{
    private LikedListElement itemToRemove;
    private LikedFragment fragment;
    private LikedListAdapter adapter;
    private DataBaseManager dataBaseManager;
    private  boolean bySwipe;
    private int indexToRemove;

    public ExcludeConfirmationFragment(LikedFragment fragment, LikedListAdapter adapter, Context context, boolean bySwipe, int indexToRemove, LikedListElement itemToRemove){
        this.fragment = fragment;
        this.adapter = adapter;
        this.bySwipe = bySwipe;
        this.indexToRemove = indexToRemove;
        this.itemToRemove = itemToRemove;

        dataBaseManager = DataBaseManager.create(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delete_confirmation, container, false);

        view.findViewById(R.id.delete_yes_button).setOnClickListener(this);
        view.findViewById(R.id.delete_no_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete_yes_button) {
            if (!bySwipe) {
                ArrayList<Integer> notesToRemove = adapter.getNotesToRemove();
                for (int i = 0; i < notesToRemove.size(); ++i) {
                    adapter.removeCheckedItem(notesToRemove.get(i));

                    dataBaseManager.remove(notesToRemove.get(i));
                }
                adapter.updateCheckStatus();
            }
            else {
                dataBaseManager.remove(itemToRemove.getId());
            }

            Toast.makeText(getContext(), R.string.remove_success_text, Toast.LENGTH_LONG).show();
        }
        else {
            if (bySwipe)
                adapter.addItem(indexToRemove, itemToRemove);
        }

        fragment.setVisibilities();

        onDestroy();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
