package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * The {@code TypyWycieczek} class represents a type of trip in the system.
 *
 * <p>
 * This entity contains information about a particular trip type, including its identifier (typ) and the number of nights (liczba_nocy)
 * associated with trips of this type. It also defines relationships with pricing information, the cities for each trip night,
 * and the trips themselves.
 * </p>
 *
 * <p>
 * The pricing for the trip type is stored in a {@link Ceny} object via a one-to-one relationship.
 * The cities associated with the trip nights are represented by a list of {@link MiastaWycieczek} objects (one per night),
 * and the trips of this type are stored as a list of {@link Wycieczki} objects.
 * </p>
 *
 * <p>
 * Lombok annotations are used to generate constructors, getters, and setters automatically.
 * </p>
 *
 * @see Ceny
 * @see MiastaWycieczek
 * @see Wycieczki
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "typy_wycieczek")
public class TypyWycieczek implements Models {

    /**
     * The unique identifier for the trip type.
     */
    @Id
    private String typ;

    /**
     * The number of nights for trips of this type.
     */
    private Integer liczba_nocy;

    /**
     * The pricing information associated with this trip type.
     *
     * <p>
     * This is a one-to-one relationship with the {@link Ceny} entity. Cascade operations and orphan removal
     * are enabled to manage the pricing record's lifecycle.
     * </p>
     */
    @OneToOne(mappedBy = "typ_wycieczki", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ceny ceny;

    /**
     * The list of trip night city assignments for this trip type.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link MiastaWycieczek} entity.
     * </p>
     */
    @OneToMany(mappedBy = "typyWycieczek", cascade = CascadeType.ALL)
    private List<MiastaWycieczek> miastaWycieczek;

    /**
     * The list of trips that are of this trip type.
     *
     * <p>
     * This is a one-to-many relationship with the {@link Wycieczki} entity.
     * </p>
     */
    @OneToMany(mappedBy = "typWycieczki", cascade = CascadeType.ALL)
    private List<Wycieczki> wycieczki;

    /**
     * Returns a string representation of the trip type.
     *
     * <p>
     * This implementation returns the unique identifier of the trip type.
     * </p>
     *
     * @return the trip type as a string
     */
    @Override
    public String toString() {
        return typ;
    }
}
