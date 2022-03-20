package com.example.studassistant.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.studassistant.entities.AppointmentsListElement;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;
import com.example.studassistant.managers.DeleteRequestManager;
import com.example.studassistant.managers.GetRequestManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class MyAppointmentFragment extends Fragment implements View.OnClickListener, TextWatcher, View.OnLayoutChangeListener {
    private RecyclerView appointmentsList;
    private GetRequestManager getRequestManager;
    private DeleteRequestManager deleteRequestManager;
    private EditText codeFilter;
    private ImageView notFoundImage;
    private ProgressBar progressBar;
    private Button removeAppointmentButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actitivty_my_appointment, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        notFoundImage = view.findViewById(R.id.notFoundImage);
        notFoundImage.setVisibility(View.INVISIBLE);

        removeAppointmentButton = view.findViewById(R.id.removeAppointmentButton);
        removeAppointmentButton.setOnClickListener(this);
        removeAppointmentButton.setVisibility(View.INVISIBLE);

        view.findViewById(R.id.autoFillButton).setOnClickListener(this);

        codeFilter = view.findViewById(R.id.codeFilter);
        codeFilter.addTextChangedListener(this);

        appointmentsList = view.findViewById(R.id.appointmentsList);
        appointmentsList.addOnLayoutChangeListener(this);
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
                AppointmentsListElement itemToRemove = adapter.getItemByIndex(indexToRemove);

                adapter.removeSwipedItem(indexToRemove);
                adapter.updateCheckStatus();

                new DeleteConfirmationFragment(MyAppointmentFragment.this, adapter, getContext(), true, indexToRemove, itemToRemove).show(getParentFragmentManager(), "DeleteConfirmation");
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

        getRequestManager = new GetRequestManager(getContext(), ArrayType.APPOINTMENTS, null, appointmentsList, null, ExtraType.USERCODE);
        codeFilter.setText("");

        deleteRequestManager = new DeleteRequestManager(getContext(), ArrayType.APPOINTMENTS, 0);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.removeAppointmentButton){
            AppointmentsListAdapter adapter = (AppointmentsListAdapter) appointmentsList.getAdapter();

            if (!adapter.getAppointmentsToRemove().isEmpty())
                new DeleteConfirmationFragment(MyAppointmentFragment.this, adapter, getContext(), false, 0, null).show(getParentFragmentManager(), "DeleteConfirmation");
            else
                Toast.makeText(getContext(), R.string.remove_failure_text, Toast.LENGTH_LONG).show();
        }
        else
            codeFilter.setText(getUserCode());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (getRequestManager.checkConnection()){
            progressBar.setVisibility(View.VISIBLE);
            appointmentsList.setVisibility(View.INVISIBLE);
            notFoundImage.setVisibility(View.INVISIBLE);

            getRequestManager.setRequestExtra(codeFilter.getText().toString());
            getRequestManager.createRequest();
        }
        else
            Toast.makeText(getContext(), R.string.connection_error_text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private String getUserCode(){
        String userCode;

        try (BufferedReader reader = new BufferedReader(new FileReader(getContext().getFilesDir() + "/code.txt"))){
            userCode = reader.lines().collect(Collectors.joining());
        }
        catch(IOException exception){
            userCode = "У вас нет идентификатора!";
        }

        return userCode;
    }

    private void updateAnimation(){
        notFoundImage.setBackgroundResource(R.drawable.not_found_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) notFoundImage.getBackground();
        animationDrawable.start();
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        setVisibilities();
    }

    public void setVisibilities() {
        progressBar.setVisibility(View.INVISIBLE);

        try{
            if (appointmentsList.getAdapter().getItemCount() == 0){
                notFoundImage.setVisibility(View.VISIBLE);
                appointmentsList.setVisibility(View.INVISIBLE);
                removeAppointmentButton.setVisibility(View.INVISIBLE);

                updateAnimation();
            }
            else{
                notFoundImage.setVisibility(View.INVISIBLE);
                appointmentsList.setVisibility(View.VISIBLE);
                removeAppointmentButton.setVisibility(View.VISIBLE);
            }
        }
        catch (NullPointerException exception){
            notFoundImage.setVisibility(View.VISIBLE);
            appointmentsList.setVisibility(View.INVISIBLE);
            removeAppointmentButton.setVisibility(View.INVISIBLE);

            updateAnimation();
        }
    }

    public void onPause(){
        super.onPause();

        codeFilter.setText("");
    }
}
