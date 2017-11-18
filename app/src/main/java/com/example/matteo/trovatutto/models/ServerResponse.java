package com.example.matteo.trovatutto.models;


import java.util.ArrayList;

public class ServerResponse {

    private String result;
    private String message;
    private User user;
    private ArrayList<Segnalazione> segnalazioni;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Segnalazione> getSegnalazioni() {
        return segnalazioni;
    }
}
