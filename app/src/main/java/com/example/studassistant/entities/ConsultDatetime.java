package com.example.studassistant.entities;

public class ConsultDatetime {
    private long id;
    private long tutorId;
    String date;
    String time;
    String room;

    public ConsultDatetime(){};

    public ConsultDatetime(long id, long tutorId, String date, String time, String room){
        this.id = id;
        this.tutorId = tutorId;
        this.date = date;
        this.time = time;
        this.room = room;
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

    @Override
    public String toString() {
        return String.format("%s %s %s", date, time, room);
    }
}
