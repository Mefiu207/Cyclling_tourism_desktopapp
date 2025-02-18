package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * The {@code Klienci} class represents a client in the system.
 *
 * <p>
 * This entity holds personal and booking-related information for a client,
 * such as first name, last name, associated trip, room type, and additional services selected.
 * It also stores the amount due for the client's trip.
 * </p>
 *
 * <p>
 * The class uses JPA annotations to map to a database table and Lombok annotations to generate
 * getters and setters automatically.
 * </p>
 *
 * @see Wycieczki
 * @see Pokoje
 */
@Getter
@Setter
@Entity
public class Klienci implements Models {

    /**
     * The unique identifier for the client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The first name of the client.
     */
    private String imie;

    /**
     * The last name of the client.
     */
    private String nazwisko;

    /**
     * The trip associated with the client.
     *
     * <p>
     * This represents a many-to-one relationship with the {@link Wycieczki} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wycieczka")
    private Wycieczki wycieczka;

    /**
     * The type of room assigned to the client.
     */
    @Column(name = "typ_pokoju")
    private String typPokoju;

    /**
     * The room assigned to the client.
     *
     * <p>
     * This represents a many-to-one relationship with the {@link Pokoje} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pokoj")
    private Pokoje pokoj;

    /**
     * Indicates whether the client receives a discount.
     */
    private Boolean ulga;

    /**
     * Indicates whether the client has opted for a bicycle service.
     */
    private Boolean rower;

    /**
     * Indicates whether the client has opted for an E-Bike service.
     */
    @Column(name = "e_bike")
    private Boolean eBike;

    /**
     * Indicates whether the client has opted for accommodation before the trip.
     */
    @Column(name = "nocleg_przed")
    private Boolean noclegPrzed;

    /**
     * Indicates whether the client has opted for accommodation after the trip.
     */
    @Column(name = "nocleg_po")
    private Boolean noclegPo;

    /**
     * Indicates whether the client has opted for half board (HB).
     */
    private Boolean hb;

    /**
     * The amount to be paid by the client.
     */
    @Column(name = "do_zaplaty")
    private BigDecimal doZaplaty;

    /**
     * Returns a string representation of the client.
     *
     * <p>
     * This implementation returns an empty string.
     * </p>
     *
     * @return an empty string
     */
    @Override
    public String toString() {
        return "";
    }
}
