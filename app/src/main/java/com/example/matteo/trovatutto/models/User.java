package com.example.matteo.trovatutto.models;

public class User {

    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
    private String datadinascita;
    private String ntel;
    private String descrizione;

    private String password;
    private String old_password;
    private String new_password;
    private String code;

    public User() {}

    public String getName() {return nome;}

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getDatadinascita() {return datadinascita;}

    public String getNtel() {
        return ntel;
    }

    public String getDescrizione() {return descrizione;}



    public void setName(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setDatadinascita(String datadinascita) { this.datadinascita = datadinascita;}

    public void setNtel(String ntel) {
        this.ntel = ntel;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }



    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
