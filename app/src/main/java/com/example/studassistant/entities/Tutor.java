package com.example.studassistant.entities;

import androidx.annotation.NonNull;

public class Tutor {
    private long id;
    private String name;
    private String surname;
    private String patronymic;

    public Tutor(){ }

    public Tutor(long id, String name, String surname, String patronymic){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
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

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @NonNull
    @Override
    public String toString(){
        return String.format("%s %s.%s.", surname, name.charAt(0), patronymic.charAt(0));
    }
}
