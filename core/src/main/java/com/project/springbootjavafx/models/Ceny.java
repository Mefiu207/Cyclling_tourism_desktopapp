package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * The {@code Ceny} class represents the pricing information for a specific trip type.
 *
 * <p>
 * This entity holds the prices for different room categories (pok_1, pok_2, pok_3, pok_4), a discount percentage
 * for children (ulga_dziecko), and prices for additional services such as bicycle, E-Bike, extra night (dodatkowa_noc),
 * and half board (hb). The pricing information is associated with a particular trip type via a one-to-one relationship.
 * </p>
 *
 * <p>
 * The class uses JPA annotations to map to a database table, and Lombok annotations to generate boilerplate code
 * such as constructors, getters, and setters.
 * </p>
 *
 * @see TypyWycieczek
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Ceny implements Models {

    /**
     * The unique identifier for the pricing record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The trip type associated with these prices.
     *
     * <p>
     * This is defined as a one-to-one relationship with the {@link TypyWycieczek} entity.
     * The join column is "typ_wycieczki" which references the "typ" column in the {@code TypyWycieczek} table.
     * </p>
     */
    @OneToOne
    @JoinColumn(name = "typ_wycieczki", referencedColumnName = "typ")
    private TypyWycieczek typ_wycieczki;

    /**
     * The price for room category 1.
     */
    private BigDecimal pok_1;

    /**
     * The price for room category 2.
     */
    private BigDecimal pok_2;

    /**
     * The price for room category 3.
     */
    private BigDecimal pok_3;

    /**
     * The price for room category 4.
     */
    private BigDecimal pok_4;

    /**
     * The discount percentage for children.
     */
    private Integer ulga_dziecko;

    /**
     * The price for a bicycle service.
     */
    private BigDecimal rower;

    /**
     * The price for an E-Bike service.
     */
    private BigDecimal e_bike;

    /**
     * The price for an extra night.
     */
    private BigDecimal dodatkowa_noc;

    /**
     * The price for half board (HB).
     */
    private BigDecimal hb;
}
