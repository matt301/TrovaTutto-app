package com.example.matteo.trovatutto.models;


public class Segnalazione {

    private String titolo;
    private String sottotitolo;
    private String Categoria;
    private String Descrizone;
    private String Indirizzo;
    private String Foto;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getSottotitolo() {
        return sottotitolo;
    }

    public void setSottotitolo(String sottotitolo) {
        this.sottotitolo = sottotitolo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getDescrizone() {
        return Descrizone;
    }

    public void setDescrizone(String descrizone) {
        Descrizone = descrizone;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }
}
