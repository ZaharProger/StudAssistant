package com.example.studassistant.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.studassistant.adapters.LikedListAdapter;
import com.example.studassistant.entities.LikedListElement;
import com.example.studassistant.managers.DataBaseManager;

public class LikedFragment extends Fragment  implements View.OnClickListener, View.OnLayoutChangeListener {
    private RecyclerView likedList;
    private DataBaseManager dataBaseManager;
    private ImageView notFoundImageDuplicated;
    private ProgressBar likedProgressBar;
    private Button removeLikedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_liked, container, false);

        removeLikedButton = view.findViewById(R.id.removeLikedButton);
        removeLikedButton.setOnClickListener(this);
        removeLikedButton.setVisibility(View.INVISIBLE);

        likedProgressBar = view.findViewById(R.id.likedProgressBar);

        notFoundImageDuplicated = view.findViewById(R.id.notFoundImageDuplicated);
        notFoundImageDuplicated.setVisibility(View.INVISIBLE);

        dataBaseManager = DataBaseManager.create(getContext());

        likedList = view.findViewById(R.id.likedList);
        likedList.addOnLayoutChangeListener(this);
        likedList.setVisibility(View.INVISIBLE);
        likedList.setHasFixedSize(true);
        likedList.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                LikedListAdapter adapter = (LikedListAdapter) likedList.getAdapter();

                int indexToRemove = viewHolder.getAdapterPosition();
                LikedListElement itemToRemove = adapter.getItemByIndex(indexToRemove);

                adapter.removeSwipedItem(indexToRemove);
                adapter.updateCheckStatus();

                new ExcludeConfirmationFragment(LikedFragment.this, adapter, getContext(), true, indexToRemove, itemToRemove).show(getParentFragmentManager(), "DeleteConfirmation");
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

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(likedList);

        updateList();

        return view;
    }

    @Override
    public void onClick(View view) {
        LikedListAdapter adapter = (LikedListAdapter) likedList.getAdapter();

        if (!adapter.getNotesToRemove().isEmpty())
            new ExcludeConfirmationFragment(LikedFragment.this, adapter, getContext(), false, 0, null).show(getParentFragmentManager(), "ExcludeConfirmation");
        else
            Toast.makeText(getContext(), R.string.remove_failure_text, Toast.LENGTH_LONG).show();
    }

    private void updateAnimation(){
        notFoundImageDuplicated.setBackgroundResource(R.drawable.not_found_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) notFoundImageDuplicated.getBackground();
        animationDrawable.start();
    }

    public void updateList() {
        LikedListAdapter likedListAdapter = new LikedListAdapter(getParentFragmentManager(), dataBaseManager.getAllData());
        likedList.setAdapter(likedListAdapter);
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        setVisibilities();
    }

    public void setVisibilities() {
        likedProgressBar.setVisibility(View.INVISIBLE);

        try{
            if (likedList.getAdapter().getItemCount() == 0){
                notFoundImageDuplicated.setVisibility(View.VISIBLE);
                likedList.setVisibility(View.INVISIBLE);
                removeLikedButton.setVisibility(View.INVISIBLE);

                updateAnimation();
            }
            else{
                notFoundImageDuplicated.setVisibility(View.INVISIBLE);
                likedList.setVisibility(View.VISIBLE);
                removeLikedButton.setVisibility(View.VISIBLE);
            }
        }
        catch (NullPointerException exception){
            notFoundImageDuplicated.setVisibility(View.VISIBLE);
            likedList.setVisibility(View.INVISIBLE);
            removeLikedButton.setVisibility(View.INVISIBLE);

            updateAnimation();
        }
    }

    public void onResume(){
        super.onResume();
        getActivity().setTitle("Избранное");
    }
}
