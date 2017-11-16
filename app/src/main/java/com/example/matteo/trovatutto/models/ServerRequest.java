package com.example.matteo.trovatutto.models;

public class ServerRequest {

    private String operation;
    private User user;
    private Segnalazione segnalazione;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSegnalazione(Segnalazione segnalazione) {
        this.segnalazione = segnalazione;
    }
}

