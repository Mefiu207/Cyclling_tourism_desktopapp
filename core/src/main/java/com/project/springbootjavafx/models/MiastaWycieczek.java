package com.project.springbootjavafx.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "miasta_wycieczek")
public class MiastaWycieczek implements Models{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // Typ wycieczki do jakiego noc sie odnosi
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typ_wycieczki")
    private TypyWycieczek typyWycieczek;

    // Miasto wycieczki gdzie jest ta noc
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "miasto")
    private Miasta miasta;

    @OneToMany(mappedBy = "miastoWycieczki")
    private List<ListyHoteli> listyHoteli;

    // Numer nocy
    private Integer nr_nocy;

    public MiastaWycieczek() {}


    @Override
    public String toString() {
        return "";
    }
}
