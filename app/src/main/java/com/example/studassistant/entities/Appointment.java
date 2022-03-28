package com.example.studassistant.entities;

public class Appointment {
    private long id;
    private String name;
    private String surname;
    private String group;
    private String tutor;
    private long tutorId;
    private String datetime;
    private long consultId;
    private String userCode;

    public Appointment(){}

    public Appointment(Appointment appointment){
        this.id = appointment.id;
        this.name = appointment.name;
        this.surname = appointment.surname;
        this.group = appointment.group;
        this.tutor = appointment.tutor;
        this.tutorId = appointment.tutorId;
        this.datetime = appointment.datetime;
        this.consultId = appointment.consultId;
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

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public long getTutorId() {
        return tutorId;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setConsultId(long consultId) {
        this.consultId = consultId;
    }

    public long getConsultId() {
        return consultId;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserCode() {
        return userCode;
    }
}
