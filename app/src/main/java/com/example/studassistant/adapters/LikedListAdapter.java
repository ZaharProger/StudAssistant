package com.example.studassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.entities.LikedListElement;
import com.example.studassistant.fragments.AppointmentFragment;
import com.example.studassistant.fragments.ViewFragment;

import java.util.ArrayList;

public class LikedListAdapter extends RecyclerView.Adapter<LikedListAdapter.LikedListViewHolder> {
    private ArrayList<LikedListElement> recyclerViewItems;
    private FragmentManager fragmentManager;
    private Context context;

    public LikedListAdapter(FragmentManager fragmentManager, Context context, ArrayList<LikedListElement> itemsList){
        recyclerViewItems = itemsList;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @NonNull
    @Override
    public LikedListAdapter.LikedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_list_item, parent, false);

        return new LikedListAdapter.LikedListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedListAdapter.LikedListViewHolder holder, int position) {
        LikedListElement item = recyclerViewItems.get(position);

        holder.tutorData.setText(item.getPersonal());
        if (holder.checkToExcludeButton.isChecked())
            holder.checkToExcludeButton.setChecked(false);

        item.setCheckToExcludeButton(holder.checkToExcludeButton);
        holder.checkToExcludeButton.setChecked(item.getCheckToExcludeButton().isChecked());

        holder.makeAppointmentButton.setOnClickListener((view) ->
            fragmentManager.beginTransaction().hide(fragmentManager.getFragments().get(0))
                                            .add(R.id.windowContainer, new AppointmentFragment(item))
                                            .addToBackStack("stack")
                                            .commit());
        holder.viewButton.setOnClickListener((view) -> new ViewFragment(item.getId()).show(fragmentManager, "View"));
        item.setMakeAppointmentButton(holder.makeAppointmentButton);
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    public ArrayList<Integer> getNotesToRemove(){
        ArrayList<Integer> notesToRemove = new ArrayList<>();

        for (LikedListElement element : recyclerViewItems){
            if (element.getCheckToExcludeButton().isChecked())
                notesToRemove.add(element.getId());
        }

        return notesToRemove;
    }

    public void removeCheckedItem(long itemId) {
        recyclerViewItems.removeIf(item -> item.getCheckToExcludeButton().isChecked() && item.getId() == itemId);
        notifyDataSetChanged();
    }

    public LikedListElement getItemByIndex(int index){
        return recyclerViewItems.get(index);
    }

    public void addItem(int index, LikedListElement item){
        recyclerViewItems.add(index, item);
        notifyItemInserted(index);
    }

    public void removeSwipedItem(int pos) {
        recyclerViewItems.remove(pos);
        notifyDataSetChanged();

        updateCheckStatus();
    }

    public void updateCheckStatus() {
        recyclerViewItems.forEach(item -> item.getCheckToExcludeButton().setChecked(false));
    }

    static class LikedListViewHolder extends RecyclerView.ViewHolder{
        private TextView tutorData;
        private CheckBox checkToExcludeButton;
        private ImageButton makeAppointmentButton;
        private ImageButton viewButton;

        LikedListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.checkToExcludeButton = itemView.findViewById(R.id.checkToExcludeButton);
            this.tutorData = itemView.findViewById(R.id.tutorData);
            this.makeAppointmentButton = itemView.findViewById(R.id.makeAppointmentButton);
            this.viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}
