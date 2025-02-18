package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * The {@code Miasta} class represents a city entity in the system.
 *
 * <p>
 * This entity holds information about a city and its relationships to hotels and trip night cities.
 * It is mapped to a database table and participates in one-to-many relationships with both the {@link Hotele}
 * and {@link MiastaWycieczek} entities.
 * </p>
 *
 * <p>
 * The {@code hotele} field represents the list of hotels located in this city. The relationship is
 * configured with cascade and orphan removal options to manage the lifecycle of associated hotels.
 * </p>
 *
 * <p>
 * The {@code miastaWycieczek} field represents the association of this city with trip night city entries.
 * </p>
 *
 * <p>
 * Lombok annotations (@Getter and @Setter) are used to automatically generate getter and setter methods.
 * </p>
 *
 * @see Hotele
 * @see MiastaWycieczek
 */
@Getter
@Setter
@Entity
public class Miasta implements Models {

    /**
     * The unique identifier for the city.
     */
    @Id
    private String miasto;

    /**
     * The list of hotels located in this city.
     *
     * <p>
     * This is a one-to-many relationship mapped by the "miasto" field in the {@link Hotele} entity.
     * Cascade operations and orphan removal are enabled to manage the hotel entities' lifecycle.
     * </p>
     */
    @OneToMany(mappedBy = "miasto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotele> hotele;

    /**
     * The list of trip night city entries associated with this city.
     *
     * <p>
     * This is a one-to-many relationship mapped by the "miasta" field in the {@link MiastaWycieczek} entity.
     * </p>
     */
    @OneToMany(mappedBy = "miasta")
    private List<MiastaWycieczek> miastaWycieczek;

    /**
     * Default no-argument constructor.
     */
    public Miasta() {}

    /**
     * Constructs a new {@code Miasta} instance with the specified city name.
     *
     * @param miasto the name of the city
     */
    public Miasta(final String miasto) {
        this.miasto = miasto;
    }

    /**
     * Returns a string representation of the city.
     *
     * <p>
     * This implementation returns the city name.
     * </p>
     *
     * @return the city name as a string
     */
    @Override
    public String toString() {
        return miasto;
    }
}
