package com.example.matteo.trovatutto.models;


import java.util.List;

public class ServerResponse {

    private String result;
    private String message;
    private User user;
    private List<Segnalazione> segnalazioni;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public List<Segnalazione> getSegnalazioni() {
        return segnalazioni;
    }
}
