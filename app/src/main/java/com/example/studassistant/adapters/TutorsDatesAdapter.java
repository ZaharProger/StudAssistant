package com.example.studassistant.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studassistant.R;
import com.example.studassistant.entities.ConsultDatetime;

import java.util.ArrayList;

public class TutorsDatesAdapter extends RecyclerView.Adapter<TutorsDatesAdapter.TutorsDatesViewHolder>{
    private ArrayList<ConsultDatetime> recyclerViewItems;

    public TutorsDatesAdapter(ArrayList<ConsultDatetime> itemsList){
        recyclerViewItems = itemsList;
    }

    @NonNull
    @Override
    public TutorsDatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_dates_item, parent, false);

        Log.e("AAA", "2");

        return new TutorsDatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorsDatesViewHolder holder, int position){
        ConsultDatetime item = recyclerViewItems.get(position);

        String[] datetimeInfo = item.toString().split("[\\s]+");

        holder.datetimeField.setText(String.format("%s %s %s", datetimeInfo[0], datetimeInfo[1], datetimeInfo[2]));
        holder.roomField.setText(datetimeInfo[3]);
        holder.orderedSpaceField.setText(item.getSpaceStat());

        double spaceStat = item.getOrderedSpace() / (1.0 * item.getMaxSpace());
        if (spaceStat < 0.5)
            holder.orderedSpaceField.setBackgroundColor(Color.parseColor("#32CD32"));
        else if (spaceStat < 0.75)
            holder.orderedSpaceField.setBackgroundColor(Color.parseColor("#FF8C00"));
        else
            holder.orderedSpaceField.setBackgroundColor(Color.parseColor("#FF0000"));
    }

    @Override
    public int getItemCount(){
        return recyclerViewItems.size();
    }

    static class TutorsDatesViewHolder extends RecyclerView.ViewHolder {
        TextView datetimeField;
        TextView roomField;
        TextView orderedSpaceField;

        TutorsDatesViewHolder(@NonNull View itemView) {
            super(itemView);

            datetimeField = itemView.findViewById(R.id.datetimeField);
            roomField = itemView.findViewById(R.id.roomField);
            orderedSpaceField = itemView.findViewById(R.id.orderedSpaceField);
        }
    }
}

