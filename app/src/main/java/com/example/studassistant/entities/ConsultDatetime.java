package com.example.studassistant.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConsultDatetime {
    private long id;
    private long tutorId;
    private String date;
    private String time;
    private String room;
    private int orderedSpace;
    private int maxSpace;

    public ConsultDatetime(){};

    public ConsultDatetime(ConsultDatetime consultDatetime){
        this.id = consultDatetime.id;
        this.tutorId = consultDatetime.tutorId;
        this.date = consultDatetime.date;
        this.time = consultDatetime.time;
        this.room = consultDatetime.room;
        this.orderedSpace = consultDatetime.orderedSpace;
        this.maxSpace = consultDatetime.maxSpace;
    }

    public ConsultDatetime(long id, long tutorId, String date, String time, String room, int orderedSpace, int maxSpace){
        this.id = id;
        this.tutorId = tutorId;
        this.date = date;
        this.time = time;
        this.room = room;
        this.orderedSpace = orderedSpace;
        this.maxSpace = maxSpace;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public long getTutorId() {
        return tutorId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setOrderedSpace(int orderedSpace) {
        this.orderedSpace = orderedSpace;
    }

    public int getOrderedSpace() {
        return orderedSpace;
    }

    public String getSpaceStat(){
        return String.format("%s/%s", orderedSpace, maxSpace);
    }

    public void setMaxSpace(int maxSpace) {
        this.maxSpace = maxSpace;
    }

    public int getMaxSpace() {
        return maxSpace;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE dd.MM.yyyy", new Locale("ru"));
        String consultDate = date;

        Calendar now = Calendar.getInstance();

        int actualDay = now.get(Calendar.DAY_OF_WEEK);
        int days = Integer.parseInt(consultDate) - actualDay;
        if (days <= 0)
            days += 7;

        now.add(Calendar.DAY_OF_YEAR, days);

        Date date = now.getTime();
        String formattedDate = dateFormatter.format(date);

        return String.format("%s %s %s", formattedDate, time, room);
    }
}
