package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "listy_hoteli")
public class ListyHoteli implements Models{

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ListyHoteliKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("pokoj")
    @JoinColumn(name = "pokoj")
    Pokoje pokoj;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("miastoWycieczki")
    @JoinColumn(name = "miasto_wycieczki")
    MiastaWycieczek miastoWycieczki;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("hotelKod")
    @JoinColumn(name = "hotel")
    Hotele hotel;
}
