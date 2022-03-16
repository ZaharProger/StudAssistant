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
import com.example.studassistant.adapters.AppointmentsListAdapter;
import com.example.studassistant.entities.RecyclerViewElement;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.DeleteRequestManager;

import java.util.ArrayList;

public class DeleteConfirmationFragment extends DialogFragment implements View.OnClickListener{
    private RecyclerViewElement itemToRemove;
    private AppointmentsListAdapter adapter;
    private DeleteRequestManager deleteRequestManager;
    private Context context;
    private  boolean bySwipe;
    private int indexToRemove;

    public DeleteConfirmationFragment(AppointmentsListAdapter adapter, Context context, boolean bySwipe, int indexToRemove, RecyclerViewElement itemToRemove){
        this.adapter = adapter;
        this.context = context;
        this.bySwipe = bySwipe;
        this.indexToRemove = indexToRemove;
        this.itemToRemove = itemToRemove;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delete_confirmation, container, false);

        view.findViewById(R.id.delete_yes_button).setOnClickListener(this);
        view.findViewById(R.id.delete_no_button).setOnClickListener(this);

        deleteRequestManager = new DeleteRequestManager(context, ArrayType.APPOINTMENTS, 0);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete_yes_button){
            if (deleteRequestManager.checkConnection()){
                if (!bySwipe){
                    ArrayList<Long> appointmentsToRemove = adapter.getAppointmentsToRemove();
                    for (int i = 0; i < appointmentsToRemove.size(); ++i){
                        adapter.removeCheckedItem(appointmentsToRemove.get(i));

                        deleteRequestManager.setIdToDelete(appointmentsToRemove.get(i));
                        deleteRequestManager.createRequest();
                    }
                    adapter.updateCheckStatus();
                }
                else{
                    deleteRequestManager.setIdToDelete(itemToRemove.getAppointment().getId());
                    deleteRequestManager.createRequest();
                }

                Toast.makeText(getContext(), R.string.remove_success_text, Toast.LENGTH_LONG).show();
            }
            else{
                if (bySwipe)
                    adapter.addItem(indexToRemove, itemToRemove);

                Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
            }
        }
        else{
            if (bySwipe)
                adapter.addItem(indexToRemove, itemToRemove);
        }

        onDestroy();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dismiss();
    }
}
