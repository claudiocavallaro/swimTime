package com.time.swimtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("anno")
    private String anno;

    @JsonProperty("societa")
    private String societa;

    @JsonProperty("sesso")
    private String sesso;

    @JsonProperty("codice")
    private String codice;

    public User(){}

    public User(String nome, String anno, String societa, String sesso, String codice) {
        this.nome = nome;
        this.anno = anno;
        this.societa = societa;
        this.sesso = sesso;
        this.codice = codice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getSocieta() {
        return societa;
    }

    public void setSocieta(String societa) {
        this.societa = societa;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", anno='" + anno + '\'' +
                ", societa='" + societa + '\'' +
                ", sesso='" + sesso + '\'' +
                ", codice='" + codice + '\'' +
                '}';
    }
}
