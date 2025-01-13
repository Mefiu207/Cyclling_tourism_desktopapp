package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "listy_hoteli")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokoje implements Models{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wycieczka")
    private Wycieczki wycieczka;

    @OneToMany(mappedBy = "pokoj")
    private List<ListyHoteli> listyHoteli;


    @OneToMany(mappedBy = "pokoj")
    private List<Klienci> klienci;

    private String typPokoju;

    @Override
    public String toString(){
        return "";
    }
}
