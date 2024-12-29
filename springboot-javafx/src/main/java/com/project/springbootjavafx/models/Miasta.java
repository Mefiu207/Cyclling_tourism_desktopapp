package com.project.springbootjavafx.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;
import lombok.Getter;

@Entity
@Setter @Getter
public class Miasta {

    @Id
    private String misato;

}
