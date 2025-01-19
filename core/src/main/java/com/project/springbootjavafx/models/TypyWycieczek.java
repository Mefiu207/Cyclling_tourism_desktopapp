package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "typy_wycieczek")
public class TypyWycieczek implements Models {

    @Id
    private String typ;

    private Integer liczba_nocy;

    // Ceny dla danego  typu wycieczki
    @OneToOne(mappedBy = "typ_wycieczki", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ceny ceny;

    // Miasta danej wycieczki
    @OneToMany(mappedBy = "typyWycieczek")
    private List<MiastaWycieczek> miastaWycieczek;

    // Wycieczki dla danego typu
    @OneToMany(mappedBy = "typWycieczki")
    private List<Wycieczki> wycieczki;

    @Override
    public String toString() {
        return typ;
    }

}
