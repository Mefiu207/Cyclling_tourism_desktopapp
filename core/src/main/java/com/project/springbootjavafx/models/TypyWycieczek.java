package com.project.springbootjavafx.models;


import jakarta.persistence.*;

@Entity
@Table(name = "typy_wycieczek")
public class TypyWycieczek implements Models {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String typ;

    private Integer liczba_nocy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Ceny ceny;


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


    public String getTyp() {
        return this.typ;
    }

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
