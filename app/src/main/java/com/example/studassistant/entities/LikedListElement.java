package com.example.studassistant.entities;

import android.widget.CheckBox;

public class LikedListElement {
    private int id;
    private String personal;
    private CheckBox checkToExcludeButton;

    public LikedListElement(){}

    public LikedListElement(int id, String personal, CheckBox checkToExcludeButton){
        this.id = id;
        this.personal = personal;
        this.checkToExcludeButton = checkToExcludeButton;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getPersonal() {
        return personal;
    }

    public void setCheckToExcludeButton(CheckBox checkToExcludeButton) {
        this.checkToExcludeButton = checkToExcludeButton;
    }

    public CheckBox getCheckToExcludeButton() {
        return checkToExcludeButton;
    }
}
