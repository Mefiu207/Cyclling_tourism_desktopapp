package com.project.springbootjavafx.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;

import java.util.List;


@Entity
public class Miasta implements Models{

    @Id
    private String miasto;

    @OneToMany(mappedBy = "miasto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotele> hotele;


    public Miasta(){}

    public Miasta(final String miasto) {
        this.miasto = miasto;
    }

    @Override
    public String toString(){
        return miasto;
    }



    public String getMiasto(){
        return miasto;
    }

    public void setMiasto(String miasto){this.miasto = miasto;}
}