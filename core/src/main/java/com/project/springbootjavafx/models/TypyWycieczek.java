package com.project.springbootjavafx.models;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "typy_wycieczek")
public class TypyWycieczek implements Models {

    @Id
    private String typ;

    private Integer liczba_nocy;

    // Ceny dla danego  typu wycieczki
    @OneToOne(mappedBy = "typ_wycieczki")
    private Ceny ceny;

    // Miasta danej wycieczki
    @OneToMany(mappedBy = "typyWycieczek")
    private List<MiastaWycieczek> miastaWycieczek;

    // Wycieczki dla danego typu
    @OneToMany(mappedBy = "typWycieczki")
    private List<Wycieczki> wycieczki;



    public TypyWycieczek(String typ, Integer liczba_nocy, Ceny ceny) {
        this.typ = typ;
        this.liczba_nocy = liczba_nocy;
        this.ceny = ceny;
    }

    public TypyWycieczek() {
    }

    @Override
    public String toString() {
        return typ;
    }


    public String getTyp() { return this.typ; }

    public Integer getLiczba_nocy() {
        return this.liczba_nocy;
    }

    public Ceny getCeny() {
        return this.ceny;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public void setLiczba_nocy(Integer liczba_nocy) {
        this.liczba_nocy = liczba_nocy;
    }

    public void setCeny(Ceny ceny) {
        this.ceny = ceny;
    }
}
