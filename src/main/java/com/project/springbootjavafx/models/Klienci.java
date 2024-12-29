package com.project.springbootjavafx.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;



@Entity
public class Klienci {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imie;
    private String nazwisko;

    private String wycieczka;

    private Integer pokoj;

    private Boolean rower;
    private Boolean e_bike;
    private Boolean nocelg_przed;
    private Boolean nocleg_po;
    private Boolean hb;
}
