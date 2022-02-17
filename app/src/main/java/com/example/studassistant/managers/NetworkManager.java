package com.example.studassistant.managers;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.studassistant.R;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.Group;
import com.example.studassistant.enums.ArrayType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class NetworkManager implements Response.Listener<JSONArray>, Response.ErrorListener{
    private static final String URL = "https://stud-assistant-db.herokuapp.com/";
    private Context context;
    private Spinner itemsList;
    private ArrayType type;
    private boolean isConnectionFailed;
    private boolean toRestore;
    private String dataToRestore;

    public NetworkManager(Context context, ArrayType type, Spinner itemsList){
        this.context = context;
        this.type = type;
        this.itemsList = itemsList;

        checkConnection();
    }

    public NetworkManager(Context context, Spinner itemsList){
        this.context = context;
        this.itemsList = itemsList;

        checkConnection();
    }

    public boolean checkConnection() {
        ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connection != null){
            NetworkInfo connectionInfo = connection.getActiveNetworkInfo();
            if (connectionInfo != null)
                isConnectionFailed = connectionInfo.getState() != NetworkInfo.State.CONNECTED;
            else
                isConnectionFailed = true;
        }
        else
            isConnectionFailed = true;

        return !isConnectionFailed;
    }

    public void getData(){
        toRestore = false;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL + type.toString().toLowerCase(Locale.ROOT), null, this, this);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            ArrayList<Group> groups = new ArrayList<>();
            ArrayList<Appointment> appointments = new ArrayList<>();

            for (int i = 0; i < response.length(); ++i){
                JSONObject extractedObject = response.getJSONObject(i);
                switch (type){
                    case GROUPS:
                        Group group = new Group();

                        group.setId(extractedObject.getInt("id"));
                        group.setName(extractedObject.getString("name"));

                        groups.add(group);
                        break;
                    case APPOINTMENTS:
                        Appointment appointment = new Appointment();

                        appointment.setId(extractedObject.getInt("id"));
                        appointment.setName(extractedObject.getString("name"));
                        appointment.setSurname(extractedObject.getString("surname"));
                        appointment.setGroup(extractedObject.getString("group"));
                        appointment.setTutor(extractedObject.getString("tutor"));
                        appointment.setDate(extractedObject.getString("date"));
                        appointment.setTime(extractedObject.getString("time"));

                        appointments.add(appointment);
                        break;
                }
            }

            String[] mappedData = null;

            switch (type){
                case GROUPS:
                    mappedData = new String[groups.size()];
                    for (int i = 0; i < groups.size(); ++i)
                        mappedData[i] = groups.get(i).getName();
                    break;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, mappedData);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            adapter.notifyDataSetChanged();

            itemsList.setAdapter(adapter);

            if (toRestore){
                boolean isFound = false;
                int i;
                for (i = 0; i < mappedData.length && !isFound; ++i)
                    isFound = mappedData[i].equals(dataToRestore);

                itemsList.setSelection(--i);
            }
            else
                itemsList.setSelection(0);
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    public void getDataToRestore(String dataToRestore) {
        toRestore = true;
        this.dataToRestore = dataToRestore;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL + type.toString().toLowerCase(Locale.ROOT), null, this, this);
        Volley.newRequestQueue(context).add(request);
    }
}
