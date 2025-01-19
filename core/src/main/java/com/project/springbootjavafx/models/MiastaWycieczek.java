package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "miasta_wycieczek")
public class MiastaWycieczek implements Models{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "nr_nocy")
    private Integer numerNocy;

    public MiastaWycieczek() {}


    @Override
    public String toString() {
        return "";
    }
}
