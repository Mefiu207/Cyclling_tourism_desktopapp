package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Ceny implements Models {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private TypyWycieczek typ_wycieczki;

    private BigDecimal pok_1;
    private BigDecimal pok_2;
    private BigDecimal pok_3;
    private BigDecimal pok_4;

    private Integer ulga_dziecko;

    private BigDecimal rower;
    private BigDecimal e_bike;

    private BigDecimal dodatkowa_noc;
    private BigDecimal hb;


    public Ceny(Integer id, TypyWycieczek typ_wycieczki, BigDecimal pok_1, BigDecimal pok_2, BigDecimal pok_3, BigDecimal pok_4, Integer ulga_dziecko, BigDecimal rower, BigDecimal e_bike, BigDecimal dodatkowa_noc, BigDecimal hb) {
        this.id = id;
        this.typ_wycieczki = typ_wycieczki;
        this.pok_1 = pok_1;
        this.pok_2 = pok_2;
        this.pok_3 = pok_3;
        this.pok_4 = pok_4;
        this.ulga_dziecko = ulga_dziecko;
        this.rower = rower;
        this.e_bike = e_bike;
        this.dodatkowa_noc = dodatkowa_noc;
        this.hb = hb;
    }

    public Ceny() {
    }

//    public Integer getId() {
//        return this.id;
//    }
//
//    public TypyWycieczek getTyp_wycieczki() {
//        return this.typ_wycieczki;
//    }
//
//    public BigDecimal getPok_1() {
//        return this.pok_1;
//    }
//
//    public BigDecimal getPok_2() {
//        return this.pok_2;
//    }
//
//    public BigDecimal getPok_3() {
//        return this.pok_3;
//    }
//
//    public BigDecimal getPok_4() {
//        return this.pok_4;
//    }
//
//    public Integer getUlga_dziecko() {
//        return this.ulga_dziecko;
//    }
//
//    public BigDecimal getRower() {
//        return this.rower;
//    }
//
//    public BigDecimal getE_bike() {
//        return this.e_bike;
//    }
//
//    public BigDecimal getDodatkowa_noc() {
//        return this.dodatkowa_noc;
//    }
//
//    public BigDecimal getHb() {
//        return this.hb;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setTyp_wycieczki(TypyWycieczek typ_wycieczki) {
//        this.typ_wycieczki = typ_wycieczki;
//    }
//
//    public void setPok_1(BigDecimal pok_1) {
//        this.pok_1 = pok_1;
//    }
//
//    public void setPok_2(BigDecimal pok_2) {
//        this.pok_2 = pok_2;
//    }
//
//    public void setPok_3(BigDecimal pok_3) {
//        this.pok_3 = pok_3;
//    }
//
//    public void setPok_4(BigDecimal pok_4) {
//        this.pok_4 = pok_4;
//    }
//
//    public void setUlga_dziecko(Integer ulga_dziecko) {
//        this.ulga_dziecko = ulga_dziecko;
//    }
//
//    public void setRower(BigDecimal rower) {
//        this.rower = rower;
//    }
//
//    public void setE_bike(BigDecimal e_bike) {
//        this.e_bike = e_bike;
//    }
//
//    public void setDodatkowa_noc(BigDecimal dodatkowa_noc) {
//        this.dodatkowa_noc = dodatkowa_noc;
//    }
//
//    public void setHb(BigDecimal hb) {
//        this.hb = hb;
//    }
}
