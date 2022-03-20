package com.example.studassistant.entities;

import android.widget.CheckBox;

public class AppointmentsListElement {
    private Appointment appointment;
    private CheckBox checkToRemoveButton;

    public AppointmentsListElement(Appointment appointment, CheckBox checkToRemoveButton){
        this.appointment = appointment;
        this.checkToRemoveButton = checkToRemoveButton;
    }

    public AppointmentsListElement(){
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setCheckToRemoveButton(CheckBox checkToRemoveButton) {
        this.checkToRemoveButton = checkToRemoveButton;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public CheckBox getCheckToRemoveButton() {
        return checkToRemoveButton;
    }

}
