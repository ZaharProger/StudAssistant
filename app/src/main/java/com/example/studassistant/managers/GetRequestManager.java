package com.example.studassistant.managers;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.studassistant.R;
import com.example.studassistant.adapters.AppointmentsListAdapter;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.Group;
import com.example.studassistant.entities.RecyclerViewElement;
import com.example.studassistant.entities.Tutor;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetRequestManager extends RequestManager implements Response.Listener<JSONArray>, Response.ErrorListener {
    private Spinner itemsListSpinner;
    private RecyclerView itemsListRecyclerView;
    private String dataToRemember;
    private String requestExtra;
    private ExtraType extraType;

    public GetRequestManager(Context context, ArrayType type, Spinner itemsListSpinner, RecyclerView itemsListRecyclerView){
        super(context, type);
        this.itemsListSpinner = itemsListSpinner;
        this.itemsListRecyclerView = itemsListRecyclerView;

        checkConnection();
    }

    public GetRequestManager(Context context, ArrayType type, Spinner itemsListSpinner, RecyclerView itemsListRecyclerView, String requestExtra, ExtraType extraType){
        super(context, type);
        this.itemsListSpinner = itemsListSpinner;
        this.itemsListRecyclerView = itemsListRecyclerView;
        this.requestExtra = requestExtra;
        this.extraType = extraType;

        checkConnection();
    }

    public GetRequestManager(Context context, ArrayType type, Spinner itemsListSpinner, RecyclerView itemsListRecyclerView, String dataToRemember){
        super(context, type);
        this.itemsListSpinner = itemsListSpinner;
        this.itemsListRecyclerView = itemsListRecyclerView;
        this.dataToRemember = dataToRemember;

        checkConnection();
    }

    public GetRequestManager(Context context, Spinner itemsListSpinner, RecyclerView itemsListRecyclerView){
        super(context, null);
        this.itemsListSpinner = itemsListSpinner;
        this.itemsListRecyclerView = itemsListRecyclerView;

        checkConnection();
    }

    @Override
    public void createRequest() {
        String preparedURL = URL + ((type != ArrayType.DATETIME)? type.toString().toLowerCase(Locale.ROOT) : "tutors");

        if (requestExtra != null){
            String extras = String.format("?%s=%s", extraType.toString().toLowerCase(Locale.ROOT), requestExtra);
            preparedURL += extras;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, preparedURL, null, this, this);
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            ArrayList<Group> groups = null;
            ArrayList<Tutor> tutors = null;
            ArrayList<String> dates = null;
            ArrayList<RecyclerViewElement> appointments = null;

            switch(type){
                case GROUPS:
                    groups = new ArrayList<>();
                    break;
                case TUTORS:
                    tutors = new ArrayList<>();
                    break;
                case DATETIME:
                    dates = new ArrayList<>();
                    break;
                case APPOINTMENTS:
                    appointments = new ArrayList<>();
                    break;
            }

            for (int i = 0; i < response.length(); ++i){
                JSONObject extractedObject = response.getJSONObject(i);
                switch (type){
                    case GROUPS:
                        Group group = new Group();

                        group.setId(extractedObject.getLong("id"));
                        group.setName(extractedObject.getString("name"));

                        groups.add(group);
                        break;
                    case TUTORS:
                        Tutor tutor = new Tutor();

                        tutor.setId(extractedObject.getLong("id"));
                        tutor.setName(extractedObject.getString("name"));
                        tutor.setSurname(extractedObject.getString("surname"));
                        tutor.setPatronymic(extractedObject.getString("patronymic"));

                        tutors.add(tutor);
                        break;
                    case DATETIME:
                        Tutor tutorToTakeDatetime = new Tutor();
                        tutorToTakeDatetime.setName(extractedObject.getString("name"));
                        tutorToTakeDatetime.setSurname(extractedObject.getString("surname"));
                        tutorToTakeDatetime.setPatronymic(extractedObject.getString("patronymic"));

                        if (dataToRemember.equalsIgnoreCase(tutorToTakeDatetime.toString())){
                            JSONArray tutor_dates = (JSONArray) extractedObject.get("dates");

                            for (int j = 0; j < tutor_dates.length(); ++j){
                                JSONObject datetimeObject = tutor_dates.getJSONObject(j);

                                String datetime = datetimeObject.getInt("day") + " " + datetimeObject.getString("time") + " " + datetimeObject.getString("room");
                                dates.add(datetime);
                            }
                        }
                        break;
                    case APPOINTMENTS:
                        Appointment appointment = new Appointment();

                        appointment.setId(extractedObject.getLong("id"));
                        appointment.setName(extractedObject.getString("name"));
                        appointment.setSurname(extractedObject.getString("surname"));
                        appointment.setGroup(extractedObject.getString("group"));
                        appointment.setTutor(extractedObject.getString("tutor"));
                        appointment.setDatetime(extractedObject.getString("datetime"));

                        RecyclerViewElement recyclerViewElement = new RecyclerViewElement(appointment, null);

                        appointments.add(recyclerViewElement);
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
                case TUTORS:
                    mappedData = new String[tutors.size()];
                    for (int i = 0; i < tutors.size(); ++i)
                        mappedData[i] = tutors.get(i).toString();
                    break;
                case DATETIME:
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE dd.MM.yyyy", new Locale("ru"));
                    mappedData = new String[dates.size()];
                    for(int i = 0; i < dates.size(); ++i){
                        String[] splittedDate = dates.get(i).trim().split("[\\s]+");

                        Calendar now = Calendar.getInstance();

                        int actualDay = now.get(Calendar.DAY_OF_WEEK);
                        int days = Integer.parseInt(splittedDate[0]) - actualDay;
                        if (days <= 0)
                            days += 7;

                        now.add(Calendar.DAY_OF_YEAR, days);

                        Date date = now.getTime();
                        String formattedDate = dateFormatter.format(date);

                        mappedData[i] = formattedDate + " " + splittedDate[1] + " " + splittedDate[2];
                    }
                    break;
            }

            if (itemsListSpinner != null){
                ArrayAdapter<String> adapter;
                if (mappedData.length != 0)
                    adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, mappedData);
                else
                    adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, new String[]{"Информация не найдена!"});

                adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                adapter.notifyDataSetChanged();

                itemsListSpinner.setAdapter(adapter);
            }

            if (itemsListRecyclerView != null){
                AppointmentsListAdapter appointmentsListAdapter = new AppointmentsListAdapter(appointments);
                itemsListRecyclerView.setAdapter(appointmentsListAdapter);
            }
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    public void setRequestExtra(String requestExtra) {
        this.requestExtra = requestExtra;
    }
}
