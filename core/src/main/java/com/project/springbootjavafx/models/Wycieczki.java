package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * The {@code Wycieczki} class represents a trip in the system.
 *
 * <p>
 * This entity holds information about a trip, including its unique name, associated trip type,
 * dates (start and end), number of participants, and revenue (wplyw). It also maintains relationships
 * with related entities such as rooms ({@link Pokoje}) and clients ({@link Klienci}).
 * </p>
 *
 * <p>
 * The trip type is represented by the {@link TypyWycieczek} entity, which categorizes trips and determines their duration.
 * The list of rooms associated with the trip is managed via a one-to-many relationship with {@link Pokoje}.
 * Similarly, the clients participating in the trip are managed via a one-to-many relationship with {@link Klienci}.
 * </p>
 *
 * <p>
 * Lombok annotations (@AllArgsConstructor, @NoArgsConstructor, @Getter, @Setter) are used to automatically generate
 * constructors, getters, and setters.
 * </p>
 *
 * @see TypyWycieczek
 * @see Pokoje
 * @see Klienci
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
public class Wycieczki implements Models {

    /**
     * The unique identifier (name) of the trip.
     */
    @Id
    private String wycieczka;

    /**
     * The trip type associated with this trip.
     *
     * <p>
     * This is a many-to-one relationship with the {@link TypyWycieczek} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typ_wycieczki")
    private TypyWycieczek typWycieczki;

    /**
     * The list of rooms associated with this trip.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link Pokoje} entity. Cascade operations
     * are enabled to propagate changes from the trip to its rooms.
     * </p>
     */
    @OneToMany(mappedBy = "wycieczka", cascade = CascadeType.ALL)
    private List<Pokoje> pokoje;

    /**
     * The start date of the trip.
     */
    private LocalDate poczatek;

    /**
     * The end date of the trip.
     */
    private LocalDate koniec;

    /**
     * The number of participants in the trip.
     *
     * <p>
     * Mapped to the column "il_uczestnikow" in the database.
     * </p>
     */
    @Column(name = "il_uczestnikow")
    private Integer ilUczestinkow;

    /**
     * The revenue or payment amount associated with the trip.
     */
    private BigDecimal wplyw;

    /**
     * The list of clients participating in the trip.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link Klienci} entity.
     * </p>
     */
    @OneToMany(mappedBy = "wycieczka")
    private List<Klienci> klienci;

    /**
     * Returns a string representation of the trip.
     *
     * <p>
     * This implementation returns the trip name.
     * </p>
     *
     * @return the trip name as a string
     */
    @Override
    public String toString() {
        return wycieczka;
    }

    /**
     * Compares this trip with the specified trip for equality.
     *
     * <p>
     * Two trips are considered equal if their unique trip names are equal.
     * </p>
     *
     * @param other the trip to be compared for equality with this trip
     * @return {@code true} if the specified trip is equal to this trip; {@code false} otherwise
     */
    public boolean equals(Wycieczki other) {
        return other.wycieczka.equals(this.wycieczka);
    }
}
