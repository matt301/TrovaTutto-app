package com.example.matteo.trovatutto.models;


public class ServerResponse {

    private String result;
    private String message;
    private User user;
    private Segnalazione segnalazione;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Segnalazione getSegnalazione() {
        return segnalazione;
    }
}
