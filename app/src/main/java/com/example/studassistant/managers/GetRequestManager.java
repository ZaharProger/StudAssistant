package com.example.studassistant.managers;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.studassistant.R;
import com.example.studassistant.adapters.AppointmentsListAdapter;
import com.example.studassistant.adapters.DatetimeListAdapter;
import com.example.studassistant.adapters.TutorsListAdapter;
import com.example.studassistant.entities.Appointment;
import com.example.studassistant.entities.ConsultDatetime;
import com.example.studassistant.entities.Group;
import com.example.studassistant.entities.AppointmentsListElement;
import com.example.studassistant.entities.Tutor;
import com.example.studassistant.enums.ArrayType;
import com.example.studassistant.enums.ExtraType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class GetRequestManager extends RequestManager implements Response.Listener<JSONArray>, Response.ErrorListener {
    private Spinner itemsListSpinner;
    private RecyclerView itemsListRecyclerView;
    private String dataToRemember;
    private String requestExtra;
    private ExtraType extraType;
    private TextView monitorMessage;
    private boolean toMonitor;

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

    public void setMonitorValue(TextView messageField){
        monitorMessage = messageField;
        toMonitor = true;
    }

    @Override
    public void createRequest() {
        String preparedURL = URL + type.toString().toLowerCase(Locale.ROOT);

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
            ArrayList<ConsultDatetime> dates = null;
            ArrayList<AppointmentsListElement> appointments = null;

            switch(type){
                case GROUPS:
                    groups = new ArrayList<>();
                    break;
                case TUTORS:
                    tutors = new ArrayList<>();
                    break;
                case DATES:
                    dates = new ArrayList<>();
                    break;
                case APPOINTMENTS:
                    appointments = new ArrayList<>();
                    break;
            }

            boolean isFound = false;

            for (int i = 0; i < response.length(); ++i){
                JSONObject extractedObject = response.getJSONObject(i);
                switch (type){
                    case GROUPS:
                        Group group = new Group();

                        group.setId(extractedObject.getLong("id"));
                        group.setName(extractedObject.getString("name"));

                        String groupName = group.getName().toLowerCase(Locale.ROOT);

                        if (groupName.contains(dataToRemember.toLowerCase(Locale.ROOT)))
                            groups.add(group);
                        break;
                    case TUTORS:
                        Tutor tutor = new Tutor();

                        tutor.setId(extractedObject.getLong("id"));
                        tutor.setName(extractedObject.getString("name"));
                        tutor.setSurname(extractedObject.getString("surname"));
                        tutor.setPatronymic(extractedObject.getString("patronymic"));

                        if (tutor.toString().toLowerCase(Locale.ROOT).contains(dataToRemember.toLowerCase(Locale.ROOT)))
                            tutors.add(tutor);
                        break;
                    case DATES:
                        ConsultDatetime consultDatetime = new ConsultDatetime();
                        consultDatetime.setId(extractedObject.getLong("id"));
                        consultDatetime.setTutorId(extractedObject.getLong("tutor_id"));
                        consultDatetime.setDate(extractedObject.getString("day"));
                        consultDatetime.setTime(extractedObject.getString("time"));
                        consultDatetime.setRoom(extractedObject.getString("room"));
                        consultDatetime.setOrderedSpace(extractedObject.getInt("ordered_space"));
                        consultDatetime.setMaxSpace(extractedObject.getInt("max_space"));

                        if (toMonitor){
                            if (consultDatetime.getOrderedSpace() < consultDatetime.getMaxSpace() && !isFound)
                                isFound = true;
                        }

                        dates.add(consultDatetime);
                        break;
                    case APPOINTMENTS:
                        Appointment appointment = new Appointment();

                        appointment.setId(extractedObject.getLong("id"));
                        appointment.setName(extractedObject.getString("name"));
                        appointment.setSurname(extractedObject.getString("surname"));
                        appointment.setGroup(extractedObject.getString("group"));
                        appointment.setTutor(extractedObject.getString("tutor"));
                        appointment.setTutorId(extractedObject.getLong("tutor_id"));
                        appointment.setDatetime(extractedObject.getString("datetime"));
                        appointment.setConsultId(extractedObject.getLong("consult_id"));

                        AppointmentsListElement appointmentsListElement = new AppointmentsListElement(appointment, null);

                        appointments.add(appointmentsListElement);
                        break;
                }
            }

            if (itemsListSpinner != null){
                switch(type){
                    case GROUPS:
                        if (!groups.isEmpty()){
                            String[] mappedGroups = groups.stream().map((group) -> group.getName()).toArray(String[]::new);

                            ArrayAdapter<String> groupsListAdapter = new ArrayAdapter<>(context, R.layout.default_spinner_layout, mappedGroups);
                            groupsListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            groupsListAdapter.notifyDataSetChanged();

                            itemsListSpinner.setAdapter(groupsListAdapter);
                        }
                        else{
                            ArrayAdapter<String> groupsListAdapter = new ArrayAdapter<>(context, R.layout.default_spinner_layout, new String[]{"Информация не найдена!"});
                            groupsListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            groupsListAdapter.notifyDataSetChanged();

                            itemsListSpinner.setAdapter(groupsListAdapter);
                        }
                        break;
                    case TUTORS:
                        if (!tutors.isEmpty()){
                            TutorsListAdapter tutorsListAdapter = new TutorsListAdapter(context, R.layout.default_spinner_layout, tutors);
                            tutorsListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            tutorsListAdapter.notifyDataSetChanged();

                            itemsListSpinner.setAdapter(tutorsListAdapter);
                        }
                        else{
                            ArrayAdapter<String> tutorsListAdapter = new ArrayAdapter<>(context, R.layout.default_spinner_layout, new String[]{"Информация не найдена!"});
                            tutorsListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            tutorsListAdapter.notifyDataSetChanged();

                            itemsListSpinner.setAdapter(tutorsListAdapter);
                        }
                        break;
                    case DATES:
                        if (!dates.isEmpty()){
                            DatetimeListAdapter datetimeListAdapter = new DatetimeListAdapter(context, R.layout.default_spinner_layout, dates);
                            datetimeListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            datetimeListAdapter.notifyDataSetChanged();

                            itemsListSpinner.setAdapter(datetimeListAdapter);
                        }
                        else{
                            ArrayAdapter<String> datetimeListAdapter = new ArrayAdapter<>(context, R.layout.default_spinner_layout, new String[]{"Загрузка..."});
                            datetimeListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                            datetimeListAdapter.notifyDataSetChanged();

                            itemsListSpinner.setAdapter(datetimeListAdapter);
                        }
                        break;
                }

            }

            if (itemsListRecyclerView != null){
                AppointmentsListAdapter appointmentsListAdapter = new AppointmentsListAdapter(appointments);
                itemsListRecyclerView.setAdapter(appointmentsListAdapter);
            }

            if (toMonitor){
                if (isFound)
                    monitorMessage.setText("  ");
                else
                    monitorMessage.setText(" ");
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

    public void setDataToRemember(String dataToRemember) {
        this.dataToRemember = dataToRemember;
    }
}
