package com.project.springbootjavafx.models;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Immutable
@Table(name = "v_lista_nocy_hoteli")
public class ListaNocyHoteli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false)
    private Integer id;

    @Column(name = "pokoj_id")
    private Integer pokojId;

    @Column(name = "noc")
    private Integer noc;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "miasto")
    private String miasto;

    @Column(name = "hotel")
    private String hotel;
}