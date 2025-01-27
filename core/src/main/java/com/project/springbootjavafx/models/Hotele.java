package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
public class Hotele implements Models {

    @Id
    @Column(name = "kod")
    private String kod;

    private String nazwa;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "miasto")
    private Miasta miasto;

    @OneToMany(mappedBy = "hotel")
    private List<ListyHoteli> listyHoteli;


    private String adres;
    private String mail;
    private String nr_tel;


    public Hotele() {}


    public Hotele(String kod, String nazwa, Miasta miasto, String adres, String mail, String nr_tel) {
        this.kod = kod;
        this.nazwa = nazwa;
        this.miasto = miasto;
        this.adres = adres;
        this.mail = mail;
        this.nr_tel = nr_tel;
    }


    public String toString() {
        return kod;
    }
}
