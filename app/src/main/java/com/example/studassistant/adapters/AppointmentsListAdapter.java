package com.example.studassistant.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.RecyclerViewElement;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.DeleteRequestManager;

import java.util.ArrayList;

public class AppointmentsListAdapter extends RecyclerView.Adapter<AppointmentsListAdapter.AppointmentsListViewHolder> {
    private ItemTouchHelper.SimpleCallback itemTouchHelper;
    private DeleteRequestManager deleteRequestManager;
    private ArrayList<RecyclerViewElement> recyclerViewItems;

    public AppointmentsListAdapter(ArrayList<RecyclerViewElement> itemsList, Context context){
        recyclerViewItems = itemsList;

        deleteRequestManager = new DeleteRequestManager(context, ArrayType.APPOINTMENTS, 0);

        itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int indexToRemove = viewHolder.getAdapterPosition();
                RecyclerViewElement itemToRemove = recyclerViewItems.get(indexToRemove);

                recyclerViewItems.remove(indexToRemove);
                notifyDataSetChanged();

                if (deleteRequestManager.checkConnection()){
                    deleteRequestManager.setIdToDelete(itemToRemove.getAppointment().getId());
                    deleteRequestManager.createRequest();

                    Toast.makeText(context, R.string.remove_success_text, Toast.LENGTH_LONG).show();
                }
                else {
                    recyclerViewItems.add(indexToRemove, itemToRemove);
                    notifyItemInserted(indexToRemove);

                    Toast.makeText(context, R.string.connection_error_text, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemToSwipe = viewHolder.itemView;

                ColorDrawable backgroundOfItem = new ColorDrawable(Color.parseColor("#336C9A"));
                Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_remove);

                if (dX < 0){
                    backgroundOfItem.setBounds(itemToSwipe.getRight() + (int)dX, itemToSwipe.getTop(),
                            itemToSwipe.getRight(), itemToSwipe.getBottom());
                    backgroundOfItem.draw(c);

                    int removeIconMargin = (itemToSwipe.getHeight() - removeIcon.getIntrinsicHeight()) / 2;

                    int removeIconTop = itemToSwipe.getTop() + removeIconMargin;
                    int removeIconLeft = itemToSwipe.getRight() - removeIconMargin - removeIcon.getIntrinsicWidth();
                    int removeIconRight = itemToSwipe.getRight() - removeIconMargin;
                    int removeIconBottom = removeIconTop + removeIcon.getIntrinsicHeight();

                    removeIcon.setBounds(removeIconLeft, removeIconTop,
                            removeIconRight, removeIconBottom);

                    removeIcon.draw(c);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
        Appointment currentItem = recyclerViewItems.get(position).getAppointment();

        holder.personalData.setText(String.format("%s %s %s", currentItem.getName(), currentItem.getSurname(), currentItem.getGroup()));
        holder.appointmentData.setText(String.format("%s %s", currentItem.getTutor(), currentItem.getDatetime()));

        recyclerViewItems.get(position).setCheckToRemoveButton(holder.checkToRemoveButton);
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    public ArrayList<Long> getAppointmentsToRemove(){
        ArrayList<Long> appointmentsToRemove = new ArrayList<>();

        for (RecyclerViewElement element : recyclerViewItems){
            if (element.getCheckToRemoveButton().isChecked())
                appointmentsToRemove.add(element.getAppointment().getId());
        }

        return appointmentsToRemove;
    }

    public void removeItemById(long appointmentId) {
        int i;
        boolean isFound = false;
        for (i = 0; i < getItemCount() && !isFound; ++i){
            if (recyclerViewItems.get(i).getCheckToRemoveButton().isChecked() && recyclerViewItems.get(i).getAppointment().getId() == appointmentId)
                isFound = true;

            recyclerViewItems.get(i).getCheckToRemoveButton().setChecked(false);
        }

        recyclerViewItems.remove(--i);
        notifyDataSetChanged();
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