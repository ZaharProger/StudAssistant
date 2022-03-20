package com.example.studassistant.entities;

public class Appointment {
    private long id;
    private String name;
    private String surname;
    private String group;
    private String tutor;
    private String datetime;
    private String userCode;

    public Appointment(){}

    public Appointment(Appointment appointment){
        this.id = appointment.id;
        this.name = appointment.name;
        this.surname = appointment.surname;
        this.group = appointment.group;
        this.tutor = appointment.tutor;
        this.datetime = appointment.datetime;
        this.userCode = appointment.userCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
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

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserCode() {
        return userCode;
    }
}
