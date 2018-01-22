package com.example.matteo.trovatutto.models;


public class Segnalazione {

    private String id;
    private String titolo;
    private String sottotitolo;
    private String categoria;
    private String descrizione;
    private String indirizzo;
    private String foto;
    private String autore;

    public Segnalazione() {}

    public Segnalazione(String titolo,String sottotitolo, String categoria, String descrizione, String indirizzo, String foto, String autore ) {
        this.titolo = titolo;
        this.sottotitolo = sottotitolo;
        this.categoria = categoria;
        this.descrizione= descrizione;
        this.indirizzo = indirizzo;
        this.foto = foto;
        this.autore = autore;
    }

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id=id;
    }


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
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }
}
