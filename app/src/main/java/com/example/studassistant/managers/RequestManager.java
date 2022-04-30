package com.example.studassistant.managers;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.studassistant.enums.ArrayType;

public class RequestManager {
    protected static final String URL = "https://stud-assistant-server.herokuapp.com/";
    protected Context context;
    protected ArrayType type;

    public RequestManager(Context context, ArrayType type){
        this.context = context;
        this.type = type;
    }

    public boolean checkConnection() {
        ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        boolean isConnectionFailed;
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

    void createRequest(){};
}
