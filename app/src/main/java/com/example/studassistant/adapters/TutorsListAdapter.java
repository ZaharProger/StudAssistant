package com.example.studassistant.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studassistant.entities.Tutor;

public class TutorsListAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resourceId;
    private Tutor[] objects;

    public TutorsListAdapter(Context context, int resourceId, Tutor[] objects) {
        super(context, resourceId);

        this.context = context;
        this.resourceId = resourceId;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, resourceId, null);

            TextView startPositionItem = (TextView) convertView;
            startPositionItem.setText(objects[0].toString());
        }

        return convertView;
    }

    public Tutor getItemByIndex(int index){
        return objects[index];
    }

}
