package com.example.matteo.trovatutto.models;

public class User {

    private String nome;
    private String email;

    private String password;
    private String old_password;
    private String new_password;
    private String code;


    public String getName() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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
