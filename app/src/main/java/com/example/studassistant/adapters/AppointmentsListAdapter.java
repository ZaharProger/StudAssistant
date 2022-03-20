package com.example.studassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.AppointmentsListElement;

import java.util.ArrayList;

public class AppointmentsListAdapter extends RecyclerView.Adapter<AppointmentsListAdapter.AppointmentsListViewHolder> {
    private ArrayList<AppointmentsListElement> recyclerViewItems;

    public AppointmentsListAdapter(ArrayList<AppointmentsListElement> itemsList){
        recyclerViewItems = itemsList;
    }

    @NonNull
    @Override
    public AppointmentsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointments_list_item, parent, false);

        return new AppointmentsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsListViewHolder holder, int position) {
        AppointmentsListElement item = recyclerViewItems.get(position);
        Appointment currentItem = item.getAppointment();

        holder.personalData.setText(String.format("%s %s %s", currentItem.getName(), currentItem.getSurname(), currentItem.getGroup()));
        holder.appointmentData.setText(String.format("%s %s", currentItem.getTutor(), currentItem.getDatetime()));
        if (holder.checkToRemoveButton.isChecked())
            holder.checkToRemoveButton.setChecked(false);

        item.setCheckToRemoveButton(holder.checkToRemoveButton);
        holder.checkToRemoveButton.setChecked(item.getCheckToRemoveButton().isChecked());
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    public ArrayList<Appointment> getAppointmentsToRemove(){
        ArrayList<Appointment> appointmentsToRemove = new ArrayList<>();

        for (AppointmentsListElement element : recyclerViewItems){
            if (element.getCheckToRemoveButton().isChecked())
                appointmentsToRemove.add(element.getAppointment());
        }

        return appointmentsToRemove;
    }

    public void removeCheckedItem(long itemId) {
        recyclerViewItems.removeIf(item -> item.getCheckToRemoveButton().isChecked() && item.getAppointment().getId() == itemId);
        notifyDataSetChanged();
    }

    public AppointmentsListElement getItemByIndex(int index){
        return recyclerViewItems.get(index);
    }

    public void addItem(int index, AppointmentsListElement item){
        recyclerViewItems.add(index, item);
        notifyItemInserted(index);
    }

    public void removeSwipedItem(int pos) {
        recyclerViewItems.remove(pos);
        notifyDataSetChanged();

        updateCheckStatus();
    }

    public void updateCheckStatus() {
        recyclerViewItems.forEach(item -> item.getCheckToRemoveButton().setChecked(false));
    }

    static class AppointmentsListViewHolder extends RecyclerView.ViewHolder{
        TextView personalData;
        TextView appointmentData;
        CheckBox checkToRemoveButton;

        AppointmentsListViewHolder(@NonNull View itemView) {
            super(itemView);

            personalData = itemView.findViewById(R.id.personalData);
            appointmentData = itemView.findViewById(R.id.appointmentData);
            checkToRemoveButton = itemView.findViewById(R.id.checkToRemoveButton);
        }
    }
}
