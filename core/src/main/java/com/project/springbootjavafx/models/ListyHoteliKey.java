package com.project.springbootjavafx.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class ListyHoteliKey implements Serializable {

    @Column(name =  "pokoj")
    Integer pokoj;

    @Column (name = "miasto_wycieczki")
    Integer miastoWycieczki;

    @Column (name = "hotel")
    String hotelKod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListyHoteliKey that = (ListyHoteliKey) o;

        return Objects.equals(pokoj, that.pokoj) &&
                Objects.equals(miastoWycieczki, that.miastoWycieczki) &&
                Objects.equals(hotelKod, that.hotelKod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokoj, miastoWycieczki, hotelKod);
    }


}
