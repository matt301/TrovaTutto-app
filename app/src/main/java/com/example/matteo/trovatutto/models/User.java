package com.example.matteo.trovatutto.models;

public class User {

    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
    private String data;
    private String ntel;
    private String bio;

    private String password;
    private String old_password;
    private String new_password;
    private String code;


    public String getName() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getData() {return data;}

    public String getNtel() {
        return ntel;
    }

    public String getBio() {return bio;}



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

    public void setData(String data) { this.data = data;}

    public void setNtel(String ntel) {
        this.ntel = ntel;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    /*
    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setCode(String code) {
        this.code = code;
    }
*/
}
