package com.example.studassistant.constants;

import com.example.studassistant.entities.ConsultDatetime;

import java.util.ArrayList;

public class PinnedDataStorage {
    public static ArrayList<ConsultDatetime> pinnedData = new ArrayList<>();

    public static ConsultDatetime getDataById(long id){
        ConsultDatetime consultDatetime = new ConsultDatetime();

        for (ConsultDatetime data : pinnedData){
            if (data.getId() == id)
                consultDatetime =  data;
        }

        return  consultDatetime;
    }
}
