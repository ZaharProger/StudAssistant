package com.example.studassistant.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.adapters.AppointmentsListAdapter;
import com.example.studassistant.entities.RecyclerViewElement;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.managers.DeleteRequestManager;
import com.example.studassistant.managers.GetRequestManager;

import java.util.ArrayList;

public class MyAppointmentFragment extends Fragment implements View.OnClickListener{
    private RecyclerView appointmentsList;
    private GetRequestManager getRequestManager;
    private DeleteRequestManager deleteRequestManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actitivty_my_appointment, container, false);

        view.findViewById(R.id.removeButton).setOnClickListener(this);

        appointmentsList = view.findViewById(R.id.appointmentsList);
        appointmentsList.setVisibility(View.INVISIBLE);
        appointmentsList.setHasFixedSize(true);
        appointmentsList.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AppointmentsListAdapter adapter = (AppointmentsListAdapter) appointmentsList.getAdapter();

                int indexToRemove = viewHolder.getAdapterPosition();
                RecyclerViewElement itemToRemove = adapter.getItemByIndex(indexToRemove);

                adapter.removeSwipedItem(indexToRemove);
                adapter.updateCheckStatus();

                new DeleteConfirmationFragment(adapter, getContext(), true, indexToRemove, itemToRemove).show(getParentFragmentManager(), "DeleteConfirmation");
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemToSwipe = viewHolder.itemView;

                ColorDrawable backgroundOfItem = new ColorDrawable(Color.parseColor("#336C9A"));
                Drawable removeIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_remove);

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

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(appointmentsList);

        getRequestManager = new GetRequestManager(getContext(), ArrayType.APPOINTMENTS, null, appointmentsList);
        deleteRequestManager = new DeleteRequestManager(getContext(), ArrayType.APPOINTMENTS, 0);

        if (getRequestManager.checkConnection())
            getRequestManager.createRequest();
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onClick(View view) {
        AppointmentsListAdapter adapter = (AppointmentsListAdapter) appointmentsList.getAdapter();

        if (!adapter.getAppointmentsToRemove().isEmpty())
            new DeleteConfirmationFragment(adapter, getContext(), false, 0, null).show(getParentFragmentManager(), "DeleteConfirmation");
        else
            Toast.makeText(getContext(), R.string.remove_failure_text, Toast.LENGTH_LONG).show();
    }
}
