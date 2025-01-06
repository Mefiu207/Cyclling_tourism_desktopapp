package com.project.springbootjavafx.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Miasta implements Models{

    @Id
    private String miasto;

    public Miasta(){}

    public Miasta(final String miasto) {
        this.miasto = miasto;
    }

    public String toString(){
        return miasto;
    }

    public String getMiasto(){
        return miasto;
    }

    public void setMiasto(String miasto){this.miasto = miasto;}
}