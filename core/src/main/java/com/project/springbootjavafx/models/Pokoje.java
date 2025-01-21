package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
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

    @Column(name = "typ_pokoju")
    private String typPokoju;

    @Column(name = "il_klientow")
    private Integer ilKlientow;

    @Column(name = "il_miejsc")
    private Integer ilMiejsc;

    @Override
    public String toString(){
        return typPokoju;
    }
}
