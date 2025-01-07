package com.project.springbootjavafx.models;


import jakarta.persistence.*;

@Entity
public class Hotele implements Models {

    @Id
    private String kod;

    private String nazwa;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "miasto", referencedColumnName = "miasto")
    private Miasta miasto;


    private String adres;
    private String mail;
    private String nr_tel;


    public Hotele() {}


    public Hotele(String kod, String nazwa, Miasta miasto, String adres, String mail, String nr_tel) {
        this.kod = kod;
        this.nazwa = nazwa;
        this.miasto = miasto;
        this.adres = adres;
        this.mail = mail;
        this.nr_tel = nr_tel;
    }


    public String toString() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public void setMiasto(Miasta miasto) {
        this.miasto = miasto;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setMail(String email) {
        this.mail = email;
    }

    public void setNr_tel(String telefon) {
        this.nr_tel = telefon;
    }

    public String getKod() {
        return this.kod;
    }

    public String getNazwa() {
        return this.nazwa;
    }

    public Miasta getMiasto() {
        return this.miasto;
    }

    public String getAdres() {
        return this.adres;
    }

    public String getMail() {
        return this.mail;
    }

    public String getNr_tel() {
        return this.nr_tel;
    }
}
