package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The {@code ListyHoteli} class represents a hotel list entry that associates a room, a city for the trip night,
 * and a hotel. This entity uses a composite primary key defined by {@link ListyHoteliKey}.
 *
 * <p>
 * It maps to the database table "listy_hoteli" and establishes many-to-one relationships with the
 * {@link Pokoje}, {@link MiastaWycieczek}, and {@link Hotele} entities. The composite key is embedded
 * using {@link EmbeddedId} and the relationships are mapped via {@link MapsId} to the corresponding fields
 * in the composite key.
 * </p>
 *
 * @see ListyHoteliKey
 * @see Pokoje
 * @see MiastaWycieczek
 * @see Hotele
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "listy_hoteli")
public class ListyHoteli implements Models {

    /**
     * The composite primary key for this entity, consisting of references to a room, a trip night city, and a hotel code.
     */
    @EmbeddedId
    ListyHoteliKey id = new ListyHoteliKey();

    /**
     * The room associated with this hotel list entry.
     *
     * <p>
     * This relationship is mapped using {@link MapsId} with the key field "pokoj" in the composite key.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("pokoj")
    @JoinColumn(name = "pokoj")
    Pokoje pokoj;

    /**
     * The trip night city associated with this hotel list entry.
     *
     * <p>
     * This relationship is mapped using {@link MapsId} with the key field "miastoWycieczki" in the composite key.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("miastoWycieczki")
    @JoinColumn(name = "miasto_wycieczki")
    MiastaWycieczek miastoWycieczki;

    /**
     * The hotel associated with this hotel list entry.
     *
     * <p>
     * This relationship is mapped using {@link MapsId} with the key field "hotelKod" in the composite key.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("hotelKod")
    @JoinColumn(name = "hotel")
    Hotele hotel;
}
