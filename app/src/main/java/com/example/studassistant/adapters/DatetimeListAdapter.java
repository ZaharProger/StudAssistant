package com.example.studassistant.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

            TextView startPositionItem = (TextView) convertView;
            startPositionItem.setText(viewObjects[position]);
        }

        return convertView;
    }

    public ConsultDatetime getItemByIndex(int index) {
        return objects.get(index);
    }
}
