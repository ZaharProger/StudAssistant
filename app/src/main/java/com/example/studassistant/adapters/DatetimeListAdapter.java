package com.example.studassistant.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studassistant.R;
import com.example.studassistant.entities.ConsultDatetime;

import java.util.ArrayList;

public class DatetimeListAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resourceId;
    private String[] viewObjects;
    private ArrayList<ConsultDatetime> objects;

    public DatetimeListAdapter(Context context, int resourceId, ArrayList<ConsultDatetime> objects) {
        super(context, resourceId, mapData(objects));

        this.context = context;
        this.resourceId = resourceId;
        this.objects = objects;
        viewObjects = mapData(objects);
    }

    private static String[] mapData(ArrayList<ConsultDatetime> objects) {
        String[] mappedDatetime = new String[objects.size()];

        for (int i = 0; i < mappedDatetime.length; ++i)
            mappedDatetime[i] = objects.get(i).toString();

        return mappedDatetime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, resourceId, null);

            TextView spaceField = convertView.findViewById(R.id.spaceField);
            spaceField.setText(objects.get(position).getSpaceStat());

            double spaceStat = objects.get(position).getOrderedSpace() / (1.0 * objects.get(position).getMaxSpace());
            if (spaceStat < 0.5)
                spaceField.setBackgroundColor(Color.parseColor("#32CD32"));
            else if (spaceStat < 0.75)
                spaceField.setBackgroundColor(Color.parseColor("#FF8C00"));
            else
                spaceField.setBackgroundColor(Color.parseColor("#FF0000"));

            TextView consultData = convertView.findViewById(R.id.consultData);
            consultData.setText(viewObjects[position]);
        }

        return convertView;
    }

    public ConsultDatetime getItemByIndex(int index) {
        return objects.get(index);
    }

    public ConsultDatetime getItemById(long id){
        ConsultDatetime foundData = new ConsultDatetime();
        for (ConsultDatetime item : objects){
            if (item.getId() == id)
                foundData = item;
        }

        return foundData;
    }
}
