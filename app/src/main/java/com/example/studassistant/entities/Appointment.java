package com.example.studassistant.entities;

public class Appointment {
    private int id;
    private String name;
    private String surname;
    private String group;
    private String tutor;
    private String datetime;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public String getTutor() {
        return tutor;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }
}
