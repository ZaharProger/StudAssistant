package com.example.studassistant.entities;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class LikedListElement {
    private int id;
    private String personal;
    private CheckBox checkToExcludeButton;
    private ImageButton makeAppointmentButton;

    public LikedListElement(){}

    public LikedListElement(int id, String personal, CheckBox checkToExcludeButton, ImageButton makeAppointmentButton){
        this.id = id;
        this.personal = personal;
        this.checkToExcludeButton = checkToExcludeButton;
        this.makeAppointmentButton = makeAppointmentButton;
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

    public void setMakeAppointmentButton(ImageButton makeAppointmentButton) {
        this.makeAppointmentButton = makeAppointmentButton;
    }

    public ImageButton getMakeAppointmentButton() {
        return makeAppointmentButton;
    }
}
