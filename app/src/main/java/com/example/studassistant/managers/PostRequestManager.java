package com.example.studassistant.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.studassistant.entities.Appointment;
import com.example.studassistant.enums.ArrayType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class PostRequestManager extends RequestManager implements Response.Listener<JSONObject>, Response.ErrorListener{
    private Appointment dataToPost;
    public PostRequestManager(Context context, ArrayType type, Appointment dataToPost){
        super(context, type);

        this.dataToPost = dataToPost;
    }

    @Override
    public void createRequest() {
        JSONObject preparedDataToPost = new JSONObject();

        try {
            preparedDataToPost.put("id", dataToPost.getId());
            preparedDataToPost.put("name", dataToPost.getName());
            preparedDataToPost.put("surname", dataToPost.getSurname());
            preparedDataToPost.put("group", dataToPost.getGroup());
            preparedDataToPost.put("tutor", dataToPost.getTutor());
            preparedDataToPost.put("datetime", dataToPost.getDatetime());
            preparedDataToPost.put("usercode", dataToPost.getUserCode());
        }
        catch (
                JSONException exception) {
            exception.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL + type.toString().toLowerCase(Locale.ROOT), preparedDataToPost, this, this);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
