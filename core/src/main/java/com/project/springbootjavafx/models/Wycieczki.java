package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
public class Wycieczki implements Models {

    @Id
    private String wycieczka;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typ_wycieczki")
    private TypyWycieczek typWycieczki;

    @OneToMany(mappedBy = "wycieczka")
    private List<Pokoje> pokoje;

    private LocalDate poczatek;

    private LocalDate koniec;

    private Integer ilUczestinkow;

    private BigDecimal wplyw;

    @OneToMany(mappedBy = "wycieczka")
    private List<Klienci> klienci;

    public Wycieczki(String wycieczka, TypyWycieczek typWycieczki, LocalDate poczatek, LocalDate koniec, Integer ilUczestinkow, BigDecimal wplyw) {
        this.wycieczka = wycieczka;
        this.typWycieczki = typWycieczki;
        this.poczatek = poczatek;
        this.koniec = koniec;
        this.ilUczestinkow = ilUczestinkow;
        this.wplyw = wplyw;
    }

    public Wycieczki() {
    }


    @Override
    public String toString() {
        return wycieczka;
    }
//
//    public String getWycieczka() {
//        return this.wycieczka;
//    }
//
//    public TypyWycieczek getTypWycieczki() {
//        return this.typWycieczki;
//    }
//
//    public LocalDate getPoczatek() {
//        return this.poczatek;
//    }
//
//    public LocalDate getKoniec() {
//        return this.koniec;
//    }
//
//    public Integer getIlUczestinkow() {
//        return this.ilUczestinkow;
//    }
//
//    public BigDecimal getWplyw() {
//        return this.wplyw;
//    }
//
//    public void setWycieczka(String wycieczka) {
//        this.wycieczka = wycieczka;
//    }
//
//    public void setTypWycieczki(TypyWycieczek typWycieczki) {
//        this.typWycieczki = typWycieczki;
//    }
//
//    public void setPoczatek(LocalDate poczatek) {
//        this.poczatek = poczatek;
//    }
//
//    public void setKoniec(LocalDate koniec) {
//        this.koniec = koniec;
//    }
//
//    public void setIlUczestinkow(Integer ilUczestinkow) {
//        this.ilUczestinkow = ilUczestinkow;
//    }
//
//    public void setWplyw(BigDecimal wplyw) {
//        this.wplyw = wplyw;
//    }
}
