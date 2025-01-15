package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Ceny implements Models {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "typ_wycieczki", referencedColumnName = "typ")
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

}
