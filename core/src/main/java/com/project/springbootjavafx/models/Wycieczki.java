package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
public class Wycieczki implements Models {

    @Id
    private String wycieczka;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typ_wycieczki")
    private TypyWycieczek typWycieczki;

    @OneToMany(mappedBy = "wycieczka", cascade = CascadeType.ALL)
    private List<Pokoje> pokoje;

    private LocalDate poczatek;

    private LocalDate koniec;

    @Column(name = "il_uczestnikow")
    private Integer ilUczestinkow;

    private BigDecimal wplyw;

    @OneToMany(mappedBy = "wycieczka")
    private List<Klienci> klienci;

    @Override
    public String toString() {
        return wycieczka;
    }

    public boolean equals(Wycieczki other){
        return other.wycieczka.equals(this.wycieczka);
    }
}
