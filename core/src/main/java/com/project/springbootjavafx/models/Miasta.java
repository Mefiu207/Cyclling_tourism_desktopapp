package com.project.springbootjavafx.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Setter
@Getter
@Entity
public class Miasta implements Models{

    @Id
    private String miasto;

    // Powiazanie do hoteli
    @OneToMany(mappedBy = "miasto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotele> hotele;

    // Polaczenie z tabele miasta wycieczek
    @OneToMany(mappedBy = "miasta")
    private List<MiastaWycieczek> miastaWycieczek;

    public Miasta(){}

    public Miasta(final String miasto) {
        this.miasto = miasto;
    }

    @Override
    public String toString(){
        return miasto;
    }

}