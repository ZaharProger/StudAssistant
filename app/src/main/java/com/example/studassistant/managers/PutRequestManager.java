package com.example.studassistant.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.studassistant.entities.ConsultDatetime;
import com.example.studassistant.enums.ArrayType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class PutRequestManager extends RequestManager implements Response.Listener<JSONObject>, Response.ErrorListener {
    private ConsultDatetime dataToPost;

    public PutRequestManager(Context context, ArrayType type, ConsultDatetime dataToPost) {
        super(context, type);

        this.dataToPost = dataToPost;
    }

    @Override
    public void createRequest() {
        JSONObject preparedDataToPost = new JSONObject();

        try {
            preparedDataToPost.put("id", dataToPost.getId());
            preparedDataToPost.put("tutor_id", dataToPost.getTutorId());
            preparedDataToPost.put("day", dataToPost.getDate());
            preparedDataToPost.put("time", dataToPost.getTime());
            preparedDataToPost.put("room", dataToPost.getRoom());
            preparedDataToPost.put("ordered_space", dataToPost.getOrderedSpace() + 1);
            preparedDataToPost.put("max_space", dataToPost.getMaxSpace());
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, URL + type.toString().toLowerCase(Locale.ROOT) + "/" + dataToPost.getId(), preparedDataToPost, this, this);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    public void setDataToPost(ConsultDatetime dataToPost) {
        this.dataToPost = dataToPost;
    }
}
