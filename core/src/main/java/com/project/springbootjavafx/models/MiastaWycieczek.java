package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * The {@code MiastaWycieczek} class represents an association between a trip type and a city for a specific night of a trip.
 *
 * <p>
 * This entity maps to the "miasta_wycieczek" table and holds information about which city is assigned to a particular night
 * of a trip. It is linked to a {@link TypyWycieczek} entity representing the trip type and a {@link Miasta} entity representing
 * the city. Additionally, it has a one-to-many relationship with {@link ListyHoteli}, which are the hotel lists for that night.
 * </p>
 *
 * <p>
 * Lombok annotations (@Getter and @Setter) are used to automatically generate getter and setter methods.
 * </p>
 *
 * @see TypyWycieczek
 * @see Miasta
 * @see ListyHoteli
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "miasta_wycieczek")
public class MiastaWycieczek implements Models {

    /**
     * The unique identifier for the MiastaWycieczek record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The trip type to which this night belongs.
     *
     * <p>
     * This is a many-to-one relationship with the {@link TypyWycieczek} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typ_wycieczki")
    private TypyWycieczek typyWycieczek;

    /**
     * The city associated with this night of the trip.
     *
     * <p>
     * This is a many-to-one relationship with the {@link Miasta} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "miasto")
    private Miasta miasta;

    /**
     * The list of hotel lists associated with this trip night.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link ListyHoteli} entity.
     * </p>
     */
    @OneToMany(mappedBy = "miastoWycieczek")
    private List<ListyHoteli> listyHoteli;

    /**
     * The number of the night in the trip (e.g., 1 for the first night, 2 for the second, etc.).
     */
    @Column(name = "nr_nocy")
    private Integer numerNocy;

    /**
     * Returns a string representation of this MiastaWycieczek instance.
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
