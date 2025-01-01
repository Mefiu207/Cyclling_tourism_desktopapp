package com.project.springbootjavafx.models;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
@Setter @Getter
public class Miasta {

    @Id
    private String miasto;

    public String toString(){
        return miasto;
    }
}
