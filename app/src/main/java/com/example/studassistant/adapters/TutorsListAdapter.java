package com.example.studassistant.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studassistant.entities.Tutor;

import java.util.ArrayList;

public class TutorsListAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resourceId;
    private ArrayList<Tutor> objects;
    private String[] viewObjects;

    public TutorsListAdapter(Context context, int resourceId, ArrayList<Tutor> objects) {
        super(context, resourceId, mapData(objects));

        this.context = context;
        this.resourceId = resourceId;
        this.objects = objects;
        viewObjects = mapData(objects);
    }

    private static String[] mapData(ArrayList<Tutor> objects) {
        String[] mappedObjects = new String[objects.size()];

        for (int i = 0; i < mappedObjects.length; ++i)
            mappedObjects[i] = objects.get(i).toString();

        return mappedObjects;
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

    public Tutor getItemByIndex(int index){
        return objects.get(index);
    }

}
