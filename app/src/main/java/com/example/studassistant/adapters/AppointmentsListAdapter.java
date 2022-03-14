package com.example.studassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.DeleteRequestManager;

import java.util.ArrayList;

public class AppointmentsListAdapter extends RecyclerView.Adapter<AppointmentsListAdapter.AppointmentsListViewHolder> {
    private ArrayList<Appointment> itemsList;
    private ItemTouchHelper.SimpleCallback itemTouchHelper;
    private DeleteRequestManager deleteRequestManager;

    public AppointmentsListAdapter(ArrayList<Appointment> itemsList, Context context){
        this.itemsList = itemsList;
        deleteRequestManager = new DeleteRequestManager(context, ArrayType.APPOINTMENTS, 0);

        itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int indexToRemove = viewHolder.getAdapterPosition();
                Appointment itemToRemove = itemsList.get(indexToRemove);

                itemsList.remove(indexToRemove);
                notifyDataSetChanged();

                if (deleteRequestManager.checkConnection()){
                    deleteRequestManager.setIdToDelete(itemToRemove.getId());
                    deleteRequestManager.createRequest();
                }
                else {
                    itemsList.add(indexToRemove, itemToRemove);
                    notifyItemInserted(indexToRemove);

                    Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public ItemTouchHelper.SimpleCallback getItemTouchHelper() {
        return itemTouchHelper;
    }

    @NonNull
    @Override
    public AppointmentsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointments_list_item, parent, false);

        return new AppointmentsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsListViewHolder holder, int position) {
        Appointment currentItem = itemsList.get(position);

        holder.personalData.setText(String.format("%s %s %s", currentItem.getName(), currentItem.getSurname(), currentItem.getGroup()));
        holder.appointmentData.setText(String.format("%s %s", currentItem.getTutor(), currentItem.getDatetime()));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    static class AppointmentsListViewHolder extends RecyclerView.ViewHolder{
        TextView personalData;
        TextView appointmentData;

        public AppointmentsListViewHolder(@NonNull View itemView) {
            super(itemView);

            personalData = itemView.findViewById(R.id.personalData);
            appointmentData = itemView.findViewById(R.id.appointmentData);
        }
    }
}
