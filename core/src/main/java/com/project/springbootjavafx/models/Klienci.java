package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Klienci implements Models{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imie;
    private String nazwisko;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wycieczka")
    private Wycieczki wycieczka;

    @Column(name = "typ_pokoju")
    private String typPokoju;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pokoj")
    private Pokoje pokoj;

    private Boolean ulga;
    private Boolean rower;

    @Column(name = "e_bike")
    private Boolean eBike;

    @Column(name = "nocleg_przed")
    private Boolean noclegPrzed;

    @Column(name = "nocleg_po")
    private Boolean noclegPo;

    private Boolean hb;

    @Override
    public String toString(){
        return "";
    }
}
