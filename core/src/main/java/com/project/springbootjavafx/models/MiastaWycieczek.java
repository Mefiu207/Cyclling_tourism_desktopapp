package com.project.springbootjavafx.models;


import jakarta.persistence.*;

@Entity
@Table(name = "miasta_wycieczek")
public class MiastaWycieczek implements Models{

    @Id
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    private TypyWycieczek typyWycieczek;

    @ManyToMany(fetch = FetchType.EAGER)
    private Miasta miasta;

    private Integer nr_nocy;

    @Override
    public String toString() {
        return "";
    }
}
