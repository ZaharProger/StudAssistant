package com.example.studassistant.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.studassistant.enums.ArrayType;

import org.json.JSONObject;

import java.util.Locale;

public class DeleteRequestManager extends RequestManager implements Response.Listener<JSONObject>, Response.ErrorListener{
    private long idToDelete;

    public DeleteRequestManager(Context context, ArrayType type, long idToDelete){
        super(context, type);

        this.idToDelete = idToDelete;
    }

    @Override
    public void createRequest() {
        JSONObject preparedDataToPost = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL + type.toString().toLowerCase(Locale.ROOT) + "/" + idToDelete,
                preparedDataToPost, this, this);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    public void setIdToDelete(long idToDelete) {
        this.idToDelete = idToDelete;
    }
}
